package com.vasylenkob.pastebin.services;

import com.vasylenkob.pastebin.dto.PostDTO;
import com.vasylenkob.pastebin.entities.MetaData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;

@Service
public class AmazonS3ClientService {
    @Value("${aws.bucket.name}")
    private String bucketName;
    private final S3Client s3;

    public AmazonS3ClientService(S3Client s3){
        this.s3 = s3;
    }

    public void putPost(String postKey, String content) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(postKey)
                .build();
        try {
            s3.putObject(putObjectRequest, RequestBody.fromBytes(content.getBytes()));
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to save post to S3: " + e.getMessage(), e);
        }
    }

    //TODO move builder to postservice
    public PostDTO getPost(MetaData metaData){
        return PostDTO.builder()
                .postTitle(metaData.getTitle())
                .content(getContentFromS3(metaData.getPostKey()))
                .expirationDate(metaData.getExpirationDate())
                .build();
    }

    public void deletePost(MetaData metaData) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(metaData.getPostKey())
                .build();
        try {
            s3.deleteObject(deleteObjectRequest);
        }catch (S3Exception e){
            throw new RuntimeException("Failed to delete post from S3: " + e.getMessage(), e);
        }
    }

    private String getContentFromS3(String postKey) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(postKey)
                .build();
        try {
            var response = s3.getObject(getObjectRequest);
            return new String(response.readAllBytes());
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to get post content from S3: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading from response: " + e.getMessage(), e);
        }
    }
}
