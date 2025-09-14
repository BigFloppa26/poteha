package ru.poteha.rent.itemstatic.region;

import ru.poteha.rent.itemstatic.region.impl.jpa.Region;
import ru.poteha.rent.itemstatic.region.model.RegionShort;

public interface RegionMapper {

    RegionShort toShort(Region region);
}
