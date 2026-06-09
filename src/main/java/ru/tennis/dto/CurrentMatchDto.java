package ru.tennis.dto;

public record CurrentMatchDto(String firstPlayerName, String secondPlayerName, String firstPlayerMatch,
                              String secondPlayerMatch, String firstPlayerSet, String secondPlayerSet, String firstPlayerPoints, String secondPlayerPoints) {
}
