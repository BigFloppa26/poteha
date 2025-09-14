package ru.poteha.rent.boat.boat.port.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import ru.poteha.rent.auth.impl.jpa.Auth;
import ru.poteha.rent.boat.boat.BoatService;
import ru.poteha.rent.boat.boat.model.BoatCreate;
import ru.poteha.rent.boat.boat.model.BoatDetail;
import ru.poteha.rent.boat.boat.model.BoatSearch;
import ru.poteha.rent.boat.boat.model.BoatShort;
import ru.poteha.rent.context.web.annotation.ApiOperation;
import ru.poteha.rent.context.web.annotation.AuthenticationDetails;

import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@RestController
@RequestMapping("/boat")
@RequiredArgsConstructor
public class BoatController {

    private final BoatService boatService;

    @ApiOperation(
            method = RequestMethod.POST,
            description = "Создание",
            tags = "Лодки",
            authorize = "hasAuthority('OWNER')"
    )
    void create(@AuthenticationDetails Supplier<Auth> auth, @RequestBody @Valid BoatCreate request) {
        boatService.createBoat(auth.get().getUser(), request);
    }

    @ApiOperation(
            method = RequestMethod.GET,
            description = "Поиск",
            tags = "Лодки"
    )
    PagedModel<BoatShort> search(@ParameterObject BoatSearch search, @ParameterObject Pageable page) {
        return new PagedModel<>(boatService.searchBy(search, page));
    }

    @ApiOperation(
            path = "/{id}",
            method = RequestMethod.GET,
            description = "Поиск",
            tags = "Лодки"
    )
    BoatDetail detailById(@PathVariable UUID id) {
        return null;
    }

}
