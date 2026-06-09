package ru.tennis.dto;

import java.util.List;

public record MatchPageDto(String playerName, int page, int pageCount, List<MatchDto> allMatches) {
}
