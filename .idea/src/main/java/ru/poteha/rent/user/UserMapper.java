package ru.poteha.rent.user;

import ru.poteha.rent.user.impl.jpa.User;
import ru.poteha.rent.user.model.UserDetail;
import ru.poteha.rent.user.model.UserShort;

public interface UserMapper {
    UserShort toShort(User user);

    UserDetail toDetail(User user);
}
