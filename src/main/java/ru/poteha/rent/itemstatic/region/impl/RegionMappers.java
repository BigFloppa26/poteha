package ru.poteha.rent.itemstatic.region.impl;

import org.springframework.stereotype.Component;
import ru.poteha.rent.itemstatic.region.RegionMapper;
import ru.poteha.rent.itemstatic.region.impl.jpa.Region;
import ru.poteha.rent.itemstatic.region.model.RegionShort;

@Component
public class RegionMappers implements RegionMapper {

    @Override
    public RegionShort toShort(Region region) {
        var regionShort = new RegionShort();
        regionShort.setId(region.getId());
        regionShort.setName(region.getName());
        regionShort.setCountry(region.getCountry());
        return regionShort;
    }
}
