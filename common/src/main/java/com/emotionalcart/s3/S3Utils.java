package com.emotionalcart.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Utils {
    private final S3Client s3Client;

    public String uploadFile(String bucketName, String directory, String id, MultipartFile file) throws Exception {
        String key = directory + "/" + id + "/" + file.getOriginalFilename();
        log.info("upload key: " + key);

        PutObjectRequest request = PutObjectRequest.builder()
                                                   .bucket(bucketName)
                                                   .key(key)
                                                   .build();

        PutObjectResponse response = s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

        if (response.sdkHttpResponse().isSuccessful()) {
            return createS3FileUrl(bucketName, key);
        }
        throw new RuntimeException(response.sdkHttpResponse().statusText().toString());
    }

    private String createS3FileUrl(String bucketName, String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }

    public void deleteFile(String bucketName, String key) throws RuntimeException {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                                                               .bucket(bucketName)
                                                               .key(key)
                                                               .build();

        DeleteObjectResponse response = s3Client.deleteObject(deleteRequest);

        if (!response.sdkHttpResponse().isSuccessful()) {
            throw new RuntimeException(response.sdkHttpResponse().statusText().toString());
        }
    }
}
