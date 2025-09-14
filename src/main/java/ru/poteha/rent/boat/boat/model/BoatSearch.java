package ru.poteha.rent.boat.boat.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class BoatSearch {
    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;

    private Integer minLength;
    private Integer maxLength;

    private Integer minWidth;
    private Integer maxWidth;

    private Integer minHeight;
    private Integer maxHeight;

    private Integer minLoad;
    private Integer maxLoad;

    private Integer minCapacity;
    private Integer maxCapacity;

    private List<UUID> option;
    private List<BoatType> type;
    private List<BoatDesign> design;
    private List<BoatMaterial> material;
}
