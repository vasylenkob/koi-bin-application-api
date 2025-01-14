package com.vasylenkob.pastebin.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class PostDTO implements Serializable {
    private String postTitle;
    private String content;
    private LocalDateTime expirationDate;
}
