package ru.poteha.rent.user.port.rest;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.poteha.rent.auth.AuthRepository;
import ru.poteha.rent.user.*;
import ru.poteha.rent.confirm.ConfirmService;
import ru.poteha.rent.itemstatic.feature.FeatureToggle;

import java.util.*;

import static ru.poteha.rent.user.UserTestDataFactory.*;

@Slf4j
@WebMvcTest(
        controllers = {
                UserController.class
        },
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.poteha.rent.context.web.*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.poteha.rent.auth.*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.poteha.rent.user.*")
        }
)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    EntityManager entityManager;

    @MockitoBean
    ConfirmService confirmService;

    @MockitoBean
    AuthRepository authRepository;

    @MockitoBean
    FeatureToggle featureToggle;

    @Test
    @WithMockUser(username = "c6a32f28-d9f3-4604-a1dc-f4ac1c89c6cc", authorities = {"RENTER"})
    public void successfulUserById() throws Exception {
        Mockito.when(userRepository.findById(RENT_ID)).thenReturn(Optional.of(rentUser()));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/user/" + RENT_ID)
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.id").value(RENT_ID.toString())
        );
    }

    @Test
    @WithMockUser(username = "c6a32f28-d9f3-4604-a1dc-f4ac1c89c6cc", authorities = {"RENTER"})
    public void unsuccessfulUserById() throws Exception {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        mockMvc.perform(
                MockMvcRequestBuilders.get("/user/" + UUID.randomUUID())
        ).andExpectAll(
                MockMvcResultMatchers.status().is4xxClientError(),
                MockMvcResultMatchers.jsonPath("$.title").value("Not Found")
        );
    }

// TODO доделать successfulUserInfo

    @Test
    public void unsuccessfulUserInfo() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/user/info")
        ).andExpectAll(
                MockMvcResultMatchers.status().is4xxClientError(),
                MockMvcResultMatchers.jsonPath("$.title").value("Unauthorized")
        );
    }

    @Test
    @WithMockUser(username = "ebdd4bc5-a536-4a7a-a885-25b354156259", authorities = {"ADMIN"})
    public void successfulUserDelete() throws Exception {
        Mockito.when(userRepository.findById(ADMIN_ID)).thenReturn(Optional.of(adminUser()));
        Mockito.doNothing().when(userRepository).deleteById(ADMIN_ID);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/user/" + ADMIN_ID)
        ).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "ebdd4bc5-a536-4a7a-a885-25b354152222", authorities = "ADMIN")
    public void unsuccessfulUserDelete() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/user/" + UUID.randomUUID())
        ).andExpectAll(
                MockMvcResultMatchers.status().is4xxClientError(),
                MockMvcResultMatchers.jsonPath("$.title").value("Not Found")
        );
    }
}