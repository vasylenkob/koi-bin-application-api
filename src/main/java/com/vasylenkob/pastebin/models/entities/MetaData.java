package com.vasylenkob.pastebin.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long metaId;

    private final String postKey;
    private final String title;
    private final LocalDateTime expirationDate;

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expirationDate);
    }
}
