package com.example.security.service;

import com.example.security.api.model.AuthenticationRequest;
import com.example.security.api.model.AuthenticationResponse;
import com.example.security.api.model.RegisteredRequest;
import com.example.security.constants.Constants;
import com.example.security.entities.AppUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    public AuthenticationResponse register(RegisteredRequest request) {
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

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        return generateToken(request.getUsername());
    }

    public AuthenticationResponse refreshToken(AuthenticationResponse request) {

        try {
            final JWTService.DecodedToken refreshToken = jwtService.decodeToken(request.getRefreshToken());
            UserDetails user = userDetailsService.loadUserByUsername(refreshToken.getUsername());
            return AuthenticationResponse.builder()
                    .accessToken(jwtService.generateAccessToken(user))
                    .refreshToken(request.getRefreshToken())
                    .build();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }


        return AuthenticationResponse.builder().build();
    }

    private AuthenticationResponse generateToken(String username) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }
}
