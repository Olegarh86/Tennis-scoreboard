package ru.tennis.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "players", indexes = {@Index(name = "idx_players_name", columnList = "name")})
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Setter
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 30)
    private String name;

    public Player(String name) {
        this.name = name;
    }
}
