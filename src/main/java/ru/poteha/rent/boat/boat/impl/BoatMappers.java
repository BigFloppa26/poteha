package ru.poteha.rent.boat.boat.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.poteha.rent.boat.boat.BoatMapper;
import ru.poteha.rent.boat.boat.impl.jpa.Boat;
import ru.poteha.rent.boat.boat.impl.jpa.BoatOption;
import ru.poteha.rent.boat.boat.impl.jpa.BoatSize;
import ru.poteha.rent.boat.boat.model.BoatCreate;
import ru.poteha.rent.boat.boat.model.BoatDetail;
import ru.poteha.rent.boat.boat.model.BoatShort;
import ru.poteha.rent.boat.option.impl.jpa.Option;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
class BoatMappers implements BoatMapper {
    private final EntityManager em;

    @Override
    public BoatDetail toDetail(Boat boat) {
        return null;
    }

    @Override
    public BoatShort toShort(Boat boat, Function<UUID, Optional<Option>> optionFunc) {
        var target = new BoatShort();
        target.setId(UUID.randomUUID());
        target.setName(boat.getName());
        target.setDescription(boat.getDescription());

        boat.getOptionIncludes().forEach(optionId -> optionFunc.apply(optionId).map(this::toOption)
                //
                .ifPresent(it -> target.getOptionIncludes().add(it)));

        return target;
    }

    @Override
    public Boat fromCreate(BoatCreate model) {
        var boat = new Boat();
        boat.setName(model.getName());
        boat.setBrand(model.getBrand());
        boat.setDescription(model.getDescription());
        boat.setLatitude(model.getLatitude());
        boat.setLongitude(model.getLongitude());
        boat.setMaxLoad(model.getMaxLoad());
        boat.setCapacity(model.getCapacity());
        boat.setBuildYear(model.getBuildYear());
        boat.setRetrofitYear(model.getRetrofitYear());
        boat.setSize(new BoatSize(
                model.getWidth(),
                model.getHeight(),
                model.getLength()
        ));
        boat.setType(model.getType());
        boat.setDesign(model.getDesign());
        boat.setMaterial(model.getMaterial());

        model.getOptions().forEach(it -> boat.getOptions().add(fromCreateOption(it)));
        return boat;
    }

    BoatShort.BoatOption toOption(Option option) {
        return new BoatShort.BoatOption(option.getId(), option.getName());
    }

    BoatOption fromCreateOption(BoatCreate.BoatCreateOption model) {
        var option = new BoatOption();
        option.setOption(em.getReference(Option.class, model.optionId()));
        option.setInclude(model.include());
        option.setPrice(model.price());
        return option;
    }

}
