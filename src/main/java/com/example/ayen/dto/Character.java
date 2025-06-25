package com.example.ayen.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "`user_character`")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    @ManyToOne
    @JoinColumn(name = "senario_id") //
    private Scenario senario;

    private LocalDateTime created_at;

    public Character(User user, String name, Scenario senario) {
        this.user = user;
        this.name = name;
        this.senario = senario;
        this.created_at = LocalDateTime.now();
    }
}
