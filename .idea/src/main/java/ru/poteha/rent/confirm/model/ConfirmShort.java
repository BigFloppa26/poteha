package ru.poteha.rent.confirm.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class ConfirmShort {
    @Schema(description = "Идентификатор кода подтверждения")
    private UUID id;
    @Schema(description = "Адрес для перехода")
    private String url;
    @Schema(description = "Подтверждено")
    private boolean confirmed;
    @Schema(description = "Время истечения")
    private long expiresIn;
    @Schema(description = "Количество попыток")
    private int attempts;
}
