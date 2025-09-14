package ru.poteha.rent.itemstatic.region.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionSearch {
    @Schema(description = "Название региона")
    private String name;
}
