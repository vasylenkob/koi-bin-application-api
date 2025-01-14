package com.vasylenkob.pastebin.controllers;

import com.vasylenkob.pastebin.dto.PutPostRequest;
import com.vasylenkob.pastebin.entities.User;
import com.vasylenkob.pastebin.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class MainController {
    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody PutPostRequest putPostRequest, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(postService.savePost(putPostRequest, user.getId()));
    }

    @GetMapping("/{hash}")
    public ResponseEntity<?> getPost(@PathVariable String hash){
        return ResponseEntity.ok(postService.getPost(hash));
    }

    @DeleteMapping("/{hash}")
    public ResponseEntity<?> deletePost(Authentication authentication, @PathVariable String hash){
        User user = (User) authentication.getPrincipal();
        postService.deletePost(user.getId(), hash);
        return ResponseEntity.ok("Post deleted");
    }

    @PutMapping("/{hash}")
    public ResponseEntity<?> editPost(@PathVariable String hash, @RequestBody PutPostRequest putPostRequest, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(postService.updatePost(hash, putPostRequest, user.getId()));
    }
    
    @GetMapping("/myposts")
    public ResponseEntity<?> getMyPosts(Authentication authentication){
        return ResponseEntity.ok(postService.getMyPosts(authentication));
    }
}
