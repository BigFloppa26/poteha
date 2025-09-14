package ru.poteha.rent.support.discuss.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.poteha.rent.support.discuss.SupportDiscussMapper;
import ru.poteha.rent.support.discuss.impl.jpa.SupportDiscuss;
import ru.poteha.rent.support.discuss.model.SupportDiscussCreate;
import ru.poteha.rent.support.discuss.model.SupportDiscussShort;
import ru.poteha.rent.user.impl.jpa.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupportDiscussMappers implements SupportDiscussMapper {

    private final EntityManager em;

    @Override
    public SupportDiscussShort toShort(SupportDiscuss supportDiscuss) {
        var discussShort = new SupportDiscussShort();
        discussShort.setRead(supportDiscuss.isRead());
        discussShort.setId(supportDiscuss.getId());
        discussShort.setMessage(supportDiscuss.getMessage());
        discussShort.setCreated(supportDiscuss.getCreated());
        discussShort.setFromUserId(supportDiscuss.getFromUser().getId());
        discussShort.setFromUsername(supportDiscuss.getFromUser().getUsername());
        return discussShort;
    }

    @Override
    public SupportDiscuss fromCreate(SupportDiscussCreate supportDiscussShort, UUID userId) {
        var supportDiscuss = new SupportDiscuss();
        supportDiscuss.setCreated(LocalDateTime.now());
        supportDiscuss.setRead(false);
        supportDiscuss.setMessage(supportDiscussShort.getMessage());
        supportDiscuss.setFromUser(em.getReference(User.class, userId));
        return supportDiscuss;
    }
}
