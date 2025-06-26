package com.example.ayen.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Lob
    private String description;

    private String image_url;

    public Scenario(String title, String description, String image_url) {
        this.title = title;
        this.description = description;
        this.image_url = image_url;
    }
}
