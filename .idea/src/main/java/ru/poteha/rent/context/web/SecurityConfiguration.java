package ru.poteha.rent.context.web;

import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.annotation.AnnotationTemplateExpressionDefaults;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.poteha.rent.auth.AuthService;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Slf4j
@Configuration
@SecurityScheme(
        name = "auth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            AuthService authService,
                                            UrlBasedCorsConfigurationSource corsSource) throws Exception {
        http.addFilterBefore(authService::jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.cors(it -> it.configurationSource(corsSource));
        http.csrf(AbstractHttpConfigurer::disable);
        //
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        //
        http.exceptionHandling(except -> except
                .authenticationEntryPoint(authService::failure)
                .accessDeniedHandler(authService::failure)
        );
        //
        http.oauth2Login(login -> login
                .successHandler(authService::authenticate)
                .failureHandler(authService::failure)
        );
        //
        http.formLogin(login -> login
                .successHandler(authService::authenticate)
                .failureHandler(authService::failure)
        );
        //
        http.logout(logout -> logout
                .logoutSuccessHandler(authService::logout)
        );
        return http.build();
    }

    @Bean
    RSASSAVerifier rsassaVerifier(@Value("${spring.security.jwt.public}") RSAPublicKey publicKey) {
        return new RSASSAVerifier(publicKey);
    }

    @Bean
    JWSSigner jwssigner(@Value("${spring.security.jwt.private}") RSAPrivateKey privateKey) {
        return new RSASSASigner(privateKey);
    }

    @Bean
    AnnotationTemplateExpressionDefaults annotationTemplateExpression() {
        return new AnnotationTemplateExpressionDefaults();
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfiguration() {
        var cors = new CorsConfiguration();
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        cors.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
        cors.setAllowedOrigins(List.of("http://localhost:3000"));
        cors.setAllowedHeaders(List.of("*"));
        cors.setAllowCredentials(true);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}