package ru.poteha.rent.support.request.impl;

import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.poteha.rent.support.request.SupportRequestRepository;
import ru.poteha.rent.support.request.SupportRequestService;
import ru.poteha.rent.support.request.impl.jpa.SupportRequest_;
import ru.poteha.rent.support.request.model.SupportRequestCreate;
import ru.poteha.rent.support.request.model.SupportRequestException;
import ru.poteha.rent.support.request.model.SupportRequestShort;
import ru.poteha.rent.support.request.model.SupportRequestStatus;
import ru.poteha.rent.user.impl.jpa.User_;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportRequestServiceImpl implements SupportRequestService {

    private final SupportRequestMappers requestMapper;
    private final SupportRequestRepository requestRepository;

    @Override
    public Page<SupportRequestShort> search(UUID userId, Pageable pageable) {
        return requestRepository.findAll((root, query, builder) -> {
            root.fetch(SupportRequest_.AUTHOR, JoinType.LEFT);
            return builder.and(builder.equal(root.get(SupportRequest_.AUTHOR).get(User_.ID), userId));
        }, pageable).map(requestMapper::toShort);
    }

    @Override
    public SupportRequestShort createSupportRequest(SupportRequestCreate request, UUID userId) {
        return requestMapper.toShort(requestRepository.save(requestMapper.fromCreate(request, userId)));
    }

    @Override
    public void closeRequest(UUID userId, UUID requestId) {
        var request = requestRepository.findById(requestId).orElseThrow(SupportRequestException.NotFound::new);
        request.setStatus(SupportRequestStatus.CLOSED);
        requestRepository.save(request);
    }
}
