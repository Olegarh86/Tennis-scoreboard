package ru.tennis.service;

import ru.tennis.dto.MatchPageDto;

public interface MatchesService {

    MatchPageDto getMatchPageDto(String playerName, int page, int pageSize);
}
