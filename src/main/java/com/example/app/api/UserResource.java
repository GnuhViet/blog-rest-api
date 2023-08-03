package com.example.app.api;

import com.example.app.api.model.user.ChangePasswordRequest;
import com.example.app.api.model.user.UserProfileRequest;
import com.example.app.constants.Constants;
import com.example.app.dto.appuser.DetailsAppUserDTO;
import com.example.app.service.AuthenticationService;
import com.example.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping("/profile")
    @Operation(summary = "User profile, Role: All", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<DetailsAppUserDTO> getProfile(Principal principal) {
        return ResponseEntity.ok(
                userService.getUserDto(principal.getName(), DetailsAppUserDTO.class)
        );
    }

    @PostMapping("/profile")
    @Operation(summary = "User profile, Role: All", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<DetailsAppUserDTO> editProfile(@RequestBody UserProfileRequest request, Principal principal) {
        return ResponseEntity.ok(
                userService.updateUserProfile(request, principal.getName())
        );
    }

    @PostMapping("/profile/change-password")
    @Operation(summary = "User profile, Role: All", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request, Principal principal) {
        authenticationService.changePassword(request, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
