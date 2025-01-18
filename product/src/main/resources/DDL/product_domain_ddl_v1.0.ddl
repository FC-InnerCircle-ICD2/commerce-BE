-- 1. 데이터베이스 생성 및 사용 (없으면 생성)
-- DROP DATABASE IF EXISTS `ecommerce`;
-- CREATE DATABASE `ecommerce` DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;
USE `ecommerce`;

-- 2. 기존 테이블 DROP (의존 관계와 상관없이 drop 순서는 역순으로 해도 무방)
DROP TABLE IF EXISTS `product_banner`;
DROP TABLE IF EXISTS `product_image`;
DROP TABLE IF EXISTS `product_option_detail`;
DROP TABLE IF EXISTS `product_option`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `banner`;
DROP TABLE IF EXISTS `banner_image`;
DROP TABLE IF EXISTS `product_category`;
DROP TABLE IF EXISTS `provider`;

-----------------------------------------------------------
-- 3. 테이블 생성 순서: 외래 키 관계를 고려하여 생성
-----------------------------------------------------------

-- [provider] 테이블 생성 (독립)
CREATE TABLE IF NOT EXISTS `provider` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '업체ID',
    `name` VARCHAR(200) NOT NULL COMMENT '업체이름',
    `description` VARCHAR(200) NULL COMMENT '업체설명',
    `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
    `is_deleted` BOOLEAN NOT NULL COMMENT '영업여부',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- [product_category] 테이블 생성 (자기 참조 FK 포함)
CREATE TABLE IF NOT EXISTS `product_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '상품카테고리ID',
    `parent_product_category_id` BIGINT NULL COMMENT '부모상품카테고리ID',
    `name` VARCHAR(200) NOT NULL COMMENT '상품카테고리이름',
    `is_active` BOOLEAN NOT NULL COMMENT '사용유무',
    `depth` INT NOT NULL COMMENT '대소분류. 1=상위,2=하위',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
    `is_deleted` BOOLEAN NOT NULL COMMENT '삭제여부',
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_product_category_TO_product_category_1`
        FOREIGN KEY (`parent_product_category_id`) REFERENCES `product_category`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- [banner_image] 테이블 생성 (독립)
CREATE TABLE IF NOT EXISTS `banner_image` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '배너사진ID',
    `file_path` VARCHAR(255) NOT NULL COMMENT 's3 파일 경로',
    `file_type` VARCHAR(50) NOT NULL COMMENT '파일 타입',
    `file_size` BIGINT NOT NULL COMMENT '파일 크기',
    `file_order` INT NOT NULL COMMENT '파일 순서',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
    `is_deleted` BOOLEAN NOT NULL COMMENT '삭제여부',
    `bucket_name` VARCHAR(100) NOT NULL COMMENT 's3 버킷명',
    `original_file_name` VARCHAR(260) NOT NULL COMMENT '파일명',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- [banner] 테이블 생성 (banner_image에 의존)
CREATE TABLE IF NOT EXISTS `banner` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '배너ID',
    `banner_image_id` BIGINT NOT NULL COMMENT '배너사진ID',
    `type` VARCHAR(10) NOT NULL COMMENT '상품/이벤트',
    `title` VARCHAR(200) NOT NULL COMMENT '배너제목',
    `banner_order` INT NOT NULL COMMENT '정렬순서',
    `start_date` TIMESTAMP NOT NULL COMMENT '배너시작날짜',
    `end_date` TIMESTAMP NOT NULL COMMENT '배너종료날짜',
    `icon_path` VARCHAR(200) NOT NULL COMMENT '아이콘 경로',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
    `is_deleted` BOOLEAN NOT NULL COMMENT '삭제여부',
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_banner_TO_banner_image_1`
        FOREIGN KEY (`banner_image_id`) REFERENCES `banner_image`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- [product] 테이블 생성 (provider, product_category에 의존)
