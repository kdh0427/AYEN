package com.example.ayen.dto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ScenarioPlayStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "character_id", unique = true)
    private ScenarioPlay scenarioPlay;

    private int attack, defense, health, mana, intelligence, agility;

    public ScenarioPlayStat(ScenarioPlay scenarioPlay, int attack, int defense, int health, int mana, int intelligence, int agility) {
        this.scenarioPlay = scenarioPlay;
        this.attack = attack;
        this.defense = defense;
        this.health = health;
        this.mana = mana;
        this.intelligence = intelligence;
        this.agility = agility;
    }
}
