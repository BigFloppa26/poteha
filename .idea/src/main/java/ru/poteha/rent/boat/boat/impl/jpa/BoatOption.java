package ru.poteha.rent.boat.boat.impl.jpa;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import ru.poteha.rent.boat.option.impl.jpa.Option;

@Setter
@Getter
@Embeddable
public class BoatOption {
    @ManyToOne
    private Option option;

    private boolean include;

    private Double price;
}
