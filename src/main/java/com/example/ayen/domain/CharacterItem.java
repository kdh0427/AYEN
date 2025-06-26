package com.example.ayen.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CharacterItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "character_id", unique = true)
    private Character character;

    private int quantity;

    public CharacterItem(Character character, int quantity) {
        this.character = character;
        this.quantity = quantity;
    }
}
