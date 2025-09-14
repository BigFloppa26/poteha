package ru.poteha.rent.itemstatic.region.port.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.poteha.rent.context.web.annotation.ApiOperation;
import ru.poteha.rent.itemstatic.region.RegionService;
import ru.poteha.rent.itemstatic.region.model.RegionSearch;
import ru.poteha.rent.itemstatic.region.model.RegionShort;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/region")
public class RegionController {

    private final RegionService regionService;

    @ApiOperation(
            method = RequestMethod.GET,
            description = "Найти регион",
            tags = "Регионы"
    )
    PagedModel<RegionShort> searchRegion(@ParameterObject RegionSearch regionSearch, Pageable pageable) {
        return new PagedModel<>(regionService.searchBy(regionSearch, pageable));
    }
}