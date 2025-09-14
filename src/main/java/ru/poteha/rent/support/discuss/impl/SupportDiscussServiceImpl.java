package ru.poteha.rent.support.discuss.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.poteha.rent.support.discuss.SupportDiscussRepository;
import ru.poteha.rent.support.discuss.SupportDiscussService;
import ru.poteha.rent.support.discuss.impl.jpa.SupportDiscuss_;
import ru.poteha.rent.support.discuss.model.SupportDiscussCreate;
import ru.poteha.rent.support.discuss.model.SupportDiscussException;
import ru.poteha.rent.support.discuss.model.SupportDiscussShort;
import ru.poteha.rent.support.request.impl.jpa.SupportRequest;
import ru.poteha.rent.support.request.impl.jpa.SupportRequest_;
import ru.poteha.rent.user.impl.jpa.User_;

import java.util.ArrayList;
import java.util.UUID;

import static ru.poteha.rent.support.request.model.SupportRequestStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportDiscussServiceImpl implements SupportDiscussService {

    private final SupportDiscussRepository supportDiscussRepository;
    private final SupportDiscussMappers supportDiscussMappers;
    private final EntityManager em;

    @Override
    public Page<SupportDiscussShort> search(UUID requestId, Pageable pageable, UUID userId) {
        return supportDiscussRepository.findBy((root, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            predicates.add(builder.equal(root.get(SupportDiscuss_.REQUEST).get(SupportRequest_.ID), requestId));
            predicates.add(builder.equal(root.get(SupportDiscuss_.FROM_USER).get(User_.ID), userId));

            return builder.and(predicates.toArray(Predicate[]::new));
        }, query -> query.page(pageable).map(supportDiscussMappers::toShort));
    }

    @Override
    public SupportDiscussShort sendMessage(UUID requestId, UUID userId, SupportDiscussCreate supportDiscussCreate) {
        if (!isActualDiscuss(requestId)) {
            throw new SupportDiscussException.IsActualDiscuss();
        }
        var supportDiscuss = supportDiscussMappers.fromCreate(supportDiscussCreate, userId);
        supportDiscuss.setRequest(em.getReference(SupportRequest.class, requestId));

        supportDiscussRepository.save(supportDiscuss);

        return supportDiscussMappers.toShort(supportDiscuss);
    }

    public boolean isActualDiscuss(UUID requestId) {
        return supportDiscussRepository.exists((root, query, builder) ->
                builder.and(
                        builder.equal(root.get(SupportDiscuss_.REQUEST).get(SupportRequest_.STATUS), OPEN),
                        builder.equal(root.get(SupportDiscuss_.REQUEST).get(SupportRequest_.ID), requestId)
                )
        );
    }
}