package com.example.ayen.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChoiceRequest {
    private Effect effect;
    private List<Item> item_changes;

    @Getter
    @Setter
    public static class Effect {
        private int attack;
        private int defense;
        private int health;
        private int mana;
        private int intelligence;
        private int agility;
    }

    @Getter
    @Setter
    public static class Item {
        private String name;
        private int effect;
        private String description;
    }
}
