package ru.poteha.rent.boat.boat.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class BoatShort {
    private UUID id;
    private String name;
    private String description;
    private List<BoatOption> optionIncludes = new ArrayList<>();

    public record BoatOption(UUID id, String name) {
    }
}
