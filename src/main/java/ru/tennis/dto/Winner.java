package ru.tennis.dto;

import lombok.Getter;

@Getter
public class Winner extends PlayerState {
    private final int winnerId;
    private final String winnerName;

    public Winner(int winnerId,  String winnerName) {
        this.winnerId = winnerId;
        this.winnerName = winnerName;
    }
}
