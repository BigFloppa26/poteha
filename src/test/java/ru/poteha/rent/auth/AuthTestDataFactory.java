package ru.poteha.rent.auth;

import ru.poteha.rent.auth.impl.jpa.Auth;
import ru.poteha.rent.user.UserTestDataFactory;

import java.util.UUID;

public class AuthTestDataFactory {

    public static Auth authAdmin() {
        var auth = new Auth();
        auth.setId(UUID.randomUUID());
        auth.setJti(UUID.randomUUID().toString());
        auth.setUser(UserTestDataFactory.adminUser());
        return auth;
    }

}
