package ru.poteha.rent.confirm.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class ConfirmCypher {
    @NotNull
    private UUID confirmId;
    private String encrypt;
}
