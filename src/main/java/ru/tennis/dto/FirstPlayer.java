package ru.tennis.dto;

import lombok.Getter;
import ru.tennis.model.Player;

@Getter
public class FirstPlayer extends PlayerState {
    public FirstPlayer(Player player) {
        super(player);
    }
}
