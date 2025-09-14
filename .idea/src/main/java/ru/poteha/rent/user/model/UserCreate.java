package ru.poteha.rent.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCreate {
    @Email
    @NotBlank
    @Schema(description = "Почта")
    private String email;
    @NotBlank
    @Schema(description = "Пароль")
    private String password;
}
