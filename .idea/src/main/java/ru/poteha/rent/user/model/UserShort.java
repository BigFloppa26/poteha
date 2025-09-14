package ru.poteha.rent.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class UserShort {
    @Schema(description = "Идентификатор пользователя")
    protected UUID id;
    @Schema(description = "Юзернейм")
    protected String username;
    @Schema(description = "Имя")
    protected String firstName;
}
