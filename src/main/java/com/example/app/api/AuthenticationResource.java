package com.example.app.api;

import com.example.app.api.model.authentication.AuthenticationRequest;
import com.example.app.api.model.authentication.AuthenticationResponse;
import com.example.app.api.model.authentication.RefreshRequest;
import com.example.app.api.model.authentication.RegisteredRequest;
import com.example.app.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationResource {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "User register, Role: All")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisteredRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    @Operation(summary = "login , Role: All")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "refresh jwt token , Role: All")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }
}
