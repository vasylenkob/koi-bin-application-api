package com.vasylenkob.pastebin.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class MetaData {

    public MetaData(String title, String postName) {
        this.title = title;
        this.postName = postName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long metaId;

    private String postName;
    private String title;
}
