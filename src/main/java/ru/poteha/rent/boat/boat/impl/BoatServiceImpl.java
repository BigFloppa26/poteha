package ru.poteha.rent.boat.boat.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.poteha.rent.boat.boat.BoatRepository;
import ru.poteha.rent.boat.boat.BoatService;
import ru.poteha.rent.boat.boat.impl.jpa.Boat;
import ru.poteha.rent.boat.boat.impl.jpa.Boat_;
import ru.poteha.rent.boat.boat.model.BoatCreate;
import ru.poteha.rent.boat.boat.model.BoatSearch;
import ru.poteha.rent.boat.boat.model.BoatShort;
import ru.poteha.rent.boat.option.OptionRepository;
import ru.poteha.rent.user.impl.jpa.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoatServiceImpl implements BoatService {

    private final OptionRepository optionRepository;
    private final BoatRepository boatRepository;
    private final BoatMappers boatMapper;

    @Override
    public void createBoat(User currentUser, BoatCreate model) {
        var boat = boatMapper.fromCreate(model);
        boat.setOwner(currentUser);
        boatRepository.save(boat);
    }

    @Override
    public Page<BoatShort> searchBy(BoatSearch search, Pageable page) {
        return boatRepository.findBy((root, cq, cb) -> {
            var predicates = new ArrayList<Predicate>();
            //
            predicates.add(cb.between(root.get(Boat_.LONGITUDE), search.getMinLongitude(), search.getMaxLongitude()));
            predicates.add(cb.between(root.get(Boat_.LATITUDE), search.getMinLatitude(), search.getMaxLatitude()));

            ifPresent(predicates, search.getType(), it -> root.get(Boat_.TYPE).in(it));
            ifPresent(predicates, search.getDesign(), it -> root.get(Boat_.DESIGN).in(it));
            ifPresent(predicates, search.getMaterial(), it -> root.get(Boat_.MATERIAL).in(it));

            return cb.and(predicates.toArray(Predicate[]::new));
        }, query -> query.page(page).map(this::aggregate));
    }

    protected BoatShort aggregate(Boat boat) {
        return boatMapper.toShort(boat, optionRepository::findOptionById);
    }

    <I1> void ifPresent(List<Predicate> predicates, I1 i1, Function<I1, Predicate> function) {
        if (!ObjectUtils.isEmpty(i1)) predicates.add(function.apply(i1));
    }

}
