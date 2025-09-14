package ru.poteha.rent.itemstatic.feature.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.poteha.rent.itemstatic.feature.FeatureRepository;
import ru.poteha.rent.itemstatic.feature.FeatureToggle;
import ru.poteha.rent.itemstatic.feature.impl.jpa.Feature;
import ru.poteha.rent.itemstatic.feature.model.FeatureException;
import ru.poteha.rent.itemstatic.feature.model.FeatureSearch;
import ru.poteha.rent.itemstatic.feature.model.FeatureShort;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeatureToggleImpl implements FeatureToggle {
    private final FeatureRepository featureRepository;
    private final FeatureMappers featureMapper;

    @Override
    public boolean isEnabled(String mainId, String... id) {
        return featureRepository.findFirstByKeyInOrderByKeyDesc(addPrefix(mainId, id)).map(Feature::isFlag).orElseThrow(FeatureException.NotFound::new);
    }

    @Override
    @Cacheable(cacheNames = "feature", key = "#id")
    public boolean isEnabled(String id) {
        return featureRepository.findByKey(id).map(Feature::isFlag).orElseThrow(FeatureException.NotFound::new);
    }

    @Override
    public Page<FeatureShort> searchBy(FeatureSearch search, Pageable page) {
        return featureRepository.findByKeyWithin(search.getQuery(), page).map(featureMapper::toShort);
    }

    @Override
    @CacheEvict(cacheNames = "feature", key = "#key")
    public void change(String key, boolean flag) {
        var feature = featureRepository.findByKey(key).orElseGet(Feature::new);
        featureMapper.updateFeature(feature, key, flag);
        featureRepository.save(feature);
    }

    @Override
    public void remove(UUID id) {
        featureRepository.findById(id).ifPresent(feature -> {
            if (feature.isRequired())
                throw new FeatureException.Required();
            featureRepository.delete(feature);
        });
    }

    private List<String> addPrefix(String mainId, String... id) {
        if (id == null || id.length == 0) return List.of(mainId);

        var keys = new ArrayList<String>();
        for (var s : id) {
            keys.add(mainId + "-" + s);
        }
        keys.add(mainId);
        return keys;
    }
}
