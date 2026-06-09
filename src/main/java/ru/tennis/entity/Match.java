package ru.tennis.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tennis_matches")
@Check(constraints = "player1_id != player2_id AND winner_id IN (player1_id, player2_id)")
public class Match {

    public Match(Player player1, Player player2, Player winner) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player1_id", referencedColumnName = "id")
    private Player player1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player2_id", referencedColumnName = "id")
    private Player player2;

    @ManyToOne(optional = false)
    @JoinColumn(name = "winner_id", referencedColumnName = "id")
    private Player winner;
}
