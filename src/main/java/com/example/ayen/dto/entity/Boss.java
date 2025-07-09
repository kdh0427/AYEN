package com.example.ayen.dto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Boss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    private int attack, defense, health;

    public Boss(Scenario scenario, int attack, int defense, int health){
        this.scenario = scenario;
        this.attack = attack;
        this.defense = defense;
        this.health = health;
    }
}
