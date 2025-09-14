package ru.poteha.rent.support.request.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.poteha.rent.support.request.SupportRequestMapper;
import ru.poteha.rent.support.request.impl.jpa.SupportRequest;
import ru.poteha.rent.support.request.model.SupportRequestCreate;
import ru.poteha.rent.support.request.model.SupportRequestShort;
import ru.poteha.rent.support.request.model.SupportRequestStatus;
import ru.poteha.rent.user.impl.jpa.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupportRequestMappers implements SupportRequestMapper {

    private final EntityManager em;

    @Override
    public SupportRequest fromCreate(SupportRequestCreate create, UUID userId) {
        var supportRequest = new SupportRequest();
        supportRequest.setTitle(create.getTitle());
        supportRequest.setStatus(SupportRequestStatus.OPEN);
        supportRequest.setAuthor(em.getReference(User.class, userId));
        supportRequest.setDescription(create.getMessage());
        return supportRequest;
    }

    @Override
    public SupportRequestShort toShort(SupportRequest request) {
        var requestShort = new SupportRequestShort();
        requestShort.setTitle(request.getTitle());
        requestShort.setId(request.getId());
        requestShort.setStatus(request.getStatus());
        requestShort.setCreated(LocalDateTime.now());
        return requestShort;
    }
}
