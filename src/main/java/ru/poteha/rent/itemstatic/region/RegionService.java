package ru.poteha.rent.itemstatic.region;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.poteha.rent.itemstatic.region.model.RegionSearch;
import ru.poteha.rent.itemstatic.region.model.RegionShort;

public interface RegionService {

    Page<RegionShort> searchBy(RegionSearch search, Pageable page);
}
