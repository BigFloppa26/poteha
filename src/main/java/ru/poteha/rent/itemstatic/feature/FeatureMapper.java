package ru.poteha.rent.itemstatic.feature;

import ru.poteha.rent.itemstatic.feature.impl.jpa.Feature;
import ru.poteha.rent.itemstatic.feature.model.FeatureShort;

public interface FeatureMapper {
    FeatureShort toShort(Feature feature);
}
