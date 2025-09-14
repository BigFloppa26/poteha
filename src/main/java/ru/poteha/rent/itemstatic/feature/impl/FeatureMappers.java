package ru.poteha.rent.itemstatic.feature.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.poteha.rent.itemstatic.feature.FeatureMapper;
import ru.poteha.rent.itemstatic.feature.impl.jpa.Feature;
import ru.poteha.rent.itemstatic.feature.model.FeatureChange;
import ru.poteha.rent.itemstatic.feature.model.FeatureShort;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeatureMappers implements FeatureMapper {

    @Override
    public FeatureShort toShort(Feature feature) {
        var target = new FeatureShort();
        target.setRequired(feature.isRequired());
        target.setFlag(feature.isFlag());
        target.setKey(feature.getKey());
        target.setId(feature.getId());
        return target;
    }

    public void updateFeature(Feature feature, String key, boolean flag) {
        feature.setRequired(false);
        feature.setFlag(flag);
        feature.setKey(key);
    }
}
