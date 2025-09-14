package ru.poteha.rent.notification.port.rest;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.poteha.rent.auth.AuthRepository;
import ru.poteha.rent.notification.NotificationRepository;
import ru.poteha.rent.notification.impl.jpa.Notification;
import ru.poteha.rent.user.UserRepository;
import ru.poteha.rent.user.UserService;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static ru.poteha.rent.notification.NotificationTestDataFactory.*;

@Slf4j
@WebMvcTest(
        controllers = {
                NotificationController.class
        },
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.poteha.rent.context.web.*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.poteha.rent.auth.*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.poteha.rent.notification.*"),
        }
)
public class NotificationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    EntityManager entityManager;

    @MockitoBean
    AuthRepository authRepository;

    @MockitoBean
    UserService userService;

    @MockitoBean
    UserDetailsService userDetailsService;

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    NotificationRepository notificationRepository;

    @MockitoBean
    JavaMailSender mailSender;

    @MockitoBean
    RedisTemplate redisTemplate;

    @Test
    @WithMockUser(username = "ebdd4bc5-a536-4a7a-a885-25b354156259", roles = {"ADMIN"})
    public void successfulSend() throws Exception {
        Mockito.when(notificationRepository.save(any(Notification.class))).thenReturn(notification());
        mockMvc.perform(
                MockMvcRequestBuilders.post("/notification")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(notificationCreate(true))
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser(username = "ebdd4bc5-a536-4a7a-a885-25b354156259", roles = {"ADMIN"})
    public void unsuccessfulSend() throws Exception {
        Mockito.when(notificationRepository.save(any())).thenReturn(notification());
        mockMvc.perform(
                MockMvcRequestBuilders.post("/notification")
                        .content(notificationCreate(false))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.jsonPath("$.detail").value("Ошибка валидации")
        );
    }

    @Test
    @WithMockUser(username = "ebdd4bc5-a536-4a7a-a885-25b354156259", roles = {"ADMIN"})
    public void successfulSearchNotificationById() throws Exception {
        Mockito.when(notificationRepository.findById(Mockito.any())).thenReturn(Optional.of(notification()));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/notification/" + UUID.randomUUID())
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser(username = "ebdd4bc5-a536-4a7a-a885-25b354156259", roles = {"ADMIN"})
    public void unsuccessfulSearchNotificationById() throws Exception {
        Mockito.when(notificationRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        mockMvc.perform(
                MockMvcRequestBuilders.get("/notification/" + UUID.randomUUID())
        ).andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.jsonPath("$.title").value("Not Found")
        );
    }
}