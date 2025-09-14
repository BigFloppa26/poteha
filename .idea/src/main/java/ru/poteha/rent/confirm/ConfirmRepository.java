package ru.poteha.rent.confirm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.poteha.rent.confirm.impl.jpa.Confirm;

import java.util.UUID;

public interface ConfirmRepository extends JpaRepository<Confirm, UUID>, JpaSpecificationExecutor<Confirm> {

    @Modifying
    @Query("update Confirm c set c.notification.id = :notificationId where c.id = :confirmId")
    void updateNotificationByConfirm(UUID notificationId, UUID confirmId);

}
