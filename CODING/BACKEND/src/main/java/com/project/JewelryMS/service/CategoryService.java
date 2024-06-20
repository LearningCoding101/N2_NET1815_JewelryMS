package com.project.JewelryMS.service;


import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.model.Category.CategoryRequest;
import com.project.JewelryMS.model.Category.CategoryResponse;
import com.project.JewelryMS.model.Category.CreateCategoryRequest;
import com.project.JewelryMS.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    public Category createCategory(CreateCategoryRequest createCategoryRequest){
        Category category = new Category();
        category.setName(createCategoryRequest.getName());
        category.setDescription(createCategoryRequest.getDescription());
        category.setStatus(1);
        return categoryRepository.save(category);

    }

    public List<CategoryResponse> readAllCategory(){
        List<Category> category = categoryRepository.findAllCategories();
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for(Category category1: category) {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setDescription(category1.getDescription());
            categoryResponse.setName(category1.getName());
            categoryResponse.setId(category1.getId());
            categoryResponses.add(categoryResponse);
        }
        return categoryResponses;
    }

    public CategoryResponse readByIDCategory(Long id){
        Optional<Category> categoryResponseOptional = categoryRepository.findCategoryById(id);
        Category category = categoryResponseOptional.get();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setDescription(category.getDescription());
        categoryResponse.setName(category.getName());
        categoryResponse.setId(category.getId());
        return categoryResponse;
    }


    public Category readCategoryById(Long id){
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.orElseGet(Category::new);
    }

    public List<Category> findAllCategories(){
        return categoryRepository.findAll();
    }
    public void updateCategory(Long id, CategoryRequest categoryRequest){
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if(categoryOpt.isPresent()){
            Category category = categoryOpt.get();
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());
            categoryRepository.save(category);
        }
    }
    public void deleteCategory(long id){
        Optional<Category> productSellOptional = categoryRepository.findById(id);
        if(productSellOptional.isPresent()){
            Category category = productSellOptional.get();
            category.setStatus(0);
            categoryRepository.save(category);
        }else {
            throw new RuntimeException("Product Sell with ID " + id + " not found");
        }
    }
}
