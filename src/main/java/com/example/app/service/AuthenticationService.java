package com.example.app.service;

import com.example.app.api.model.authentication.AuthenticationRequest;
import com.example.app.api.model.authentication.AuthenticationResponse;
import com.example.app.api.model.authentication.RefreshRequest;
import com.example.app.api.model.authentication.RegisteredRequest;
import com.example.app.api.model.user.ChangePasswordRequest;
import com.example.app.constants.Constants;
import com.example.app.entities.AppUser;
import com.example.app.exception.RegisterExceptionBuilder;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisteredRequest request) throws AuthenticationException {
        RegisterExceptionBuilder exceptionBuilder = new RegisterExceptionBuilder();

        if (userService.existByUsername(request.getUsername())) {
            exceptionBuilder.addFieldError("username", "username.exists", "Username already exists");
        }

        if (!exceptionBuilder.isEmptyError()) {
            throw exceptionBuilder
                    .message("register credentials conflict")
                    .build();
        }

        AppUser user = AppUser.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        try {
            userService.saveUser(user);
            userService.addRoleToUser(user.getUsername(), Constants.ROLE_USER);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return generateToken(request.getUsername());
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        return generateToken(request.getUsername());
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws MalformedJwtException {

        final JWTService.DecodedToken refreshToken = jwtService.decodeToken(request.getRefreshToken());
        UserDetails user = userDetailsService.loadUserByUsername(refreshToken.getUsername());
        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(request.getRefreshToken())
                .build();
    }

    public void changePassword(ChangePasswordRequest request, String username) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        request.getOldPassword()
                )
        );

        userService.updateUserPassword(passwordEncoder.encode(request.getNewPassword()), username);
    }

    private AuthenticationResponse generateToken(String username) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }
}
