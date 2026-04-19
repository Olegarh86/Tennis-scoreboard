package ru.tennis;

import ru.tennis.model.Player;

public class FirstPlayer {

    public final Integer id;
    public final String name;
    public final Score score;
    public final Integer game;
    public final Integer set;

    FirstPlayer(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.score = Score.ZERO;
        this.game = 0;
        this.set = 0;
    }
}
