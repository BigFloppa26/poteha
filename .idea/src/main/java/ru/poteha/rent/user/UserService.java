package ru.poteha.rent.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.poteha.rent.user.model.UserCreate;
import ru.poteha.rent.user.model.UserDetail;
import ru.poteha.rent.user.model.UserModify.*;
import ru.poteha.rent.user.model.UserSearch;
import ru.poteha.rent.user.model.UserShort;

import java.util.UUID;

public interface UserService {

    Page<UserShort> searchBy(UserSearch search, UUID userId, Pageable page);

    UserDetail getById(UUID id);

    UUID importOAuth2(OAuth2User oAuth2User);

    void changePassword(UUID userId, ChangePassword model);

    void deleteUser(UUID userId, boolean isAuth);

    void createVerify(UserCreate model);

    void create(UserCreate model);
}
