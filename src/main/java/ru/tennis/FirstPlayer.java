package ru.tennis;

import lombok.Getter;
import lombok.Setter;
import ru.tennis.model.Player;

@Getter
@Setter
public class FirstPlayer extends PlayerOrder {

    FirstPlayer(Player player) {
        super(player);
    }
}
