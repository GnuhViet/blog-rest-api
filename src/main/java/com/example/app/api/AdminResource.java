package com.example.app.api;

import com.example.app.api.helper.PaginationHelper;
import com.example.app.api.model.paging.PagedResponse;
import com.example.app.api.model.paging.PaginationRequest;
import com.example.app.constants.Constants;
import com.example.app.dtos.appuser.AppUserWithArticlesDTO;
import com.example.app.dtos.appuser.AppUserWithCommentsDTO;
import com.example.app.dtos.appuser.DetailsAppUserDTO;
import com.example.app.dtos.appuser.SimpleAppUserDTO;
import com.example.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminResource {
    private final UserService userService;

    @GetMapping("/users")
    @Secured(Constants.ROLE_ADMIN)
    @Operation(summary = "List user, Role: Admin", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PagedResponse<SimpleAppUserDTO>> getAll(@Valid PaginationRequest request) {
        return ResponseEntity.ok(
                PaginationHelper.createPagedResponse(
                        request,
                        userService.getAllUsers(PaginationHelper.parsePagingRequest(request), SimpleAppUserDTO.class),
                        userService.countUser()
                )
        );
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Single user info, Role: Admin", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<DetailsAppUserDTO> getSingle(@PathVariable String userId) {
        return ResponseEntity.ok(
                userService.getUserDto(userId, DetailsAppUserDTO.class)
        );
    }

    @GetMapping("/users/{username}/article")
    @Secured(Constants.ROLE_ADMIN)
    @Operation(summary = "User articles, Role: Admin", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<AppUserWithArticlesDTO> getUserArticle(@PathVariable String username) {
        return ResponseEntity.ok(
                userService.getUserDto(username, AppUserWithArticlesDTO.class)
        );
    }

    @GetMapping("/users/{username}/comments")
    @Secured(Constants.ROLE_ADMIN)
    @Operation(summary = "User comments, Role: Admin", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<AppUserWithCommentsDTO> getUserComments(@PathVariable String username) {
        return ResponseEntity.ok(
                userService.getUserDto(username, AppUserWithCommentsDTO.class)
        );
    }

    @PutMapping("/users/set-admin")
    @Secured(Constants.ROLE_ADMIN)
    @Operation(summary = "Set role admin, Role: Admin", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> setAdmin(@RequestBody List<String> usernames) {
        userService.setAdminRoleToUsers(usernames);
        return ResponseEntity.noContent().build();
    }
}
