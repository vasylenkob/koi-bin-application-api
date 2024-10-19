package com.vasylenkob.pastebin.services;

import com.vasylenkob.pastebin.models.Post;
import com.vasylenkob.pastebin.models.entities.MetaData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {
    MetaDataService metaDataService;
    HashService hashService;
    AmazonS3ClientService s3;

    public String savePost(Post post){
        String postName = createPostName(post.getPostTitle());
        MetaData meta = metaDataService.saveMetaData(new MetaData(post.getPostTitle(), postName));
        s3.savePost(postName, post.getContent());
        return hashService.makeHash(meta);
    }

    // TODO: добавить исключения!!
    public Optional<Post> getPost(String hash){
        MetaData metaData = metaDataService.getMetaData(Long.valueOf(hashService.deHash(hash))).get();
        return Optional.of(s3.getPost(metaData));
    }

    private String createPostName(String title){
        Date currentDate = new Date();
        return currentDate + ":" + title;
    }

}
