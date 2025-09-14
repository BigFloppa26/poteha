package ru.poteha.rent.itemstatic.feature.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FeatureChange {
    @NotEmpty
    private String key;
    @NotNull
    private boolean flag;
}
