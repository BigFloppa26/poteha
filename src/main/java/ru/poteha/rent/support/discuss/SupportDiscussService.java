package ru.poteha.rent.support.discuss;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.poteha.rent.support.discuss.model.SupportDiscussCreate;
import ru.poteha.rent.support.discuss.model.SupportDiscussShort;

import java.util.UUID;

public interface SupportDiscussService {

    Page<SupportDiscussShort> search(UUID requestId, Pageable pageable, UUID userId);

    SupportDiscussShort sendMessage(UUID requestId, UUID userId, SupportDiscussCreate supportDiscussCreate);
}
