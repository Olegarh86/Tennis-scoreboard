package ru.tennis;

import lombok.Getter;
import lombok.Setter;
import ru.tennis.model.Player;

@Getter
@Setter
public class SecondPlayer extends PlayerOrder {

    SecondPlayer(Player player) {
        super(player);
    }
}
