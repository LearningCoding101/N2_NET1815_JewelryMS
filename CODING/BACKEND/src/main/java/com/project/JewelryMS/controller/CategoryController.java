package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.model.Category.CategoryRequest;
import com.project.JewelryMS.model.Category.CategoryResponse;
import com.project.JewelryMS.model.Category.CreateCategoryRequest;
import com.project.JewelryMS.model.ProductSell.ProductSellRequest;
import com.project.JewelryMS.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    // Create a new category
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Category> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        Category category = categoryService.createCategory(createCategoryRequest);
        return ResponseEntity.ok(category);
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.readAllCategory();
        return ResponseEntity.ok(categories);
    }

    // Get a single category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        CategoryResponse categoryResponse = categoryService.readByIDCategory(id);
        return ResponseEntity.ok(categoryResponse);
    }

    // Delete a category by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

    // Update a category by ID
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok("Category updated successfully");
    }
}
