package ru.poteha.rent.boat.boat.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class BoatCreate {

    @NotBlank
    private String name;
    private String brand;
    private String description;

    @NotNull
    private double latitude;
    @NotNull
    private double longitude;

    @Positive
    private Integer maxLoad;
    @Positive
    private Integer capacity;

    @Pattern(regexp = "^\\d{4}$")
    private Integer buildYear;
    @Pattern(regexp = "^\\d{4}$")
    private Integer retrofitYear;

    @Positive
    private Integer width;
    @Positive
    private Integer height;
    @Positive
    private Integer length;

    @NotNull
    private BoatType type;
    private BoatDesign design;
    private BoatMaterial material;

    private List<BoatCreateOption> options = new ArrayList<>();

    public record BoatCreateOption(UUID optionId, boolean include, Double price) {
    }
}
