package ru.poteha.rent.itemstatic.region;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.poteha.rent.itemstatic.region.impl.jpa.Region;

import java.util.UUID;

public interface RegionRepository extends JpaRepository<Region, UUID>, JpaSpecificationExecutor<Region> { }