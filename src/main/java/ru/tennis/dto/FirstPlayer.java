package ru.tennis.dto;

import lombok.Getter;
import lombok.Setter;
import ru.tennis.model.Player;

@Getter
@Setter
public class FirstPlayer extends PlayerState {
    FirstPlayer(Player player) {
        super(player);
    }
}
