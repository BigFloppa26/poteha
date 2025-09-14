package ru.poteha.rent.user.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import ru.poteha.rent.user.UserRepository;
import ru.poteha.rent.user.UserService;
import ru.poteha.rent.user.impl.jpa.User;
import ru.poteha.rent.user.impl.jpa.User_;
import ru.poteha.rent.user.model.*;
import ru.poteha.rent.user.model.UserModify.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final UserMappers userMapper;

    @Override
    public void createVerify(UserCreate model) {
        if (userRepository.existsByUsername(model.getEmail()))
            throw new UserException.AlreadyExists();
    }

    @Override
    @Transactional
    public void create(UserCreate model) {
        createVerify(model);
        userRepository.save(userMapper.fromCreate(model));
    }

    @Override
    public Page<UserShort> searchBy(UserSearch search, UUID userId, Pageable page) {
        return userRepository.findBy((root, query, builder) -> {
            var predicates = new ArrayList<Predicate>();
            //  where ... and lower(firstName) like '%?%'
            ifPresent(predicates, search.getFirstName(), it -> {
                return builder.like(builder.lower(root.get(User_.FIRST_NAME)), '%' + it.toLowerCase() + '%');
            });
            return builder.and(predicates.toArray(Predicate[]::new));
        }, query -> query.page(page).map(userMapper::toShort));
    }

    @Override
    public UserDetail getById(UUID id) {
        return userRepository.findById(id).map(userMapper::toDetail).orElseThrow(UserException.NotFound::new);
    }

    @Override
    @Transactional
    public UUID importOAuth2(OAuth2User oAuth2User) {
        var user = userRepository.findByUsername(oAuth2User.getName()).orElseGet(User::new);
        userMapper.update(user, oAuth2User, true);
        return userRepository.saveAndFlush(user).getId();
    }

    @Override
    public void changePassword(UUID userId, ChangePassword model) {
        var user = userRepository.findById(userId).orElseThrow(UserException.NotFound::new);
        user.setPassword(model.getNewPassword());
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID userId, boolean isAuth) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new UserException.NotFound();
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    <I> void ifPresent(List<Predicate> predicates, I input, Function<I, Predicate> function) {
        if (!ObjectUtils.isEmpty(input)) predicates.add(function.apply(input));
    }
}
