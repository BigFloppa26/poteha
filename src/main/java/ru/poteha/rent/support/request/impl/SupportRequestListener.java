package ru.poteha.rent.support.request.impl;

import jakarta.persistence.PostPersist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.poteha.rent.support.request.impl.jpa.SupportRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupportRequestListener {

    @PostPersist
    public void postPersist(SupportRequest supportRequest) {
        log.info("Post Persist Request: {}", supportRequest);
    }
}