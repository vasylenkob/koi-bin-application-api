package com.vasylenkob.pastebin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;

import software.amazon.awssdk.regions.Region;

@Configuration
public class AppConfig {

    @Value("${aws.region}")
    private Region region;
    @Value("${aws.accessKeyId}")
    private String access_key_id;
    @Value("${aws.secretAccessKey}")
    private String secret_access_key;

    @Bean
    public S3Client s3(){
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(access_key_id, secret_access_key);
        return S3Client.builder().region(region).credentialsProvider(StaticCredentialsProvider.create(awsCreds)).build();
    }
}
