package ru.poteha.rent.support.discuss;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.poteha.rent.support.discuss.impl.jpa.SupportDiscuss;

import java.util.UUID;

public interface SupportDiscussRepository extends JpaRepository<SupportDiscuss, UUID>, JpaSpecificationExecutor<SupportDiscuss> {
    Page<SupportDiscuss> findByRequestId(UUID requestId, Pageable pageable);
}
