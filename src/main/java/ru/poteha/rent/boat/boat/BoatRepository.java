package ru.poteha.rent.boat.boat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.poteha.rent.boat.boat.impl.jpa.Boat;

import java.util.UUID;

public interface BoatRepository extends JpaRepository<Boat, UUID>, JpaSpecificationExecutor<Boat> {
}
