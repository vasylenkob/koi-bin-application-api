package com.vasylenkob.pastebin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostForm {
 private String postTitle;
 private String content;
 private int lifetimeMinutes;
}
