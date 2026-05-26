package ru.tennis.dto;

import lombok.Getter;
import ru.tennis.model.Player;

@Getter
public class SecondPlayer extends PlayerState {
    SecondPlayer(Player player) {
        super(player);
    }
}
