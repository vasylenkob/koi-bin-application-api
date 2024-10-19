package com.vasylenkob.pastebin.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Post {
    private String postTitle;
    private String content;
}
