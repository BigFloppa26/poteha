package ru.poteha.rent.boat.boat.impl.jpa;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class BoatSize {
    private Integer width;
    private Integer height;
    private Integer length;
}
