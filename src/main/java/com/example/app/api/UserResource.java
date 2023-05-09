package com.example.app.api;

import com.example.app.api.domain.PagedResponse;
import com.example.app.api.domain.PaginationRequest;
import com.example.app.api.helper.PaginationHelper;
import com.example.app.entities.AppUser;
import com.example.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @GetMapping
    @Secured("ROLE_ADMIN")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PagedResponse<AppUser>> getAll(PaginationRequest request, HttpServletRequest req) {
        List<AppUser> result = userService.getAllUsers(PaginationHelper.parsePagingRequest(request));

        return ResponseEntity.ok(
                PaginationHelper.createPagedResponse(
                        request,
                        result,
                        result.size(),
                        req.getRequestURI()
                )
        );
    }


}
