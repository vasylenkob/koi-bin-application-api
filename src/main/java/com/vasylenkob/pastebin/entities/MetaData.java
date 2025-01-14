package com.vasylenkob.pastebin.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long metaId;

    @Column(unique = true, nullable = false)
    private String postKey;
    private String title;
    private LocalDateTime expirationDate;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    public boolean isExpired(){
        if (expirationDate != null){
            return LocalDateTime.now().isAfter(expirationDate);
        }
        return false;
    }
}
