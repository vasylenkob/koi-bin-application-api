package com.vasylenkob.pastebin.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class MetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long metaId;

    @Column(unique = true, nullable = false)
    private String postKey;
    private String title;
    private LocalDateTime expirationDate;

    public MetaData(String postKey, String title, LocalDateTime expirationDate) {
        this.postKey = postKey;
        this.title = title;
        this.expirationDate = expirationDate;
    }

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expirationDate);
    }
}
