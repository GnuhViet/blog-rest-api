package com.example.app.service;

import com.example.app.api.model.authentication.AuthenticationRequest;
import com.example.app.api.model.authentication.AuthenticationResponse;
import com.example.app.api.model.authentication.RegisteredRequest;
import com.example.app.constants.Constants;
import com.example.app.dtos.appuser.DetailsAppUserDTO;
import com.example.app.entities.AppUser;
import com.example.app.entities.Role;
import com.example.app.exception.authentication.RegisterException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AuthenticationServiceTest {

    @Mock private UserService userService;
    @Mock private AuthenticationManager authenticationManager;
    @Spy private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Spy private final JWTService jwtService = new JWTService(
            "413F4428472B4B6250645367566B5970337336763979244226452948404D6351",
            3600000,
            86400000,
            "my-blog"
    );

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

        when(userService.getUserid(Mockito.any())).thenReturn(detailsAppUserDTO.getId());
        when(userService.getUserDto(Mockito.anyString(), Mockito.any())).thenReturn(detailsAppUserDTO);
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
        doNothing().when(userService).addRoleToUser(Mockito.anyString(), Mockito.anyString());


        AuthenticationResponse resp = authenticationService.register(registeredRequest);

        log.info("\nJWT tokens: " + resp);

        org.assertj.core.api.Assertions
                .assertThat(resp.getAccessToken())
                .isNotNull();

        org.assertj.core.api.Assertions
                .assertThat(resp.getRefreshToken())
                .isNotNull();
    }

    @Test
    public void Authenticate_ReturnJWT() {
        when(authenticationManager.authenticate(Mockito.any())).thenReturn(null);
        AuthenticationRequest request = AuthenticationRequest.builder().username("string").password("string").build();


        AuthenticationResponse resp = authenticationService.authenticate(request);

        log.info("\nJWT tokens: " + resp);

        org.assertj.core.api.Assertions
                .assertThat(resp.getAccessToken())
                .isNotNull();

        org.assertj.core.api.Assertions
                .assertThat(resp.getRefreshToken())
                .isNotNull();
    }
}
