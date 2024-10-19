package com.vasylenkob.pastebin.controllers;

import com.vasylenkob.pastebin.models.Post;
import com.vasylenkob.pastebin.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class PostController {
    private PostService postService;

    @PostMapping("/")
    public ResponseEntity<?> createPost(@RequestBody Post post){
        return ResponseEntity.ok(postService.savePost(post));
    }

    // TODO: обработка опшинал
    @GetMapping("/{hash}")
    public ResponseEntity<?> getPost(@PathVariable String hash){
        return ResponseEntity.ok(postService.getPost(hash));
    }
}
