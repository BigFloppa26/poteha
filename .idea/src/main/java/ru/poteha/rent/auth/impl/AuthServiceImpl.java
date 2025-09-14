package ru.poteha.rent.auth.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.poteha.rent.auth.AuthRepository;
import ru.poteha.rent.auth.AuthService;
import ru.poteha.rent.auth.impl.jpa.Auth;
import ru.poteha.rent.auth.model.AuthRefresh;
import ru.poteha.rent.auth.model.AuthShort;
import ru.poteha.rent.auth.model.AuthToken;
import ru.poteha.rent.user.UserService;
import ru.poteha.rent.user.impl.jpa.User;

import java.io.IOException;
import java.text.ParseException;
import java.time.*;
import java.util.Date;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RSASSAVerifier rsassaVerifier;
    private final AuthRepository authRepository;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final AuthMappers authMapper;
    private final JWSSigner jwssigner;

    @Value("${spring.security.jwt.expires}")
    protected Duration expires;

    @Override
    public Page<AuthShort> searchBy(UUID userId, Pageable page) {
        return authRepository.findByUserId(userId, page).map(authMapper::toShort);
    }

    @Override
    public AuthToken updateAccessToken(Auth auth, AuthRefresh request) {
        if (!auth.getId().toString().equals(request.getRefreshToken()))
            throw new BadCredentialsException("Ошибка проверки прав");
        if (!auth.getUser().isEnabled())
            throw new DisabledException("Пользователь деактивирован");
        auth.setLastUse(LocalDateTime.now());
        authRepository.save(auth);
        return createToken(auth);
    }

    @Override
    public void jwtFilter(HttpServletRequest rq, HttpServletResponse rp, FilterChain chain) throws ServletException, IOException {
        authorizationBearer(rq, accessToken -> {
            var auth = createAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
        });
        chain.doFilter(rq, rp);
    }

    @Override
    public void jwtFilter(ServletRequest rq, ServletResponse rp, FilterChain chain) throws ServletException, IOException {
        this.jwtFilter((HttpServletRequest) rq, (HttpServletResponse) rp, chain);
    }

    @Override
    public void authenticate(HttpServletRequest rq, HttpServletResponse rp, Authentication auth) {
        if (auth instanceof UsernamePasswordAuthenticationToken token)
            authenticateUsernamePasswordToken(rq, rp, token);

        if (auth instanceof OAuth2AuthenticationToken token)
            authenticateOAuth2Token(rq, rp, token);
    }

    @Override
    public void failure(HttpServletRequest rq, HttpServletResponse rp, RuntimeException except) {
        failureHandle(rp, except);
    }

    @Override
    public void logout(HttpServletRequest rq, HttpServletResponse rp, Authentication authentication) {
        authorizationBearer(rq, accessToken -> {
            var jti = (String) createAuthentication(accessToken).getCredentials();
            authRepository.removeByJti(jti);
        });
    }

    void authenticateUsernamePasswordToken(HttpServletRequest rq, HttpServletResponse rp,
                                           UsernamePasswordAuthenticationToken token) {
        var user = (User) token.getPrincipal();
        if (!user.isEnabled())
            throw new DisabledException("Пользователь деактивирован");
        authenticateUser(rq, rp, user);
    }

    void authenticateOAuth2Token(HttpServletRequest rq, HttpServletResponse rp, OAuth2AuthenticationToken token) {
        var userId = userService.importOAuth2(token.getPrincipal());
        authenticateUser(rq, rp, userId);
    }

    void authenticateUser(HttpServletRequest rq, HttpServletResponse rp, UUID userId) {
        authenticate(rp, authMapper.fromUser(rq, userId));
    }

    void authenticateUser(HttpServletRequest rq, HttpServletResponse rp, User user) {
        authenticate(rp, authMapper.fromUser(rq, user));
    }

    void authenticate(HttpServletResponse rp, Auth auth) {
        successHandle(rp, authRepository.save(auth));
    }

    void failureHandle(HttpServletResponse rp, RuntimeException except) {
        handle(rp, ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, except.getLocalizedMessage()));
    }

    void successHandle(HttpServletResponse rp, Auth auth) {
        handle(rp, createToken(auth));
    }

    void handle(HttpServletResponse rp, Object payload) {
        rp.setContentType("application/json;charset=UTF-8");
        if (payload instanceof ProblemDetail pd)
            rp.setStatus(pd.getStatus());
        try (var sos = rp.getOutputStream()) {
            sos.write(objectMapper.writeValueAsBytes(payload));
        } catch (IOException e) {
            throw new AccessDeniedException(e.getLocalizedMessage());
        }
    }

    Authentication createAuthentication(String bearerToken) {
        return authMapper.toAuthentication(authRepository::findByJtiAndUserId, () -> {
            try {
                var signed = SignedJWT.parse(bearerToken);
                if (!signed.verify(rsassaVerifier))
                    throw new BadCredentialsException("Ошибка авторизации");
                return signed.getJWTClaimsSet();
            } catch (JOSEException | ParseException e) {
                throw new BadCredentialsException("Ошибка авторизации");
            }
        });
    }

    AuthToken createToken(Auth auth) {
        var current = new Date(Instant.now().plus(expires).toEpochMilli());
        return authMapper.toAuthToken(auth, current.getTime(), () -> {
            var jwt = authMapper.toSignedJWT(auth, current);
            try {
                jwt.sign(jwssigner);
            } catch (JOSEException e) {
                throw new AccessDeniedException("Ошибка при создании сессии", e);
            }
            return jwt.serialize();
        });
    }

    void authorizationBearer(HttpServletRequest rq, Consumer<String> consumer) {
        var header = rq.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer "))
            consumer.accept(header.substring(7));
    }
}