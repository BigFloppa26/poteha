package ru.poteha.rent.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import ru.poteha.rent.auth.impl.jpa.Auth;
import ru.poteha.rent.auth.model.AuthRefresh;
import ru.poteha.rent.auth.model.AuthShort;
import ru.poteha.rent.auth.model.AuthToken;

import java.io.IOException;
import java.util.UUID;

public interface AuthService {

    Page<AuthShort> searchBy(UUID userId, Pageable page);

    AuthToken updateAccessToken(Auth auth, AuthRefresh request);

    //
    //
    //
    void jwtFilter(HttpServletRequest rq, HttpServletResponse rp, FilterChain chain) throws ServletException, IOException;

    void jwtFilter(ServletRequest rq, ServletResponse rp, FilterChain chain) throws ServletException, IOException;

    //
    //
    //
    void authenticate(HttpServletRequest rq, HttpServletResponse rp, Authentication auth);

    void failure(HttpServletRequest rq, HttpServletResponse rp, RuntimeException except);

    void logout(HttpServletRequest rq, HttpServletResponse rp, Authentication auth);
}
