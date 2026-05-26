package ru.tennis.dto;

import lombok.*;

import java.util.UUID;

import ru.tennis.model.Player;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CurrentMatch {
    private String uuid;
    private FirstPlayer firstPlayer;
    private SecondPlayer secondPlayer;
    @Setter
    private Winner winner;
    @Setter
    private boolean tieBreak;

    public CurrentMatch(Player firstPlayer, Player secondPlayer) {
        this.uuid = UUID.randomUUID().toString();
        this.firstPlayer = new FirstPlayer(firstPlayer);
        this.secondPlayer = new SecondPlayer(secondPlayer);
        this.winner = null;
        this.tieBreak = false;
    }

    public boolean hasWinner() {
        return winner != null;
    }
}
