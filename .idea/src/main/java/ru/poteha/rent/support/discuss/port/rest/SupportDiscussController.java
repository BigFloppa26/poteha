package ru.poteha.rent.support.discuss.port.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.poteha.rent.context.web.annotation.ApiOperation;
import ru.poteha.rent.support.discuss.SupportDiscussService;
import ru.poteha.rent.support.discuss.model.SupportDiscussCreate;
import ru.poteha.rent.support.discuss.model.SupportDiscussShort;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/support")
@RequiredArgsConstructor
public class SupportDiscussController {

    private final SupportDiscussService supportDiscussService;

    @ApiOperation(
            path = "/{requestId}/discuss",
            method = RequestMethod.GET,
            description = "Обсуждение по обращению",
            tags = "Поддержка"
    )
    PagedModel<SupportDiscussShort> supportDiscuss(@PathVariable UUID requestId,
                                                   @ParameterObject Pageable page,
                                                   @AuthenticationPrincipal UUID userId) {
        return new PagedModel<>(supportDiscussService.search(requestId, page, userId));
    }

    @ApiOperation(
            path = "/{requestId}/discuss",
            method = RequestMethod.POST,
            description = "Отправить сообщение",
            tags = "Поддержка"
    )
    SupportDiscussShort sendMessage(@PathVariable UUID requestId,
                                    @AuthenticationPrincipal UUID userId,
                                    @RequestBody @Valid SupportDiscussCreate request) {
        return supportDiscussService.sendMessage(requestId, userId, request);
    }
}