package com.vasylenkob.pastebin.dto;

import lombok.Data;

@Data
public class VerifyUser {
    private String email;
    private String verificationCode;
}
