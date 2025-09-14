package ru.poteha.rent.notification.port.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.poteha.rent.context.web.annotation.ApiOperation;
import ru.poteha.rent.notification.NotificationService;
import ru.poteha.rent.notification.model.NotificationCreate;
import ru.poteha.rent.notification.model.NotificationDetail;
import ru.poteha.rent.notification.model.NotificationSearch;
import ru.poteha.rent.notification.model.NotificationShort;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @ApiOperation(
            path = "/event",
            method = RequestMethod.GET,
            description = "Шина уведомлений",
            tags = "Уведомление"
    )
    SseEmitter event(@AuthenticationPrincipal UUID userId) {
        return notificationService.registerEmitter(userId, new SseEmitter(3_600_000L));
    }

    @ApiOperation(
            method = RequestMethod.POST,
            description = "Сформировать и отправить уведомление",
            tags = "Админка: Уведомление"
    )
    void send(@RequestBody @Valid NotificationCreate request) {
        notificationService.sendAsync(request);
    }

    @ApiOperation(
            method = RequestMethod.GET,
            description = "Поиск",
            tags = "Админка: Уведомление"
    )
    PagedModel<NotificationShort> search(@ParameterObject NotificationSearch search,
                                         @ParameterObject Pageable page) {
        return new PagedModel<>(notificationService.searchBy(search, page));
    }

    @ApiOperation(
            path = "/{id}",
            method = RequestMethod.GET,
            description = "Информация",
            tags = "Админка: Уведомление"
    )
    NotificationDetail notificationById(@PathVariable UUID id) {
        return notificationService.getDetailById(id);
    }
}