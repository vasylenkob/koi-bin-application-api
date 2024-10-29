package com.vasylenkob.pastebin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Post {
 private String postTitle;
 private String content;
 private int lifetimeMinutes;
}
