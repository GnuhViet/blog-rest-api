package com.example.app.service;

import com.example.app.api.model.authentication.RegisteredRequest;
import com.example.app.constants.Constants;
import com.example.app.dto.appuser.DetailsAppUserDTO;
import com.example.app.entities.AppUser;
import com.example.app.entities.Role;
import com.example.app.exception.authentication.RegisterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AuthenticationServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Spy
    private JWTService jwtService = new JWTService();

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisteredRequest registeredRequest;
    private DetailsAppUserDTO detailsAppUserDTO;
    private List<Role> roles;
    private AppUser appUser;

    @BeforeEach
    public void init() {

        roles = List.of(
                Role.builder()
                        .id(UUID.randomUUID().toString())
                        .name(Constants.ROLE_ADMIN)
                        .build(),
                Role.builder()
                        .id(UUID.randomUUID().toString())
                        .name(Constants.ROLE_USER)
                        .build()
        );


        registeredRequest = RegisteredRequest.builder()
                .username("string")
                .password("string")
                .fullName("full name")
                .build();

        detailsAppUserDTO = DetailsAppUserDTO.builder()
                .id(UUID.randomUUID().toString())
                .username("string")
                .fullName("string")
                .roles(roles)
                .build();

        appUser = AppUser.builder()
                .fullName(registeredRequest.getFullName())
                .username(registeredRequest.getUsername())
                .password(registeredRequest.getPassword()) // NOT ENCODED
                .build();
    }


    @Test
    public void Register_AlreadyExitsUser_ThrowException() {
        when(userService.existByUsername(registeredRequest.getUsername())).thenReturn(true);
        Assertions.assertThrows(RegisterException.class,
                () -> authenticationService.register(registeredRequest)
        );
    }

    @Test
    public void Register_NewUser_ReturnJWT() {
        when(userService.existByUsername(registeredRequest.getUsername())).thenReturn(false);
        when(userService.saveUser(appUser)).thenReturn(detailsAppUserDTO);
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn(registeredRequest.getPassword());
        doNothing().when(userService).addRoleToUser(Mockito.anyString(), Mockito.anyString());


        String token = authenticationService.register(registeredRequest).getAccessToken();

        log.info(token);

        org.assertj.core.api.Assertions
                .assertThat(token)
                .isNotNull();
    }
}
