package ru.tennis.dto;

import ru.tennis.CurrentMatch;
import ru.tennis.model.Match;

import java.util.List;

public record MatchScoreDto(CurrentMatch  currentMatch, int currentPage, int pageCount, List<Match> allFinishedMatches) {
}
