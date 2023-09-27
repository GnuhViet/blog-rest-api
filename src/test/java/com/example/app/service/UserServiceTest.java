package com.example.app.service;

import com.example.app.constants.Constants;
import com.example.app.dtos.appuser.DetailsAppUserDTO;
import com.example.app.entities.AppUser;
import com.example.app.entities.Role;
import com.example.app.exception.user.UserAlreadyHaveRoleException;
import com.example.app.repository.RoleRepository;
import com.example.app.repository.UserRepository;
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
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.*;

import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Spy private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private UserService userService;

    private DetailsAppUserDTO detailsAppUserDTO;
    private List<Role> roles;
    private AppUser appUserAdmin;
    private AppUser appUserNormal;

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

        detailsAppUserDTO = DetailsAppUserDTO.builder()
                .id(UUID.randomUUID().toString())
                .username("string")
                .fullName("string")
                .roles(roles)
                .build();

        appUserAdmin = AppUser.builder()
                .username("string")
                .password("string")
                .fullName("full name")
                .roles(roles)
                .build();

        appUserNormal = AppUser.builder()
                .username("string")
                .password("string")
                .fullName("full name")
                .roles(new ArrayList<>(List.of(roles.get(1))))
                .build();

        when(roleRepository.findByName(Constants.ROLE_ADMIN)).thenReturn(Optional.of(roles.get(0)));
        when(roleRepository.findByName(Constants.ROLE_USER)).thenReturn(Optional.of(roles.get(1)));
    }

    @Test
    public void AddRoleToUser_AddExistRole_ThrowException() {
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(appUserAdmin));

        Assertions.assertThrows(UserAlreadyHaveRoleException.class,
                () -> userService.addRoleToUser(appUserAdmin.getUsername(), Constants.ROLE_ADMIN)
        );
    }

    @Test
    public void AddRoleToUser_AddNewRole() {
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(appUserNormal));

        DetailsAppUserDTO result = userService.addRoleToUser("string", Constants.ROLE_ADMIN);

        Assertions.assertEquals(result.getRoles().size(), 2);
    }

    @Test
    public void AddRoleToUser_AddNewRoleToEmptyRoleUser() {
        appUserNormal.setRoles(null);
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(appUserNormal));

        DetailsAppUserDTO result = userService.addRoleToUser("string", Constants.ROLE_ADMIN);

        Assertions.assertNotNull(result.getRoles());
    }
}
