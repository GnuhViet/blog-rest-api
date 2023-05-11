package com.example.app.api;

import com.example.app.api.domain.paging.PagedResponse;
import com.example.app.api.domain.paging.PaginationRequest;
import com.example.app.api.helper.PaginationHelper;
import com.example.app.entities.AppUser;
import com.example.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @GetMapping
    @Secured("ROLE_ADMIN")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PagedResponse<AppUser>> getAll(@Valid PaginationRequest request, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(
                PaginationHelper.createPagedResponse(
                        request,
                        userService.getAllUsers(PaginationHelper.parsePagingRequest(request)),
                        userService.countUser(),
                        httpServletRequest.getRequestURI()
                )
        );
    }



}
