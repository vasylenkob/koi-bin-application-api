package com.vasylenkob.pastebin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShortPostInfoDTO {
    private String hash;
    private String title;
}
