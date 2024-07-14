package com.project.JewelryMS.SystemTest;

import com.project.JewelryMS.controller.CategoryController;
import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.model.Category.CategoryRequest;
import com.project.JewelryMS.model.Category.CategoryResponse;
import com.project.JewelryMS.model.Category.CreateCategoryRequest;
import com.project.JewelryMS.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory() {
        CreateCategoryRequest request = new CreateCategoryRequest();
        Category category = new Category();
        when(categoryService.createCategory(request)).thenReturn(category);

        ResponseEntity<Category> response = categoryController.createCategory(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    void getAllCategories() {
        List<CategoryResponse> categories = Arrays.asList(new CategoryResponse(), new CategoryResponse());
        when(categoryService.readAllCategory()).thenReturn(categories);

        ResponseEntity<List<CategoryResponse>> response = categoryController.getAllCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categories, response.getBody());
    }

    @Test
    void getCategoryById() {
        Long id = 1L;
        CategoryResponse category = new CategoryResponse();
        when(categoryService.readByIDCategory(id)).thenReturn(category);

        ResponseEntity<CategoryResponse> response = categoryController.getCategoryById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

//    @Test
//    void deleteCategory() {
//        Long id = 1L;
//
//        ResponseEntity<String> response = categoryController.deleteCategory(id);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Category deleted successfully", response.getBody());
//        verify(categoryService).deleteCategory(id);
//    }

    @Test
    void updateCategory() {
        Long id = 1L;
        CategoryRequest request = new CategoryRequest();

        ResponseEntity<String> response = categoryController.updateCategory(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Category updated successfully", response.getBody());
        verify(categoryService).updateCategory(id, request);
    }
}