package com.vasylenkob.pastebin.services;

import com.vasylenkob.pastebin.dto.PutPostRequest;
import com.vasylenkob.pastebin.dto.PostDTO;
import com.vasylenkob.pastebin.dto.ShortPostInfoDTO;
import com.vasylenkob.pastebin.entities.MetaData;
import com.vasylenkob.pastebin.entities.User;
import com.vasylenkob.pastebin.exceptions.PostDoesNotExistException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @Transactional
    public String savePost(PutPostRequest putPostRequest, Long userId){
        String postKey = createPostKey(putPostRequest.getPostTitle());
        LocalDateTime expirationDate = calculateExpirationDateFor(putPostRequest);
        Optional<User> currentUserOptional = userService
                .getUserById(userId);
        User currentUser = currentUserOptional.orElseThrow();
        MetaData meta = MetaData.builder()
                .user(currentUser)
                .postKey(postKey)
                .expirationDate(expirationDate)
                .title(putPostRequest.getPostTitle())
                .build();
        metaDataService.saveMetaData(meta);
        s3.putPost(postKey, putPostRequest.getContent());
        return hashService.makeHash(meta);
    }


    //TODO rewrite method
    @Transactional
    @CachePut(value = "Post", key = "#hash")
    public PostDTO updatePost(String hash, PutPostRequest putPostRequest, Long userId){
        Long decodedHash = hashService.deHash(hash);
        if (metaDataService.getMetaData(userId, decodedHash).isPresent()){
            String postKey = createPostKey(putPostRequest.getPostTitle());
            LocalDateTime expirationDate = calculateExpirationDateFor(putPostRequest);
            Optional<User> currentUserOptional = userService
                    .getUserById(userId);
            User currentUser = currentUserOptional.orElseThrow();
            MetaData meta = MetaData.builder()
                    .user(currentUser)
                    .postKey(postKey)
                    .expirationDate(expirationDate)
                    .title(putPostRequest.getPostTitle())
                    .build();
            metaDataService.saveMetaData(meta);
            s3.putPost(postKey, putPostRequest.getContent());
            return PostDTO.builder()
                    .postTitle(putPostRequest.getPostTitle())
                    .content(putPostRequest.getContent())
                    .expirationDate(expirationDate)
                    .build();
        }
        throw new PostDoesNotExistException("Post does not exist or belong to this user");
    }

    @Cacheable("Post")
    public PostDTO getPost(String hash) {
        MetaData metaData;
        try {
            Long decodedHash = hashService.deHash(hash);
            metaData = metaDataService.getMetaData(decodedHash).orElseThrow();
        } catch (RuntimeException e) {
            throw new PostDoesNotExistException("Post does not exist");
        }
        return s3.getPost(metaData);
    }

    public List<ShortPostInfoDTO> getMyPosts(Authentication authentication){
        Optional<User> currentUserOptional = userService
                .getUserById(((User) authentication.getPrincipal())
                        .getId());
        User currentUser = currentUserOptional.orElseThrow();

        return metaDataService.getAllMetaDataByUserId(currentUser.getId())
                    .stream().map(this::createShortPostInfo).toList();
    }

    private String createPostKey(String title){
        LocalDateTime currentDate = LocalDateTime.now();
        return currentDate + " : " + title;
    }

    @Transactional
    @CacheEvict("Post")
    public void deletePost(MetaData metaData){
        metaDataService.deleteMetaData(metaData.getMetaId());
        s3.deletePost(metaData);
    }

    @Transactional
    @CacheEvict(value = "Post", key = "#hash")
    public void deletePost(Long userId, String hash) {
        Long decodedHash = hashService.deHash(hash);
        MetaData metaData = metaDataService.getMetaData(userId, decodedHash)
                .orElseThrow(() -> new PostDoesNotExistException("Post does not exist or belong to this user"));
        s3.deletePost(metaData);
        metaDataService.deleteMetaData(userId, decodedHash);
    }


    private LocalDateTime calculateExpirationDateFor(PutPostRequest post){

        if (post.getLifetimeMinutes()!= null){
            LocalDateTime currentTime = LocalDateTime.now();
            return currentTime.plusMinutes(post.getLifetimeMinutes());
        }
        return null;
    }

    private ShortPostInfoDTO createShortPostInfo(MetaData metaData){
        String hash = hashService.makeHash(metaData);
        String link = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/" + hash)
                .toUriString();
        return ShortPostInfoDTO.builder()
                .hash(hash)
                .title(metaData.getTitle())
                .build();
    }
}
