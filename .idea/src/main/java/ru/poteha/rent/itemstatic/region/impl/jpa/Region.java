package ru.poteha.rent.itemstatic.region.impl.jpa;

import jakarta.persistence.*;
import lombok.*;
import ru.poteha.rent.itemstatic.region.model.Country;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "APP_REGION")
public class Region {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Country country;
}