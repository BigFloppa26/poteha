package ru.poteha.rent.auth;

import ru.poteha.rent.auth.impl.jpa.Auth;
import ru.poteha.rent.auth.model.AuthShort;

public interface AuthMapper {
    AuthShort toShort(Auth auth);
}
