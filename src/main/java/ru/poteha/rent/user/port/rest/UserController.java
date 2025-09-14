package ru.poteha.rent.user.port.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.poteha.rent.auth.impl.jpa.Auth;
import ru.poteha.rent.confirm.ConfirmService;
import ru.poteha.rent.confirm.model.ConfirmCreate;
import ru.poteha.rent.confirm.model.ConfirmCypher;
import ru.poteha.rent.confirm.model.ConfirmShort;
import ru.poteha.rent.context.web.annotation.ApiOperation;
import ru.poteha.rent.context.web.annotation.AuthenticationDetails;
import ru.poteha.rent.itemstatic.feature.FeatureToggle;
import ru.poteha.rent.notification.model.NotificationCreate;
import ru.poteha.rent.notification.model.NotificationType;
import ru.poteha.rent.user.UserService;
import ru.poteha.rent.user.model.UserCreate;
import ru.poteha.rent.user.model.UserDetail;
import ru.poteha.rent.user.model.UserModify.*;
import ru.poteha.rent.user.model.UserSearch;
import ru.poteha.rent.user.model.UserShort;

import java.net.URI;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final ConfirmService confirmService;
    private final FeatureToggle featureToggle;
    private final UserService userService;

    @ApiOperation(
            method = RequestMethod.GET,
            description = "Поиск",
            tags = "Пользователи"
    )
    PagedModel<UserShort> search(@AuthenticationPrincipal UUID userId,
                                 @ParameterObject UserSearch search,
                                 @ParameterObject Pageable page) {
        return new PagedModel<>(userService.searchBy(search, userId, page));
    }

    @ApiOperation(
            path = "/{id}",
            method = RequestMethod.GET,
            description = "Информация о пользователе",
            tags = "Пользователи"
    )
    UserDetail userById(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @ApiOperation(
            path = "/info",
            method = RequestMethod.GET,
            description = "Информация о пользователе",
            tags = "Пользователи"
    )
    UserDetail userInfo(@AuthenticationPrincipal UUID userId) {
        return userService.getById(userId);
    }

    @ApiOperation(
            path = "/{id}",
            method = RequestMethod.DELETE,
            description = "Удаление пользователя",
            tags = {"Пользователи", "Админка: Пользователи"},
            authorize = "hasAuthority('ADMIN') or (#id == principal)"
    )
    void userDelete(@PathVariable UUID id) {
        userService.deleteUser(id, false);
    }

    //
    //
    //
    @ApiOperation(
            path = "/{id}/password",
            method = RequestMethod.PUT,
            description = "Установить/сменить пароль",
            tags = {"Пользователи", "Админка: Пользователи"},
            authorize = "hasAuthority('ADMIN') or (#id == principal)"
    )
    ConfirmShort changePassword(@PathVariable UUID id,
                                @RequestHeader("Origin") URI origin,
                                @AuthenticationDetails Supplier<Auth> auth,
                                @RequestBody @Valid ChangePassword request) {
        var user = auth.get().getUser();

        var feature = featureToggle.isEnabled("PWD_CHANGE", user.getUsername());
        return confirmService.generateAndSend(feature, () -> {
            var confirm = new ConfirmCreate(origin + "/user/%s/password".formatted(id), request);
            confirm.setNotification(NotificationCreate.builder()
                    .recipient(user.getUsername())
                    .type(NotificationType.EMAIL)
                    .template("pwdChange.ftl")
                    .header("Смена пароля")
                    .build());
            return confirm;
        });
    }

    @ApiOperation(
            path = "/{id}/password",
            method = RequestMethod.GET,
            description = "Подтверждение: Установить/сменить пароль",
            tags = "Пользователи",
            authorize = "permitAll()"
    )
    void changePasswordConfirm(@PathVariable UUID id, @ParameterObject @Valid ConfirmCypher confirm) {
        var changePassword = confirmService.getPayload(confirm, ChangePassword.class);
        userService.changePassword(id, changePassword);
    }

    //
    //
    //
    @ApiOperation(
            path = "/create",
            method = RequestMethod.POST,
            description = "Создание/Регистрация пользователя",
            tags = "Пользователи",
            authorize = "permitAll()"
    )
    ConfirmShort createUser(@RequestHeader("Origin") URI origin, @RequestBody @Valid UserCreate request) {
        userService.createVerify(request);

        return confirmService.generateAndSend("USR_CREATE", () -> {
            var confirm = new ConfirmCreate(origin + "/user/create", request);
            confirm.setNotification(NotificationCreate.builder()
                    .recipient(request.getEmail())
                    .type(NotificationType.EMAIL)
                    .template("usrCreate.ftl")
                    .header("Регистрация")
                    .build());
            return confirm;
        });
    }

    @ApiOperation(
            path = "/create",
            method = RequestMethod.GET,
            description = "Подтверждение: Создание/Регистрация пользователя",
            tags = "Пользователи",
            authorize = "permitAll()"
    )
    void createUserConfirm(@ParameterObject @Valid ConfirmCypher confirm) {
        var createUser = confirmService.getPayload(confirm, UserCreate.class);
        userService.create(createUser);
    }

}
