package com.vasylenkob.pastebin.services;

import com.vasylenkob.pastebin.dto.Post;
import com.vasylenkob.pastebin.dto.SavedPost;
import com.vasylenkob.pastebin.entities.MetaData;
import com.vasylenkob.pastebin.exceptions.PostDoesNotExistException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PostService {
    private final MetaDataService metaDataService;
    private final HashService hashService;
    private final AmazonS3ClientService s3;

    public String savePost(Post post){
        String postKey = createPostKey(post.getPostTitle());
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationDate = currentTime.plusMinutes(post.getLifetimeMinutes());

        MetaData meta = metaDataService.saveMetaData(
                new MetaData(postKey, post.getPostTitle(), expirationDate));

        s3.savePost(postKey, post.getContent());
        return hashService.makeHash(meta);
    }

    public SavedPost getPost(String hash) {
        long postId;
        try {
             postId = Long.parseLong(hashService.deHash(hash));
        } catch (RuntimeException e) {
            throw new PostDoesNotExistException("Post does not exist");
        }
        MetaData metaData = metaDataService
                .getMetaData(postId)
                .orElseThrow(() -> new PostDoesNotExistException("Post does not exist"));
        return s3.getSavedPost(metaData);
    }

    private String createPostKey(String title){
        LocalDateTime currentDate = LocalDateTime.now();
        return currentDate + " : " + title;
    }

    public void deletePost(MetaData metaData) {
        metaDataService.deleteMetaDataById(metaData.getMetaId());
        s3.deletePost(metaData);
    }
}
