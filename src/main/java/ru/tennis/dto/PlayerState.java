package ru.tennis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tennis.gameState.Game;
import ru.tennis.gameState.Score;
import ru.tennis.gameState.GameSet;
import ru.tennis.model.Player;

@Getter
@NoArgsConstructor
public class PlayerState {
    private Integer id;
    private String name;
    @Setter
    private Score score;
    @Setter
    private Game game;
    @Setter
    private GameSet gameSet;

    PlayerState(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.score = new Score(0);
        this.game = Game.ZERO;
        this.gameSet = GameSet.ZERO;
    }
}
