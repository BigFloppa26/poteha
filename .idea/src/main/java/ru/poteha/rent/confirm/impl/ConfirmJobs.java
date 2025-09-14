package ru.poteha.rent.confirm.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.poteha.rent.confirm.ConfirmRepository;
import ru.poteha.rent.confirm.impl.jpa.Confirm_;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmJobs {

    private final ConfirmRepository confirmRepository;

    @Scheduled(cron = "@midnight")
    public void cleanup() {
        confirmRepository.delete((root, query, builder) -> {
            log.debug("Удаление записей с истекшим скором подтверждения");
            return builder.lt(root.get(Confirm_.EXPIRES_IN), System.currentTimeMillis() / 1000);
        });
    }
}

