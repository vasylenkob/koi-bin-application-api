package com.vasylenkob.pastebin.controllers;

import com.vasylenkob.pastebin.dto.PostForm;
import com.vasylenkob.pastebin.entities.MetaData;
import com.vasylenkob.pastebin.exceptions.PostDoesNotExistException;
import com.vasylenkob.pastebin.services.MetaDataService;
import com.vasylenkob.pastebin.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class PostController {
    private final PostService postService;
    private final MetaDataService metaDataService;

    @PostMapping("/")
    public ResponseEntity<?> createPost(@RequestBody PostForm postForm, Authentication authentication){
        return ResponseEntity.ok(postService.savePost(postForm, authentication));
    }

    @GetMapping("/{hash}")
    public ResponseEntity<?> getPost(@PathVariable String hash){
        return ResponseEntity.ok(postService.getPost(hash));
    }

    @DeleteMapping("/{hash}")
    public ResponseEntity<?> deletePost(@PathVariable String hash){
        MetaData metaData = metaDataService.findByHash(hash)
                .orElseThrow(() -> new PostDoesNotExistException("Post does not exist"));
        postService.deletePost(metaData);
        return ResponseEntity.ok("Post deleted");
    }
    
    @GetMapping("/myposts")
    public ResponseEntity<?> getMyPosts(Authentication authentication){
        return ResponseEntity.ok(postService.getMyPosts(authentication));
    }
}
