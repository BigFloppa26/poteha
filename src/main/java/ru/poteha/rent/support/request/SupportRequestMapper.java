package ru.poteha.rent.support.request;

import ru.poteha.rent.support.request.impl.jpa.SupportRequest;
import ru.poteha.rent.support.request.model.SupportRequestCreate;
import ru.poteha.rent.support.request.model.SupportRequestShort;

import java.util.UUID;

public interface SupportRequestMapper {

    SupportRequest fromCreate(SupportRequestCreate request, UUID userId);

    SupportRequestShort toShort(SupportRequest request);
}
