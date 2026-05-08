package ru.tennis.dto;

import ru.tennis.model.Match;

import java.util.List;

public record MatchesDto(String playerName, int page, int pageCount, List<Match> allMatches) {
}
