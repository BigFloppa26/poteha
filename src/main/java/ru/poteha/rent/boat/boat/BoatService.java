package ru.poteha.rent.boat.boat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.poteha.rent.boat.boat.model.BoatCreate;
import ru.poteha.rent.boat.boat.model.BoatSearch;
import ru.poteha.rent.boat.boat.model.BoatShort;
import ru.poteha.rent.user.impl.jpa.User;

public interface BoatService {
    void createBoat(User currentUser, BoatCreate model);

    Page<BoatShort> searchBy(BoatSearch search, Pageable page);
}
