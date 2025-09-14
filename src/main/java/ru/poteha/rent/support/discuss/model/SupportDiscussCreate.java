package ru.poteha.rent.support.discuss.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SupportDiscussCreate {
    @NotBlank
    private String message;
}
