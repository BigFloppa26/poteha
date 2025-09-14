package ru.poteha.rent.user;

import ru.poteha.rent.user.impl.jpa.User;
import ru.poteha.rent.user.model.Role;

import java.util.List;
import java.util.UUID;

public class UserTestDataFactory {
    public final static UUID ADMIN_ID = UUID.fromString("ac2af47f-139e-4b46-b0ba-2a135fd8181a");
    public final static UUID OWNER_ID = UUID.fromString("7aff050e-06c2-462a-aec1-907b4b3c26dd");
    public final static UUID RENT_ID = UUID.fromString("c6a32f28-d9f3-4604-a1dc-f4ac1c89c6cc");

    public final static String ADMIN_NAME = "admin";
    public final static String OWNER_NAME = "owner";
    public final static String RENT_NAME = "rent";

    public static User adminUser() {
        var target = new User();
        target.setUsername(ADMIN_NAME);
        target.setPassword("{noop}admin");
        target.setEnabled(true);
        target.setId(ADMIN_ID);
        target.setAuthorities(List.of(Role.ADMIN));
        return target;
    }

    public static User rentUser() {
        var target = new User();
        target.setUsername(RENT_NAME);
        target.setPassword("rent");
        target.setEnabled(true);
        target.setId(RENT_ID);
        target.setAuthorities(List.of(Role.RENTER));
        return target;
    }

    public static User ownerUser() {
        var target = new User();
        target.setUsername(OWNER_NAME);
        target.setPassword("owner");
        target.setEnabled(true);
        target.setId(OWNER_ID);
        target.setAuthorities(List.of(Role.RENTER, Role.OWNER));
        return target;
    }
}
