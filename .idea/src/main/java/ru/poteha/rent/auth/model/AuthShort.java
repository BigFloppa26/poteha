package ru.poteha.rent.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class AuthShort {
    @Schema(description = "Идентификатор аутентификации")
    protected UUID id;
    @Schema(description = "Последнее использование")
    protected LocalDateTime lastUse;
}
