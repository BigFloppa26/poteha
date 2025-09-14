package ru.poteha.rent.auth;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.poteha.rent.auth.impl.jpa.Auth;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<Auth, UUID> {
    @Query("select a from Auth a join fetch a.user where a.jti = :jti and a.user.id = :userId and a.user.enabled")
    Optional<Auth> findByJtiAndUserId(String jti, UUID userId);

    @Query("select a from Auth a join fetch a.user u where u.id = :userId")
    Page<Auth> findByUserId(UUID userId, Pageable page);

    @Modifying
    @Query("delete from Auth a where a.jti = :jti")
    void removeByJti(String jti);
}
