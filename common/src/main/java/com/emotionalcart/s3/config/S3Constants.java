package com.emotionalcart.s3.config;

public class S3Constants {
    public static final String BUCKET_NAME = "emotionalcart-bucket";
    public static final String REVIEW_DIRECTORY = "reviews";
    public static final String PRODUCT_DIRECTORY = "products";
    public static final String BANNER_DIRECTORY = "banners";
    public static final String BASE_S3_URL = "https://%s.s3.amazonaws.com/%s";

    private S3Constants() {
    }

    public static String getFileUrl(String key) {
        return String.format(BASE_S3_URL, BUCKET_NAME, key);
    }
}
