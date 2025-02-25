package com.emotionalcart.core.feature.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderStatistics {
    @Id
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long categoryId;

    private Long totalOrder;

    private Long totalQuantitySold;

    @LastModifiedDate
    private LocalDateTime lastOrderedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
