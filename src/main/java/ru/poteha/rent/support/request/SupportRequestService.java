package ru.poteha.rent.support.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.poteha.rent.support.request.model.SupportRequestCreate;
import ru.poteha.rent.support.request.model.SupportRequestShort;

import java.util.UUID;

public interface SupportRequestService {

    SupportRequestShort createSupportRequest(SupportRequestCreate supportRequest, UUID userId);

    Page<SupportRequestShort> search(UUID userId, Pageable pageable);

    void closeRequest(UUID userId, UUID requestId);
}
