package ru.poteha.rent.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import ru.poteha.rent.user.UserMapper;
import ru.poteha.rent.user.impl.jpa.User;
import ru.poteha.rent.user.model.Role;
import ru.poteha.rent.user.model.UserCreate;
import ru.poteha.rent.user.model.UserDetail;
import ru.poteha.rent.user.model.UserShort;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class UserMappers implements UserMapper {

    User fromCreate(UserCreate userCreate) {
        var user = new User();
        user.setAuthorities(List.of(Role.RENTER));
        user.setPassword("{noop}" + userCreate.getPassword());
        user.setUsername(userCreate.getEmail());
        user.setEnabled(true);
        return user;
    }

    void update(User user, OAuth2User oAuth2User, boolean enabled) {
        user.setUsername(oAuth2User.getName());
        if (user.getAuthorities() == null) {
            user.setAuthorities(List.of(Role.RENTER));
            user.setEnabled(enabled);
        }
    }

    @Override
    public UserShort toShort(User user) {
        var userShort = new UserShort();
        userShort.setFirstName(user.getFirstName());
        userShort.setUsername(user.getUsername());
        userShort.setId(user.getId());
        return userShort;
    }

    @Override
    public UserDetail toDetail(User user) {
        var detail = new UserDetail();
        detail.setUsername(user.getUsername());
        detail.setId(user.getId());
        detail.setRoles(user.getAuthorities());
        return detail;
    }
}
