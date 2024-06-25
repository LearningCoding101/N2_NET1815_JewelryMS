package com.project.JewelryMS;



import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.model.Category.CategoryRequest;
import com.project.JewelryMS.model.Category.CategoryResponse;
import com.project.JewelryMS.model.Category.CreateCategoryRequest;
import com.project.JewelryMS.repository.CategoryRepository;
import com.project.JewelryMS.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CreateCategoryRequest createCategoryRequest;
    private CategoryRequest categoryRequest;

    @BeforeEach
    public void setup() {
        category = new Category();
        category.setId(500L);
        category.setName("Necklace");
        category.setDescription("Gold Necklace");
        category.setStatus(1);

        createCategoryRequest = new CreateCategoryRequest();
        createCategoryRequest.setName("Test1");
        createCategoryRequest.setDescription("Gold Necklace");

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Test2");
        categoryRequest.setDescription("Diamond Bracelet");
    }

    @Test
    public void testCreateCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category createdCategory = categoryService.createCategory(createCategoryRequest);

        assertNotNull(createdCategory);
        assertEquals(category.getName(), createdCategory.getName());
        assertEquals(category.getDescription(), createdCategory.getDescription());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testReadAllCategory() {
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        when(categoryRepository.findAllCategories()).thenReturn(categories);

        List<CategoryResponse> categoryResponses = categoryService.readAllCategory();

        assertNotNull(categoryResponses);
        assertFalse(categoryResponses.isEmpty());
        assertEquals(category.getName(), categoryResponses.get(0).getName());
        verify(categoryRepository, times(1)).findAllCategories();
    }

    @Test
    public void testReadByIDCategory() {
        when(categoryRepository.findCategoryById(12L)).thenReturn(Optional.of(category));

        CategoryResponse categoryResponse = categoryService.readByIDCategory(12L);

        assertNotNull(categoryResponse);
        assertEquals(category.getName(), categoryResponse.getName());
        verify(categoryRepository, times(1)).findCategoryById(12L);
    }

    @Test
    public void testReadCategoryById() {
        when(categoryRepository.findById(12L)).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.readCategoryById(12L);

        assertNotNull(foundCategory);
        assertEquals(category.getName(), foundCategory.getName());
        verify(categoryRepository, times(1)).findById(12L);
    }

    @Test
    public void testFindAllCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> foundCategories = categoryService.findAllCategories();

        assertNotNull(foundCategories);
        assertFalse(foundCategories.isEmpty());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateCategory() {
        when(categoryRepository.findById(12L)).thenReturn(Optional.of(category));

        categoryService.updateCategory(12L, categoryRequest);

        verify(categoryRepository, times(1)).findById(12L);
        verify(categoryRepository, times(1)).save(category);
        assertEquals(categoryRequest.getName(), category.getName());
        assertEquals(categoryRequest.getDescription(), category.getDescription());
    }

    @Test
    public void testDeleteCategory() {
        when(categoryRepository.findById(12L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(12L);

        verify(categoryRepository, times(1)).findById(12L);
        verify(categoryRepository, times(1)).save(category);
        assertEquals(0, category.getStatus());
    }
}
