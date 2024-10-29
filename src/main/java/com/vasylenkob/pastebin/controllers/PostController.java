package com.vasylenkob.pastebin.controllers;

import com.vasylenkob.pastebin.dto.Post;
import com.vasylenkob.pastebin.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/")
    public ResponseEntity<?> createPost(@RequestBody Post post){
        return ResponseEntity.ok(postService.savePost(post));
    }

    @GetMapping("/{hash}")
    public ResponseEntity<?> getPost(@PathVariable String hash){
        return ResponseEntity.ok(postService.getPost(hash));
    }
}
