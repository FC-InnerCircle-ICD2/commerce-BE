package com.emotionalcart.order.domain.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class AuditableEntity extends BaseEntity {

    /**
     * 수정일시
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
