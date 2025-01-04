package com.emotionalcart.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@ToString(exclude = {"parent", "children", "products"})
public class Category extends BaseEntity {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parent;

    private String name;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> children;

    @Column(name = "is_active")
    private boolean active = true;

    private int depth = 1;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    public static Category of(String categoryId, String name) {
        Category category = new Category();
        category.id = Long.parseLong(categoryId);
        category.name = name;
        return category;
    }

    public static Category of(String id, String name, Category parentCategory, int depth) {
        Category category = Category.of(id, name);
        category.parent = parentCategory;
        parentCategory.addChild(category);
        category.depth = depth;
        return category;
    }

    private void addChild(Category category) {
        if (CollectionUtils.isEmpty(children)) {
            children = new ArrayList<>();
        }
        children.add(category);
    }

    public void addProduct(Product product) {
        if (CollectionUtils.isEmpty(products)) {
            products = new ArrayList<>();
        }
        products.add(product);
    }

}
