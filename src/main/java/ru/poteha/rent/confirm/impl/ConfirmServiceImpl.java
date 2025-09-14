package ru.poteha.rent.confirm.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.poteha.rent.confirm.ConfirmRepository;
import ru.poteha.rent.confirm.ConfirmService;
import ru.poteha.rent.confirm.impl.jpa.Confirm;
import ru.poteha.rent.confirm.model.*;
import ru.poteha.rent.itemstatic.feature.FeatureToggle;
import ru.poteha.rent.notification.NotificationService;
import ru.poteha.rent.notification.model.NotificationCreate;


import java.time.*;
import java.util.UUID;
import java.util.function.Supplier;

import static org.springframework.util.DigestUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmServiceImpl implements ConfirmService {
    private final NotificationService notificationService;
    private final ConfirmRepository confirmRepository;
    private final ConfirmMappers confirmMapper;
    private final FeatureToggle featureToggle;
    private final ObjectMapper objectMapper;

    @Value("${spring.confirm.expires}")
    protected Duration expires;
    @Value("${spring.confirm.attempts}")
    protected int attempts;

    @Override
    public <T> T getPayload(UUID id, String md5Code, Class<T> cls) {
        var confirm = confirmRepository.findById(id).orElseThrow(ConfirmException.NotConfirm::new);
        verifyForPayload(confirm, md5Code);
        var payload = objectMapper.convertValue(confirm.getPayload(), cls);
        confirmRepository.delete(confirm);
        return payload;
    }

    @Override
    public <T> T getPayload(ConfirmCypher code, Class<T> cls) {
        return getPayload(code.getConfirmId(), code.getEncrypt(), cls);
    }

    @Override
    public ConfirmShort resend(UUID id) {
        var confirm = confirmRepository.findById(id).orElseThrow(ConfirmException.NotConfirm::new);
        verifyForResend(confirm);

        confirm.setAttempts(attempts);
        confirm.setCode(createCode());

        return flushAndSend(true, confirm, NotificationCreate.builder()
                .recipient(confirm.getNotification().getRecipient())
                .header(confirm.getNotification().getHeader())
                .type(confirm.getNotification().getType())
                .template("confirmResend.ftl")
                .build());
    }

    @Override
    public ConfirmShort consume(ConfirmCode model) {
        return consume(model.getId(), model.getCode());
    }

    @Override
    public ConfirmShort consume(UUID id, String code) {
        var confirm = confirmRepository.findById(id).orElseThrow(() -> new AccessDeniedException(""));
        verifyForConsume(confirm, code);
        confirm.setConfirmed(true);
        return confirmMapper.toShort(confirmRepository.save(confirm));
    }

    @Override
    public ConfirmShort generateAndSend(String feature, Supplier<ConfirmCreate> supplier) {
        return generateAndSend(featureToggle.isEnabled(feature), supplier);
    }

    @Override
    public ConfirmShort generateAndSend(boolean feature, Supplier<ConfirmCreate> supplier) {
        return generateAndSend(feature, supplier.get());
    }

    ConfirmShort generateAndSend(boolean feature, ConfirmCreate model) {
        var confirm = confirmMapper.fromCreate(model, !feature);
        confirm.setPayload(objectMapper.convertValue(model.getPayload(), JsonNode.class));
        confirm.setAttempts(attempts);
        confirm.setCode(createCode());
        confirm.setExpiresIn((System.currentTimeMillis() + expires.toMillis()) / 1000);
        return flushAndSend(feature, confirm, model.getNotification());
    }

    ConfirmShort flushAndSend(boolean feature, Confirm confirm, NotificationCreate notification) {
        confirmRepository.saveAndFlush(confirm);
        if (feature) {
            confirmMapper.enrichNotification(notification, confirm, expires.toMinutes());
            notificationService.sendAsync(notification, notifyId -> {
                log.debug("Отправлено уведомление {} для подтверждения {}", notifyId, confirm.getId());
                confirmRepository.updateNotificationByConfirm(notifyId, confirm.getId());
            });
        }
        return confirmMapper.toShort(confirm);
    }

    void verifyForConsume(Confirm confirm, String code) {
        if (confirm.getModified().plus(expires).isBefore(LocalDateTime.now()))
            throw new ConfirmException.ExpiredAt();

        if (!confirm.getCode().equals(code)) {
            verifyForResend(confirm);
            confirm.setAttempts((confirm.getAttempts() - 1));
            confirmRepository.saveAndFlush(confirm);
            throw new ConfirmException.IllegalCode();
        }
    }

    void verifyForPayload(Confirm confirm, String md5Code) {
        if (md5Code != null && !matches(confirm.getCode(), md5Code))
            throw new ConfirmException.IllegalCode();
        if (md5Code == null && !confirm.isConfirmed())
            throw new ConfirmException.NotConfirm();
    }

    boolean matches(String code, String md5Code) {
        return md5DigestAsHex(code.getBytes()).equals(md5Code);
    }

    void verifyForResend(Confirm confirm) {
        if (confirm.getAttempts() <= 0)
            throw new ConfirmException.NoAttempts();
    }


    String createCode() {
        return "11111";
    }
}