package ru.poteha.rent.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthRefresh {
    @NotBlank
    @Schema(description = "Рефреш токен")
    private String refreshToken;
}
