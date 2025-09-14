package ru.poteha.rent.support.request.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.poteha.rent.support.request.SupportRequestRepository;

import java.time.LocalDateTime;

import static ru.poteha.rent.support.request.model.SupportRequestStatus.CLOSED;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupportRequestJobs {

    private final SupportRequestRepository supportRequestRepository;

    @Scheduled(cron = "* * 1 * * *")
    public void closeRequest() {
        var count = supportRequestRepository.updateAllStatuses(CLOSED, LocalDateTime.now(), LocalDateTime.now().minusDays(3));
        log.info("Closing requests: {}", count);
    }
}
