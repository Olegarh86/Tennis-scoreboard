package ru.tennis.dto;

import lombok.*;

import java.util.UUID;

import ru.tennis.model.Player;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CurrentMatch {
    public String uuid;
    public FirstPlayer firstPlayer;
    public SecondPlayer secondPlayer;
    public Winner winner;
    public Boolean tieBreak;
    public Boolean endMatch;

    public CurrentMatch(Player firstPlayer, Player secondPlayer) {
        this.uuid = UUID.randomUUID().toString();
        this.firstPlayer = new FirstPlayer(firstPlayer);
        this.secondPlayer = new SecondPlayer(secondPlayer);
        this.winner = new Winner(new Player());
        this.tieBreak = false;
        this.endMatch = false;
    }
}
