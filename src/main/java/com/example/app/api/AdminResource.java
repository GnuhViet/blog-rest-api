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
@Secured(Constants.ROLE_ADMIN)
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminResource {
    private final UserService userService;

    @GetMapping("/users")
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

    @GetMapping("/users/{username}")
    @Operation(summary = "Single user info, Role: Admin", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<DetailsAppUserDTO> getSingle(@PathVariable String username) {
        return ResponseEntity.ok(
                userService.getUserDto(username, DetailsAppUserDTO.class)
        );
    }

    @GetMapping("/users/{username}/article")
    @Operation(summary = "User articles, Role: Admin", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<AppUserWithArticlesDTO> getUserArticle(@PathVariable String username) {
        return ResponseEntity.ok(
                userService.getUserDto(username, AppUserWithArticlesDTO.class)
        );
    }

    @GetMapping("/users/{username}/comments")
    @Operation(summary = "User comments, Role: Admin", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<AppUserWithCommentsDTO> getUserComments(@PathVariable String username) {
        return ResponseEntity.ok(
                userService.getUserDto(username, AppUserWithCommentsDTO.class)
        );
    }

    @PutMapping("/users/set-admin")
    @Operation(summary = "Set role admin, Role: Admin", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> setAdmin(@RequestBody Map<String, List<String>> users) {
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        users.get("username").forEach(user -> userService.addRoleToUser(user, Constants.ROLE_ADMIN));
        return ResponseEntity.noContent().build();
    }
}
