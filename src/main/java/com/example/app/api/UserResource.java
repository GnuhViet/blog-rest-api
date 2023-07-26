package com.example.app.api;

import com.example.app.api.model.paging.PagedResponse;
import com.example.app.api.model.paging.PaginationRequest;
import com.example.app.api.helper.PaginationHelper;
import com.example.app.api.model.user.UserProfileRequest;
import com.example.app.constants.Constants;
import com.example.app.dto.AppUserDto;
import com.example.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping
    @Secured(Constants.ROLE_ADMIN)
    @Operation(summary = "List user, Role: Admin", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PagedResponse<AppUserDto>> getAll(@Valid PaginationRequest request, HttpServletRequest httpServletRequest) {

        return ResponseEntity.ok(
                PaginationHelper.createPagedResponse(
                        request,
                        userService.getAllUsers(PaginationHelper.parsePagingRequest(request)),
                        userService.countUser(),
                        httpServletRequest.getRequestURI()
                )
        );
    }

    @GetMapping("/profile")
    @Secured({Constants.ROLE_USER, Constants.ROLE_ADMIN})
    @Operation(summary = "User profile, Role: All", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<AppUserDto> getProfile(Principal principal) {
        return ResponseEntity.ok(
                userService.getUserDto(principal.getName())
        );
    }

    @PostMapping("/profile")
    @Secured({Constants.ROLE_USER, Constants.ROLE_ADMIN})
    @Operation(summary = "User profile, Role: All", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<AppUserDto> editProfile(@RequestBody UserProfileRequest userProfile, Principal principal) {
        return ResponseEntity.ok(
                userService.updateUserProfile(userProfile, principal.getName())
        );
    }

    @PostMapping("/profile/change-password")
    @Secured({Constants.ROLE_USER, Constants.ROLE_ADMIN})
    @Operation(summary = "User profile, Role: All", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<AppUserDto> changePassword(@RequestBody UserProfileRequest userProfile, Principal principal) {
        return ResponseEntity.ok(
                userService.updateUserProfile(userProfile, principal.getName())
        );
    }
}
