package ru.tennis.dto;

import lombok.Getter;
import lombok.Setter;
import ru.tennis.model.Player;

@Getter
@Setter
public class Winner {
    private Player winner;
    private int winnerId;
    private String winnerName;

    Winner(Player player) {
        this.winner = player;
        this.winnerId = player.getId();
        this.winnerName = player.getName();
    }
}
