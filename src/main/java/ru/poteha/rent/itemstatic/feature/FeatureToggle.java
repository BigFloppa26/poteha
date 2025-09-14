package ru.poteha.rent.itemstatic.feature;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.poteha.rent.itemstatic.feature.model.FeatureSearch;
import ru.poteha.rent.itemstatic.feature.model.FeatureShort;

import java.util.UUID;

public interface FeatureToggle {
    boolean isEnabled(String mainId, String... id);

    boolean isEnabled(String id);

    //
    //
    //
    Page<FeatureShort> searchBy(FeatureSearch search, Pageable page);

    void change(String key, boolean flag);

    void remove(UUID id);
}
