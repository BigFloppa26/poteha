package ru.poteha.rent.auth.port.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.poteha.rent.auth.AuthService;
import ru.poteha.rent.auth.impl.jpa.Auth;
import ru.poteha.rent.auth.model.AuthRefresh;
import ru.poteha.rent.auth.model.AuthShort;
import ru.poteha.rent.auth.model.AuthToken;
import ru.poteha.rent.context.web.annotation.ApiOperation;
import ru.poteha.rent.context.web.annotation.AuthenticationDetails;

import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ApiOperation(
            method = RequestMethod.GET,
            description = "Список",
            tags = "Авторизация"
    )
    PagedModel<AuthShort> search(@AuthenticationPrincipal UUID userId, @ParameterObject Pageable page) {
        return new PagedModel<>(authService.searchBy(userId, page));
    }

    @ApiOperation(
            path = "/refresh",
            method = RequestMethod.POST,
            description = "Перевыпуск access-token",
            tags = "Авторизация"
    )
    AuthToken refresh(@AuthenticationDetails Supplier<Auth> auth, @RequestBody @Valid AuthRefresh request) {
        return authService.updateAccessToken(auth.get(), request);
    }

}
