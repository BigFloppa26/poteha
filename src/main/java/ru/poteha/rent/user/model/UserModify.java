package ru.poteha.rent.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserModify {

    @Setter
    @Getter
    public static class ChangePassword {
        @NotBlank
        @Schema(description = "Новый пароль")
        private String newPassword;
    }

}
