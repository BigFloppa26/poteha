package ru.poteha.rent.confirm.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class ConfirmCode {
    @Schema(description = "Идентификатор кода подтверждения")
    private UUID id;
    @Schema(description = "Код подтверждения")
    private String code;
}
