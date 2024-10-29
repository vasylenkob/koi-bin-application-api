package com.vasylenkob.pastebin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SavedPost {
    private String postTitle;
    private String content;
    private LocalDateTime expirationDate;
}
