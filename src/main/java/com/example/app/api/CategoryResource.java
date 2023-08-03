package com.example.app.api;

import com.example.app.constants.Constants;
import com.example.app.dto.category.CategoryDTO;
import com.example.app.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryResource {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategory() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @PostMapping
    @Secured(Constants.ROLE_ADMIN)
    @Operation(summary = "User profile, Role: Admin", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> createCategory(@RequestBody String name) {
        categoryService.save(name);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Secured(Constants.ROLE_ADMIN)
    @Operation(summary = "User profile, Role: Admin", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO category) {
        return ResponseEntity.ok(categoryService.update(category));
    }

    @DeleteMapping
    @Secured(Constants.ROLE_ADMIN)
    @Operation(summary = "User profile, Role: Admin", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteCategory(@RequestBody String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
