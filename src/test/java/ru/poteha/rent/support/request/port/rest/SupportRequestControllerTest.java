package ru.poteha.rent.support.request.port.rest;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.poteha.rent.auth.AuthRepository;
import ru.poteha.rent.support.discuss.SupportDiscussRepository;
import ru.poteha.rent.support.request.SupportRequestRepository;
import ru.poteha.rent.user.UserService;

import static ru.poteha.rent.support.request.RequestTestDataFactory.*;
import static ru.poteha.rent.support.request.model.SupportRequestStatus.OPEN;

@Slf4j
@WebMvcTest(
        controllers = {
                SupportRequestController.class
        },
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.poteha.rent.context.web.*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.poteha.rent.auth.*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.poteha.rent.support.*")
        }
)
public class SupportRequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SupportRequestRepository supportRequestRepository;

    @MockitoBean
    SupportDiscussRepository supportDiscussRepository;

    @MockitoBean
    EntityManager entityManager;

    @MockitoBean
    AuthRepository authRepository;

    @MockitoBean
    UserService userService;

    @Test
    @WithMockUser(username = "c6a32f28-d9f3-4604-a1dc-f4ac1c89c6cc", authorities = "RENTER")
    public void successfulCreateRequest() throws Exception {
        Mockito.when(supportRequestRepository.save(Mockito.any())).thenReturn(supportRequest());
        mockMvc.perform(
                MockMvcRequestBuilders.post("/support")
                        .content(bytesCorrectSupportRequestCreate(true))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.status").value(OPEN.toString()));
    }

    @Test
    @WithMockUser(username = "c6a32f28-d9f3-4604-a1dc-f4ac1c89c6cc", authorities = "RENTER")
    public void unsuccessfulCreateRequest() throws Exception {
        Mockito.when(supportRequestRepository.saveAndFlush(Mockito.any())).thenReturn(supportRequest());
        mockMvc.perform(
                MockMvcRequestBuilders.post("/support")
                        .content(bytesCorrectSupportRequestCreate(false))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(MockMvcResultMatchers.status().is4xxClientError(),
                MockMvcResultMatchers.jsonPath("$.detail").value("Ошибка валидации"));
    }
}
