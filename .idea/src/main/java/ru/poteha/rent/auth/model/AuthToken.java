package ru.poteha.rent.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.poteha.rent.user.model.Role;

import java.util.List;

@Setter
@Getter
public class AuthToken {
    @Schema(description = "Access токен")
    private String accessToken;
    @Schema(description = "Refresh токен")
    private String refreshToken;
    @Schema(description = "Права пользователя")
    private List<Role> roles;
    @Schema(description = "Тип токена")
    private String tokenType;
    @Schema(description = "Окончание действия")
    private long expiresIn;
}
