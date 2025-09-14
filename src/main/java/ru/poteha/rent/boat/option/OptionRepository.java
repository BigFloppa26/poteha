package ru.poteha.rent.boat.option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.poteha.rent.boat.option.impl.jpa.Option;

import java.util.Optional;
import java.util.UUID;

public interface OptionRepository extends JpaRepository<Option, UUID>, JpaSpecificationExecutor<Option> {

    Optional<Option> findOptionById(UUID id);
}
