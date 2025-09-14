package ru.poteha.rent.confirm.port.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.poteha.rent.auth.AuthRepository;
import ru.poteha.rent.confirm.ConfirmRepository;
import ru.poteha.rent.confirm.impl.jpa.Confirm;
import ru.poteha.rent.itemstatic.feature.FeatureToggle;
import ru.poteha.rent.notification.NotificationService;
import ru.poteha.rent.user.UserRepository;
import ru.poteha.rent.user.UserService;

import java.util.Optional;
import java.util.UUID;

import static ru.poteha.rent.confirm.ConfirmTestDataFactory.*;

@Slf4j
@WebMvcTest(
        controllers = {
                ConfirmController.class
        },
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.poteha.rent.confirm.*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.poteha.rent.context.web.*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.poteha.rent.auth.*")
        }
)
@RequiredArgsConstructor
public class ConfirmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private ConfirmRepository confirmRepository;

    @MockitoBean
    EntityManager entityManager;

    @MockitoBean
    NotificationService notificationService;

    @MockitoBean
    FeatureToggle featureToggle;

    @MockitoBean
    AuthRepository authRepository;

    @MockitoBean
    UserService userService;

    @MockitoBean
    UserDetailsService userDetailsService;

    @MockitoBean
    UserRepository userRepository;

    @Test
    public void successfulConsume() throws Exception {
        Mockito.when(confirmRepository.save(Mockito.any(Confirm.class))).thenReturn(confirm(true));
        Mockito.when(confirmRepository.findById(CONFIRM_ID)).thenReturn(Optional.of(confirm(true)));
        mockMvc.perform(
                MockMvcRequestBuilders.put("/confirm/consume")
                        .content(objectMapper.writeValueAsBytes(correctConfirmCode()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.confirmed").value("true")
        );
    }

    @Test
    public void unsuccessfulConsume() throws Exception {
        Mockito.when(confirmRepository.findById(CONFIRM_ID)).thenReturn(Optional.of(confirm(true)));
        Mockito.when(confirmRepository.save(confirm(true))).thenReturn(confirm(true));
        mockMvc.perform(
                MockMvcRequestBuilders.put("/confirm/consume")
                        .content(objectMapper.writeValueAsBytes(incorrectConfirmCode()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(
                MockMvcResultMatchers.status().is4xxClientError(),
                MockMvcResultMatchers.jsonPath("$.detail").value("Неверный код подтверждения")
        );
    }

    @Test
    public void successfulResend() throws Exception {
        Mockito.when(confirmRepository.findById(CONFIRM_ID)).thenReturn(Optional.of(confirm(false)));
        mockMvc.perform(
                MockMvcRequestBuilders.put("/confirm/resend").
                        queryParam("confirmId", CONFIRM_ID.toString())
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.confirmed").value("false"),
                MockMvcResultMatchers.jsonPath("$.attempts").value(3)
        );

    }

    @Test
    public void unsuccessfulResend() throws Exception {
        Mockito.when(confirmRepository.findById(CONFIRM_ID)).thenReturn(Optional.of(confirm(false)));
        mockMvc.perform(
                MockMvcRequestBuilders.put("/confirm/resend")
                        .queryParam("confirmId", UUID.randomUUID().toString())
        ).andExpectAll(
                MockMvcResultMatchers.status().is4xxClientError(),
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
        );
    }
}