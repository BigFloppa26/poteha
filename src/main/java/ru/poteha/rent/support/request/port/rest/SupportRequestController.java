package ru.poteha.rent.support.request.port.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.poteha.rent.context.web.annotation.ApiOperation;
import ru.poteha.rent.support.request.SupportRequestService;
import ru.poteha.rent.support.request.model.SupportRequestCreate;
import ru.poteha.rent.support.request.model.SupportRequestShort;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/support")
@RequiredArgsConstructor
public class SupportRequestController {

    private final SupportRequestService supportRequestService;

    @ApiOperation(
            method = RequestMethod.GET,
            description = "Мои обращения",
            tags = "Поддержка"
    )
    PagedModel<SupportRequestShort> search(@AuthenticationPrincipal UUID userId, @ParameterObject Pageable page) {
        return new PagedModel<>(supportRequestService.search(userId, page));
    }

    @ApiOperation(
            method = RequestMethod.POST,
            description = "Новое обращение",
            tags = "Поддержка"
    )
    SupportRequestShort createRequest(@AuthenticationPrincipal UUID userId,
                                      @RequestBody @Valid SupportRequestCreate request) {
        return supportRequestService.createSupportRequest(request, userId);
    }

    @ApiOperation(
            path = "/{requestId}/close",
            method = RequestMethod.PUT,
            description = "Закрыть обращение",
            tags = "Поддержка"
    )
    void closeRequest(@AuthenticationPrincipal UUID userId, @PathVariable UUID requestId) {
        supportRequestService.closeRequest(userId, requestId);
    }
}
