package ru.poteha.rent.itemstatic.region.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.poteha.rent.itemstatic.region.RegionMapper;
import ru.poteha.rent.itemstatic.region.RegionRepository;
import ru.poteha.rent.itemstatic.region.RegionService;
import ru.poteha.rent.itemstatic.region.impl.jpa.Region_;
import ru.poteha.rent.itemstatic.region.model.RegionSearch;
import ru.poteha.rent.itemstatic.region.model.RegionShort;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;

    @Override
    public Page<RegionShort> searchBy(RegionSearch search, Pageable page) {
        return regionRepository.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(search.getName())) {
                predicates.add(
                        builder.like(
                                builder.lower(root.get(Region_.NAME)),
                                "%" + search.getName().toLowerCase() + "%"
                        )
                );
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        }, page).map(regionMapper::toShort);
    }
}
