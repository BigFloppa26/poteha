package ru.poteha.rent.user.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    RENTER,
    OWNER,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
