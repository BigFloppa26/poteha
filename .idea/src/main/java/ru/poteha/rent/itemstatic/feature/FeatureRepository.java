package ru.poteha.rent.itemstatic.feature;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.poteha.rent.itemstatic.feature.impl.jpa.Feature;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeatureRepository extends JpaRepository<Feature, UUID> {
    Optional<Feature> findFirstByKeyInOrderByKeyDesc(List<String> keys);

    Page<Feature> findByKeyWithin(String key, Pageable pageable);

    Optional<Feature> findByKey(String key);
}
