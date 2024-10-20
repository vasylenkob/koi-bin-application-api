package com.vasylenkob.pastebin.services;

import com.vasylenkob.pastebin.models.Post;
import com.vasylenkob.pastebin.models.SavedPost;
import com.vasylenkob.pastebin.models.entities.MetaData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {
    MetaDataService metaDataService;
    HashService hashService;
    AmazonS3ClientService s3;

    public String savePost(Post post){
        String postKey = createPostKey(post.getPostTitle());
        LocalDateTime curretTime = LocalDateTime.now();
        LocalDateTime expirationDate = curretTime.plusMinutes(post.getLifetimeMinutes());

        MetaData meta = metaDataService.saveMetaData(
                new MetaData(postKey, post.getPostTitle(), expirationDate));

        s3.savePost(postKey, post.getContent());
        return hashService.makeHash(meta);
    }

    // TODO add exception??
    public Optional<SavedPost> getPost(String hash){
        MetaData metaData = metaDataService
                .getMetaData(Long.valueOf(hashService.deHash(hash))).get();
        return Optional.of(s3.getSavedPost(metaData));
    }

    private String createPostKey(String title){
        Date currentDate = new Date();
        return currentDate + ":" + title;
    }

    // TODO add exception??
    public void deletePost(MetaData metaData) {
        metaDataService.deleteMetaDataById(metaData.getMetaId());
        s3.deletePost(metaData);
    }
}