CREATE TABLE IF NOT EXISTS `product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '상품ID',
    `provider_id` BIGINT NOT NULL COMMENT '업체ID',
    `category_id` BIGINT NOT NULL COMMENT '카테고리ID',
    `name` VARCHAR(200) NOT NULL COMMENT '상품이름',
    `description` TEXT NOT NULL COMMENT '상품설명',
    `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
    `is_deleted` BOOLEAN NOT NULL COMMENT '판매유무',
    `price` INT NOT NULL COMMENT '상품가격',
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_provider_TO_product_1`
        FOREIGN KEY (`provider_id`) REFERENCES `provider`(`id`),
    CONSTRAINT `FK_product_category_TO_product_1`
        FOREIGN KEY (`category_id`) REFERENCES `product_category`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- [product_option] 테이블 생성 (product에 의존)
CREATE TABLE IF NOT EXISTS `product_option` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '상품옵션ID',
    `product_id` BIGINT NOT NULL COMMENT '상품ID',
    `name` VARCHAR(200) NOT NULL COMMENT '상품옵션명',
    `is_deleted` BOOLEAN NOT NULL COMMENT '삭제여부',
    `is_required` BOOLEAN NOT NULL COMMENT '필수여부',
    PRIMARY KEY (`id`, `product_id`),
    CONSTRAINT `FK_product_TO_product_option_1`
        FOREIGN KEY (`product_id`) REFERENCES `product`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- [product_option_detail] 테이블 생성 (product_option에 의존)
CREATE TABLE IF NOT EXISTS `product_option_detail` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '상품옵션상세ID',
    `product_option_id` BIGINT NOT NULL COMMENT '상품옵션ID',
    `value` VARCHAR(200) NOT NULL COMMENT '상품옵션값',
    `quantity` INT NOT NULL COMMENT '잔여수량',
    `order` INT NOT NULL DEFAULT 1 COMMENT '정렬순서',
    `is_deleted` BOOLEAN NOT NULL COMMENT '삭제여부',
    `additional_price` INT NULL COMMENT '옵션추가금액',
    PRIMARY KEY (`id`, `product_option_id`),
    CONSTRAINT `FK_product_option_TO_product_option_detail_1`
  		FOREIGN KEY (`product_option_id`) REFERENCES `product_option`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- [product_banner] 테이블 생성 (product, banner에 의존)
CREATE TABLE IF NOT EXISTS `product_banner` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '상품배너ID',
    `product_id` BIGINT NOT NULL COMMENT '상품ID',
    `banner_id` BIGINT NOT NULL COMMENT '배너ID',
    `link_url` VARCHAR(200) NOT NULL COMMENT '배너 클릭시 이동할 url',
    `link_type` VARCHAR(10) NOT NULL DEFAULT 'internal' COMMENT 'internal/external',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
    `is_deleted` BOOLEAN NOT NULL COMMENT '삭제여부',
    PRIMARY KEY (`id`, `product_id`),
    CONSTRAINT `FK_product_TO_product_banner_1`
        FOREIGN KEY (`product_id`) REFERENCES `product`(`id`),
    CONSTRAINT `FK_banner_TO_product_banner_1`
        FOREIGN KEY (`banner_id`) REFERENCES `banner`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- [product_image] 테이블 생성 (product_option_detail에 의존)
CREATE TABLE IF NOT EXISTS `product_image` (
    `id` BIGINT NOT NULL COMMENT '상품사진ID',
    `product_option_detail_id` BIGINT NOT NULL COMMENT '상품옵션상세ID',
    `file_path` VARCHAR(255) NOT NULL COMMENT 's3 파일 경로',
    `file_type` VARCHAR(50) NOT NULL COMMENT '파일 타입',
    `file_size` BIGINT NOT NULL COMMENT '파일 크기',
    `file_order` INT NOT NULL COMMENT '파일 순서',
    `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
    `is_deleted` BOOLEAN NOT NULL COMMENT '삭제여부',
    `bucket_name` VARCHAR(100) NOT NULL COMMENT 's3 버킷명',
    `original_file_name` VARCHAR(260) NOT NULL COMMENT '파일명',
    `is_representative` BOOLEAN NOT NULL COMMENT '대표 여부',
    PRIMARY KEY (`id`, `product_option_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
