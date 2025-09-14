package ru.poteha.rent.boat.boat;

import ru.poteha.rent.boat.boat.impl.jpa.Boat;
import ru.poteha.rent.boat.boat.model.BoatCreate;
import ru.poteha.rent.boat.boat.model.BoatDetail;
import ru.poteha.rent.boat.boat.model.BoatShort;
import ru.poteha.rent.boat.option.impl.jpa.Option;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public interface BoatMapper {
    BoatDetail toDetail(Boat boat);

    BoatShort toShort(Boat boat, Function<UUID, Optional<Option>> optionFunc);

    Boat fromCreate(BoatCreate model);
}
