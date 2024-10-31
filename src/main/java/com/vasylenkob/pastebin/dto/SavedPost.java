package com.vasylenkob.pastebin.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SavedPost {
    private String postTitle;
    private String content;
    private LocalDateTime expirationDate;
}
