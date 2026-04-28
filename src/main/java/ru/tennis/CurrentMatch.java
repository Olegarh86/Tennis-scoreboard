package ru.tennis;

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
    public Integer winnerId;
    public Boolean tieBreak;
    public Boolean endMatch;

    public CurrentMatch(Player firstPlayer, Player secondPlayer) {
        this.uuid = UUID.randomUUID().toString();
        this.firstPlayer = new FirstPlayer(firstPlayer);
        this.secondPlayer = new SecondPlayer(secondPlayer);
        this.winnerId = 0;
        this.tieBreak = false;
        this.endMatch = false;
    }
}
