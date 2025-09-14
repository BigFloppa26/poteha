package ru.poteha.rent.auth.port.rest;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.poteha.rent.auth.AuthRepository;
import ru.poteha.rent.auth.impl.jpa.Auth;
import ru.poteha.rent.user.UserService;

import static ru.poteha.rent.auth.AuthTestDataFactory.authAdmin;

@Slf4j
@WebMvcTest(
        controllers = {
                AuthController.class
        },
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.poteha.rent.auth.*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.poteha.rent.context.web.*"),
        }
)
class AuthControllerTest {

    @MockitoBean
    UserDetailsService userDetailsService;

    @MockitoBean
    AuthRepository authRepository;

    @MockitoBean
    EntityManager entityManager;

    @MockitoBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Mockito.when(userDetailsService.loadUserByUsername("admin")).thenReturn(authAdmin().getUser());
        Mockito.when(authRepository.save(Mockito.any(Auth.class))).thenReturn(authAdmin());
    }

    @Test
    void successfulLoginForm() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .formField("username", "admin")
                        .formField("password", "admin")
        ).andExpectAll(
                MockMvcResultMatchers.status().is2xxSuccessful()
        );
    }

    @Test
    void unsuccessfulLoginForm() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .formField("username", "admin")
                        .formField("password", "incorrect")
        ).andExpectAll(
                MockMvcResultMatchers.status().is4xxClientError(),
                MockMvcResultMatchers.jsonPath("$.title").value("Unauthorized")
        );
    }
}