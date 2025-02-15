package com.emotionalcart.adminproduct.infrastructure.repository;

import com.emotionalcart.core.feature.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminCategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndIsDeletedIsFalse(Long categoryId);
}
