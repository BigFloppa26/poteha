package ru.poteha.rent.confirm;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import ru.poteha.rent.confirm.impl.jpa.Confirm;
import ru.poteha.rent.confirm.model.ConfirmCode;

import java.time.LocalDateTime;
import java.util.UUID;

import static ru.poteha.rent.notification.NotificationTestDataFactory.notification;

@RequiredArgsConstructor
public class ConfirmTestDataFactory {

    public static final UUID CONFIRM_ID = UUID.fromString("6324b67f-2e67-48d4-b157-9c99484e1058");

    public static Confirm confirm(boolean confirmed) {
        var confirm = new Confirm();
        confirm.setConfirmed(confirmed);
        confirm.setId(CONFIRM_ID);
        confirm.setCode("11111");
        confirm.setAttempts(3);
        confirm.setNotification(notification());
        confirm.setModified(LocalDateTime.now());
        return confirm;
    }

    public static ConfirmCode correctConfirmCode() throws JsonProcessingException {
        var correctConfirmCode = new ConfirmCode();
        correctConfirmCode.setId(CONFIRM_ID);
        correctConfirmCode.setCode("11111");
        return correctConfirmCode;
    }

    public static ConfirmCode incorrectConfirmCode() throws JsonProcessingException {
        var incorrectConfirmCode = new ConfirmCode();
        incorrectConfirmCode.setId(CONFIRM_ID);
        incorrectConfirmCode.setCode("22222");
        return incorrectConfirmCode;
    }
}