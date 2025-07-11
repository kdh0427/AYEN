package com.example.ayen.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ChoiceRequest {
    private Effect effect;
    private List<Item> item;
    private Map<String, String> requiredItem;

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
        private String description;
        // getters, setters
    }
}
