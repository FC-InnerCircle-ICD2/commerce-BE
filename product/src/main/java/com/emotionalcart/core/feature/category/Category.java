package com.emotionalcart.core.feature.category;

import com.emotionalcart.core.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    // category : 양방향 연관관계를 설정하여, 자식 엔티티에서 부모 엔티티를 참조할 뿐만 아니라, 부모 엔티티에서도 자식 엔티티를 참조

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private Boolean isActive = true;

    @NotNull
    @Column(nullable = false)
    private Integer depth;

    // 부모 카테고리에 대한 양방향 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    // 자식 카테고리에 대한 양방향 연관관계
    // cascade = CascadeType.ALL: 부모 엔티티의 모든 변경 사항이 자식 엔티티에 전파
    // orphanRemoval = true: 부모 엔티티에서 자식 엔티티가 제거되면, 자식 엔티티도 삭제
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Category> subCategories = new ArrayList<>();

    // 카테고리 생성
    public static Category create(String name, Category parentCategory) {
        int depth = (parentCategory != null) ? parentCategory.getDepth() + 1 : 1;
        Category category = new Category(parentCategory, name, true, depth);

        if (parentCategory != null) {
            parentCategory.getSubCategories().add(category);
        }

        return category;
    }

    // 생성자
    private Category(
            Category parentCategory,
            String name,
            Boolean isActive,
            Integer depth) {
        this.parentCategory = parentCategory;
        this.name = name;
        this.isActive = isActive;
        this.depth = depth;
    }
}
