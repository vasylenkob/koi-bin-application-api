package com.vasylenkob.pastebin.services;

import com.vasylenkob.pastebin.entities.MetaData;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class HashService {
    public String makeHash(MetaData metaData){
        return Base64.getEncoder().withoutPadding().encodeToString(String.valueOf(metaData.getMetaId()).getBytes());
    }

    public Long deHash(String hash) throws IllegalArgumentException{
        byte[] decodedBytes = Base64.getDecoder().decode(hash);
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        return Long.parseLong(decodedString);
    }
}
