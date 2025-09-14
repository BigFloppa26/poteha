package ru.poteha.rent.confirm.port.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.poteha.rent.confirm.ConfirmService;
import ru.poteha.rent.confirm.model.ConfirmCode;
import ru.poteha.rent.confirm.model.ConfirmShort;
import ru.poteha.rent.context.web.annotation.ApiOperation;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/confirm")
@RequiredArgsConstructor
public class ConfirmController {

    private final ConfirmService confirmService;

    @ApiOperation(
            value = "/consume",
            method = RequestMethod.PUT,
            description = "Проверка кода подтверждения",
            tags = "Подтверждение",
            authorize = "permitAll()"
    )
    ConfirmShort consume(@RequestBody @Valid ConfirmCode request) {
        return confirmService.consume(request);
    }

    @ApiOperation(
            value = "/resend",
            method = RequestMethod.PUT,
            description = "Отправить код повторно",
            tags = "Подтверждение",
            authorize = "permitAll()"
    )
    ConfirmShort resend(@RequestParam UUID confirmId) {
        return confirmService.resend(confirmId);
    }
}