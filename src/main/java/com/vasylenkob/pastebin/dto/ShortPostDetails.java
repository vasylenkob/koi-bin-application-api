package com.vasylenkob.pastebin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShortPostDetails {
    private String link;
    private String title;
}
