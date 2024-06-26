package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.ProductBuy;
import com.project.JewelryMS.model.Category.CategoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.status = 1")
    List<Category> findAllCategories();

    @Query("SELECT c FROM Category c WHERE c.id = :id AND c.status = 1")
    Optional<Category> findCategoryById(Long id);

    @Query("SELECT c FROM Category c WHERE c.name = :name AND c.status = 1")
    Optional<Category> findCategoryByName(String name);
}
