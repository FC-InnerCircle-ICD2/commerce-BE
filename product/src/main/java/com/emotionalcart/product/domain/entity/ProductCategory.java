package com.emotionalcart.product.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.emotionalcart.product.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategory {

    // category : 양방향 연관관계를 설정하여, 자식 엔티티에서 부모 엔티티를 참조할 뿐만 아니라, 부모 엔티티에서도 자식 엔티티를 참조

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotNull
//    @Column(nullable = false)
    private String name;

//    @NotNull
//    @Column(nullable = false)
//    private Boolean isActive = true;
    private Boolean isActive;

//    @NotNull
//    @Column(nullable = false)
    private Integer depth;

    // 부모 카테고리에 대한 양방향 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private ProductCategory parentProductCategory;

    // 자식 카테고리에 대한 양방향 연관관계
    // cascade = CascadeType.ALL: 부모 엔티티의 모든 변경 사항이 자식 엔티티에 전파
    // orphanRemoval = true: 부모 엔티티에서 자식 엔티티가 제거되면, 자식 엔티티도 삭제
    @OneToMany(mappedBy = "parentProductCategory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProductCategory> subCategories = new ArrayList<>();

    // 카테고리 생성
   public static ProductCategory create(String name, ProductCategory parentProductCategory) {
    int depth = (parentProductCategory != null) ? parentProductCategory.getDepth() + 1 : 1;
    ProductCategory productCategory = new ProductCategory(parentProductCategory, name, true, depth);

    if (parentProductCategory != null) {
        parentProductCategory.getSubCategories().add(productCategory);
    }

    return productCategory;
}

    // 생성자
    private ProductCategory(
            ProductCategory parentProductCategory,
            String name,
            Boolean isActive,
            Integer depth) {
        this.parentProductCategory = parentProductCategory;
        this.name = name;
        this.isActive = isActive;
        this.depth = depth;
    }
}
