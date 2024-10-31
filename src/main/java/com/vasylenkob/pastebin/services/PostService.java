package com.vasylenkob.pastebin.services;

import com.vasylenkob.pastebin.dto.PostForm;
import com.vasylenkob.pastebin.dto.SavedPost;
import com.vasylenkob.pastebin.dto.ShortPostDetails;
import com.vasylenkob.pastebin.entities.MetaData;
import com.vasylenkob.pastebin.entities.User;
import com.vasylenkob.pastebin.exceptions.PostDoesNotExistException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {
    private final MetaDataService metaDataService;
    private final HashService hashService;
    private final AmazonS3ClientService s3;
    private final UserService userService;

    public String savePost(PostForm postForm, Authentication authentication){
        String postKey = createPostKey(postForm.getPostTitle());
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationDate = currentTime.plusMinutes(postForm.getLifetimeMinutes());
        Optional<User> currentUserOptional = userService
                .getUserById(((User) authentication.getPrincipal())
                        .getId());
        User currentUser = currentUserOptional.orElseThrow();
        MetaData meta = MetaData.builder()
                .user(currentUser)
                .postKey(postKey)
                .expirationDate(expirationDate)
                .title(postForm.getPostTitle())
                .build();
        metaDataService.saveMetaData(meta);
        s3.savePost(postKey, postForm.getContent());
        return hashService.makeHash(meta);
    }

    public SavedPost getPost(String hash) {
        MetaData metaData;
        try {
            metaData = metaDataService.findByHash(hash).orElseThrow();
        } catch (RuntimeException e) {
            throw new PostDoesNotExistException("Post does not exist");
        }
        return s3.getSavedPost(metaData);
    }

    public List<ShortPostDetails> getMyPosts(Authentication authentication){
        Optional<User> currentUserOptional = userService
                .getUserById(((User) authentication.getPrincipal())
                        .getId());
        User currentUser = currentUserOptional.orElseThrow();

        return metaDataService.getAllMetaDataByUserId(currentUser.getId())
                    .stream().map(this::createShortPostDetails).toList();
    }

    public ShortPostDetails createShortPostDetails(MetaData metaData){
        String hash = hashService.makeHash(metaData);
        String link = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/" + hash)
                .toUriString();
        return ShortPostDetails.builder()
                .link(link)
                .title(metaData.getTitle())
                .build();
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
