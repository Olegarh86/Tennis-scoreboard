package ru.tennis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tennis.gameState.Game;
import ru.tennis.gameState.Score;
import ru.tennis.gameState.Set;
import ru.tennis.model.Player;

@Getter
@Setter
@NoArgsConstructor
public class PlayerState {
    public Integer id;
    public String name;
    public Score score;
    public Game game;
    public Set set;

    PlayerState(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.score = new Score(0);
        this.game = Game.ZERO;
        this.set = Set.ZERO;
    }
}
