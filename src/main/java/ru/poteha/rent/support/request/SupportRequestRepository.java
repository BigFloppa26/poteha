package ru.poteha.rent.support.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.poteha.rent.support.request.impl.jpa.SupportRequest;
import ru.poteha.rent.support.request.model.SupportRequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SupportRequestRepository extends JpaRepository<SupportRequest, UUID>, JpaSpecificationExecutor<SupportRequest> {

    @Modifying
    @Query("update SupportRequest sr set sr.status = :status, sr.modified = :currentTime where sr.modified >= :time")
    int updateAllStatuses(SupportRequestStatus status, LocalDateTime currentTime, LocalDateTime time);
}
