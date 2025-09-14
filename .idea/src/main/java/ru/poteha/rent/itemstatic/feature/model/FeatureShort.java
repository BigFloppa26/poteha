package ru.poteha.rent.itemstatic.feature.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class FeatureShort {
    private UUID id;
    private String key;
    private boolean flag;
    private boolean required;
}
