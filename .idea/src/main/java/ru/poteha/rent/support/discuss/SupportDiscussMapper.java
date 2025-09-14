package ru.poteha.rent.support.discuss;

import ru.poteha.rent.support.discuss.impl.jpa.SupportDiscuss;
import ru.poteha.rent.support.discuss.model.SupportDiscussCreate;
import ru.poteha.rent.support.discuss.model.SupportDiscussShort;

import java.util.UUID;

public interface SupportDiscussMapper {

    SupportDiscussShort toShort(SupportDiscuss supportDiscuss);

    SupportDiscuss fromCreate(SupportDiscussCreate supportDiscussCreate, UUID userId);
}
