package ru.poteha.rent.itemstatic.region.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RegionShort {
    @Schema(description = "Идентификатор региона")
    private UUID id;
    @Schema(description = "Наименование региона")
    private String name;
    @Schema(description = "Код страны")
    private Country country;
}