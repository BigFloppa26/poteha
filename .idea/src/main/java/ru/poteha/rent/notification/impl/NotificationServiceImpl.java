package ru.poteha.rent.notification.impl;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.poteha.rent.notification.NotificationRepository;
import ru.poteha.rent.notification.NotificationService;
import ru.poteha.rent.notification.impl.jpa.Notification;
import ru.poteha.rent.notification.impl.jpa.Notification_;
import ru.poteha.rent.notification.model.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final Map<UUID, SseEmitter> localEmitters = new ConcurrentHashMap<>();

    private final JavaMailSender mailSender;

    private final NotificationRepository notificationRepository;
    private final NotificationMappers notificationMapper;
    private final Configuration ftlFactory;

    @Override
    public Page<NotificationShort> searchBy(NotificationSearch search, Pageable page) {
        return notificationRepository.findBy((root, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            ifPresent(predicates, search.getHeader(), it -> builder.like(root.get(Notification_.HEADER), "%" + search.getHeader() + "%"));
            ifPresent(predicates, search.getRecipient(), it -> builder.like(root.get(Notification_.RECIPIENT), "%" + search.getRecipient() + "%"));
            ifPresent(predicates, search.getStatus(), it -> root.get(Notification_.STATUS).in(it));
            ifPresent(predicates, search.getType(), it -> root.get(Notification_.TYPE).in(it));

            return builder.and(predicates.toArray(Predicate[]::new));
        }, query -> query.page(page).map(notificationMapper::toShort));
    }

    @Override
    public NotificationDetail getDetailById(UUID id) {
        return notificationRepository.findById(id).map(notificationMapper::toDetail).orElseThrow(NotificationException.NotFound::new);
    }

    @Async
    @Override
    @Transactional
    public void sendAsync(NotificationCreate model, Consumer<UUID> consumer) {
        var notification = notificationMapper.fromCreate(model, this::render);
        try {
            switch (notification.getType()) {
                case EMAIL -> sendEmail(notification);
            }
        } catch (Exception e) {
            log.error("Ошибка при оправке - {}", e.getLocalizedMessage());
            notification.setStatus(NotificationStatus.ERROR);
        }
        consumer.accept(notificationRepository.save(notification).getId());
    }

    @Async
    @Override
    @Transactional
    public void sendAsync(NotificationCreate model) {
        this.sendAsync(model, notification -> log.trace("Notification: {}", notification));
    }

    //
    //  SSE
    //

    @Override
    public SseEmitter registerEmitter(UUID userId, SseEmitter emitter) {
        localEmitters.put(userId, emitter);
        emitter.onCompletion(() -> removeEmitter(userId));
        emitter.onTimeout(() -> removeEmitter(userId));
        return emitter;
    }

    @Override
    public void broadcastEmitters(BiConsumer<UUID, SseEmitter> consume) {
        localEmitters.forEach(consume);
    }

    @Override
    public void removeEmitter(UUID userId) {
        var emitter = localEmitters.remove(userId);
        if (emitter != null)
            emitter.complete();
    }

    //
    //  Провайдеры
    //

    void sendEmail(Notification notification) throws MessagingException {
        if (mailSender instanceof JavaMailSenderImpl jms && jms.getUsername() != null) {
            var message = mailSender.createMimeMessage();

            var helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setText(notification.getBody(), true);
            helper.setSubject(notification.getHeader());
            helper.setTo(notification.getRecipient());
            helper.setFrom(jms.getUsername());

            //mailSender.send(message);
        }
    }

    //
    //  Уитилитарные методы
    //

    String render(String template, Map<String, Object> attributes) {
        try (var sw = new StringWriter()) {
            ftlFactory.getTemplate(template).process(attributes, sw);
            return sw.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    <I> void ifPresent(List<Predicate> predicates, I input, Function<I, Predicate> function) {
        if (!ObjectUtils.isEmpty(input)) predicates.add(function.apply(input));
    }
}
