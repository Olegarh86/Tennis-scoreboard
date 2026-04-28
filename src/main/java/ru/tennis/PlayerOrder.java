package ru.tennis;

import lombok.Getter;
import lombok.Setter;
import ru.tennis.model.Player;

@Getter
@Setter
public class PlayerOrder {
    public final Integer id;
    public final String name;
    public Score score;
    public Game game;
    public Set set;

    PlayerOrder(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.score = new Score(0);
        this.game = Game.ZERO;
        this.set = Set.ZERO;
    }
}
