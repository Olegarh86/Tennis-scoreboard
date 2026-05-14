package ru.tennis.dto;

import lombok.Getter;
import lombok.Setter;
import ru.tennis.model.Player;

@Getter
@Setter
public class SecondPlayer extends PlayerState {
    SecondPlayer(Player player) {
        super(player);
    }
}
