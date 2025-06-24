package com.example.ayen.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CharacterStat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "character_id", unique = true)
    private Character character;

    private int attack, defense, health, mana, intelligence, agility;

    public CharacterStat(Character character, int attack, int defense, int health, int mana, int intelligence, int agility) {
        this.character = character;
        this.attack = attack;
        this.defense = defense;
        this.health = health;
        this.mana = mana;
        this.intelligence = intelligence;
        this.agility = agility;
    }
}
