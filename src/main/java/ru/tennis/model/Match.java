package ru.tennis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tennis_matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int id;

//    @Column(name = "player1", nullable = false)
    @ManyToOne
    @JoinColumn(name = "player1", referencedColumnName = "id")
    public Player player1;

//    @Column(name = "player2", nullable = false)
    @ManyToOne
    @JoinColumn(name = "player2", referencedColumnName = "id")
    public Player player2;

//    @Column(name = "winner")
    @ManyToOne
    @JoinColumn(name = "winner", referencedColumnName = "id")
    public Player winner;
}
