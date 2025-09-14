package ru.poteha.rent.auth.impl;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import ru.poteha.rent.auth.AuthMapper;
import ru.poteha.rent.auth.impl.jpa.Auth;
import ru.poteha.rent.auth.model.AuthShort;
import ru.poteha.rent.auth.model.AuthToken;
import ru.poteha.rent.user.impl.jpa.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
class AuthMappers implements AuthMapper {
    private final EntityManager em;

    @Override
    public AuthShort toShort(Auth auth) {
        var authShort = new AuthShort();
        authShort.setId(auth.getId());
        authShort.setLastUse(auth.getLastUse());
        return authShort;
    }

    Auth fromUser(HttpServletRequest rq, UUID userId) {
        return fromUser(rq, em.find(User.class, userId));
    }

    Auth fromUser(HttpServletRequest rq, User user) {
        var auth = new Auth();
        auth.setJti(UUID.randomUUID().toString());
        auth.setUser(user);
        auth.setUserAgent(rq.getHeader(HttpHeaders.USER_AGENT));
        return auth;
    }

    AuthToken toAuthToken(Auth auth, long expires, Supplier<String> accessToken) {
        var token = new AuthToken();
        token.setRoles(auth.getUser().getAuthorities());
        token.setRefreshToken(auth.getId().toString());
        token.setAccessToken(accessToken.get());
        token.setTokenType("Bearer");
        token.setExpiresIn(expires);
        return token;
    }

    JWTClaimsSet toClaims(Auth auth, Date expires) {
        return new JWTClaimsSet.Builder()
                .audience(auth.getUser().getAuthorities().stream().map(Enum::name).toList())
                .subject(auth.getUser().getId().toString())
                .expirationTime(expires)
                .jwtID(auth.getJti())
                .build();
    }

    SignedJWT toSignedJWT(Auth auth, Date expires) {
        return new SignedJWT(
                new JWSHeader(JWSAlgorithm.RS512),
                toClaims(auth, expires)
        );
    }

    Authentication toAuthentication(BiFunction<String, UUID, Optional<Auth>> function, Supplier<JWTClaimsSet> c) {
        return toAuthentication(function, c.get());
    }

    Authentication toAuthentication(BiFunction<String, UUID, Optional<Auth>> function, JWTClaimsSet c) {
        return toAuthentication(function, UUID.fromString(c.getSubject()), c.getJWTID(), c.getAudience());
    }

    Authentication toAuthentication(BiFunction<String, UUID, Optional<Auth>> function, UUID subject, String jwtId, List<String> audience) {
        var auth = new UsernamePasswordAuthenticationToken(subject, jwtId, AuthorityUtils.createAuthorityList(audience));
        auth.setDetails((Supplier<Auth>) () -> function.apply(jwtId, subject).orElseThrow(() -> new DisabledException("Ошибка авторизации")));
        return auth;
    }
}