package com.vasylenkob.pastebin.dto;

import lombok.Data;

@Data
public class SignUpUser {
    private String username;
    private String email;
    private String password;
}
