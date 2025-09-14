package ru.poteha.rent.itemstatic.feature.port.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import ru.poteha.rent.context.web.annotation.ApiOperation;
import ru.poteha.rent.itemstatic.feature.FeatureToggle;
import ru.poteha.rent.itemstatic.feature.model.FeatureChange;
import ru.poteha.rent.itemstatic.feature.model.FeatureSearch;
import ru.poteha.rent.itemstatic.feature.model.FeatureShort;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/feature")
@RequiredArgsConstructor
public class FeatureController {

    private final FeatureToggle featureToggle;

    @ApiOperation(
            method = RequestMethod.PUT,
            description = "Создать/Изменить фичу",
            tags = "Фичи",
            authorize = "hasAuthority('ADMIN')"
    )
    void changeFeature(@RequestBody @Valid FeatureChange request) {
        featureToggle.change(request.getKey(), request.isFlag());
    }

    @ApiOperation(
            path = "/{id}",
            method = RequestMethod.DELETE,
            description = "Удалить фичу",
            tags = "Фичи",
            authorize = "hasAuthority('ADMIN')"
    )
    void removeFeature(@PathVariable UUID id) {
        featureToggle.remove(id);
    }

    @ApiOperation(
            method = RequestMethod.GET,
            description = "Поиск",
            tags = "Фичи",
            authorize = "hasAuthority('ADMIN')"
    )
    PagedModel<FeatureShort> search(@ParameterObject FeatureSearch request, @ParameterObject Pageable page) {
        return new PagedModel<>(featureToggle.searchBy(request, page));
    }

}
