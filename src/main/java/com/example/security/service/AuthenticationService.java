package com.example.security.service;

import com.example.security.api.domain.authentication.AuthenticationRequest;
import com.example.security.api.domain.authentication.AuthenticationResponse;
import com.example.security.api.domain.authentication.RefreshRequest;
import com.example.security.api.domain.authentication.RegisteredRequest;
import com.example.security.constants.Constants;
import com.example.security.entities.AppUser;
import com.example.security.exception.RegisterException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.Map;

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

        if (userService.existByUsername(request.getUsername())) {
            BindingResult bindingResult = new BeanPropertyBindingResult(request, "registeredRequest");
            bindingResult.rejectValue("username", "username.exists", "Username already exists");
            throw new RegisterException("register credentials conflict", bindingResult);
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

    private AuthenticationResponse generateToken(String username) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }
}
