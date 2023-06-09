package com.example.app;

import com.example.app.entities.AppUser;
import com.example.app.entities.Role;
import com.example.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class Application {
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.saveRole(new Role(null, "ROLE_USER"));
            userService.saveRole(new Role(null, "ROLE_ADMIN"));

            userService.saveUser(AppUser.builder()
                    .username("string")
                    .password(passwordEncoder.encode("string"))
                    .build()
            );
            userService.addRoleToUser("string", "ROLE_USER");
            userService.addRoleToUser("string", "ROLE_ADMIN");

            userService.saveUser(AppUser.builder()
                    .username("user")
                    .password(passwordEncoder.encode("string"))
                    .build()
            );
            userService.addRoleToUser("user", "ROLE_USER");
        };
    }

}
