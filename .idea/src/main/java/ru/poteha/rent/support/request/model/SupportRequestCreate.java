package ru.poteha.rent.support.request.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SupportRequestCreate {
    @NotBlank
    private String title;
    @NotBlank
    private String message;
}
