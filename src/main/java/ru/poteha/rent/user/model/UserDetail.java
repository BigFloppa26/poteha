package ru.poteha.rent.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class UserDetail {
    @Schema(description = "Идентификатор пользователя")
    private UUID id;
    @Schema(description = "Юзернейм")
    private String username;
    @Schema(description = "Роли")
    private List<Role> roles;
}
