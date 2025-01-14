package com.vasylenkob.pastebin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PutPostRequest {
 private String postTitle;
 private String content;
 private Integer lifetimeMinutes;
}
