package com.emotionalcart.s3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

@Slf4j
@Component
public class S3Utils {
    private final S3Client s3Client;

    public S3Utils(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public ResponseEntity<String> uploadFile(String bucketName, String directory, MultipartFile file) {
        try {
            String key = directory + "/" + file.getOriginalFilename();
            log.info("upload key: " + key);

            PutObjectRequest request = PutObjectRequest.builder()
                                                       .bucket(bucketName)
                                                       .key(key)
                                                       .build();

            PutObjectResponse response = s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

            if (response.sdkHttpResponse().isSuccessful()) {
                return ResponseEntity.ok(createS3FileUrl(bucketName, key));
            } else {
                return ResponseEntity.internalServerError().body("Cannot upload file: " + file.getOriginalFilename());
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Cannot upload file: " + file.getOriginalFilename());
        }
    }

    private String createS3FileUrl(String bucketName, String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }
}
