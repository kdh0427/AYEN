package com.example.ayen.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Lob
    private String description;

    @Column(columnDefinition = "json")
    private String settings;

    public Item(String name, String description, String settings) {
        this.name = name;
        this.description = description;
        this.settings = settings;
    }
}
