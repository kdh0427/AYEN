package com.example.ayen.dto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ScenarioPlayItem")
public class ScenarioItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "scenario_play_id")
    private ScenarioPlay scenarioPlay;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int quantity;

    public ScenarioItem(ScenarioPlay scenarioPlay, Item item, int quantity) {
        this.scenarioPlay = scenarioPlay;
        this.item = item;
        this.quantity = quantity;
    }
}
