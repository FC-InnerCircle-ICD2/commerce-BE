package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.category.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByIsActiveIsTrueAndIsDeletedIsFalse();
}
