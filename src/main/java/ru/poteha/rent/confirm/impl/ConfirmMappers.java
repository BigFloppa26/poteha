package ru.poteha.rent.confirm.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.poteha.rent.confirm.ConfirmMapper;
import ru.poteha.rent.confirm.impl.jpa.Confirm;
import ru.poteha.rent.confirm.model.ConfirmCreate;
import ru.poteha.rent.confirm.model.ConfirmShort;
import ru.poteha.rent.notification.model.NotificationCreate;

import java.util.HashMap;

import static org.springframework.util.DigestUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
class ConfirmMappers implements ConfirmMapper {
    private final static String CONFIRM_LINK = "%s?confirmId=%s&encrypt=%s";

    @Override
    public ConfirmShort toShort(Confirm confirm) {
        var confirmShort = new ConfirmShort();
        confirmShort.setConfirmed(confirm.isConfirmed());
        confirmShort.setUrl(confirm.getUrl());
        confirmShort.setId(confirm.getId());
        confirmShort.setAttempts(confirm.getAttempts());
        confirmShort.setExpiresIn(confirm.getExpiresIn());
        return confirmShort;
    }

    public Confirm fromCreate(ConfirmCreate model, boolean confirmed) {
        var confirm = new Confirm();
        confirm.setConfirmed(confirmed);
        confirm.setUrl(model.getUrl());
        return confirm;
    }

    void enrichNotification(NotificationCreate notificationCreate, Confirm confirm, long expiresMinutes) {
        if (notificationCreate.getAttributes() == null) {
            notificationCreate.setAttributes(new HashMap<>());
        }
        notificationCreate.getAttributes().put("confirmCode", confirm.getCode());
        notificationCreate.getAttributes().put("confirmExpires", expiresMinutes);
        notificationCreate.getAttributes().put("confirmLink", CONFIRM_LINK.formatted(
                confirm.getUrl(), confirm.getId(), md5DigestAsHex(confirm.getCode().getBytes())
        ));
    }
}