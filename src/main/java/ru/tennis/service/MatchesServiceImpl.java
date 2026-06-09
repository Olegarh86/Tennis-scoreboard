package ru.tennis.service;

import lombok.RequiredArgsConstructor;
import ru.tennis.dto.MatchDto;
import ru.tennis.dto.MatchPageDto;
import ru.tennis.entity.Match;
import ru.tennis.util.PaginationUtil;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MatchesServiceImpl implements MatchesService {
    private final FinishedMatchesPersistenceService persistenceService;

    public MatchPageDto getMatchPageDto(String playerName, int page, int pageSize) {
        int offset = PaginationUtil.offsetCalculate(page, pageSize);
        Long totalItems;
        List<MatchDto> matchesDto = new ArrayList<>();
        List<Match> allMatches;

        if (playerName.isEmpty()) {
            totalItems = persistenceService.countAll();
            allMatches = persistenceService.findAll(pageSize, offset);
            for (Match match : allMatches) {
                matchesDto.add(new MatchDto(match.getPlayer1().getName(), match.getPlayer2().getName(),
                        match.getWinner().getName()));
            }
        } else {
            totalItems = persistenceService.countAll(playerName);
            allMatches = persistenceService.findAll(playerName, pageSize, offset);
            for (Match match : allMatches) {
                matchesDto.add(new MatchDto(match.getPlayer1().getName(), match.getPlayer2().getName(),
                        match.getWinner().getName()));
            }
        }
        int pageCount = 1;
        if (totalItems == 0 && matchesDto.isEmpty()) {
            return new MatchPageDto(playerName, page, pageCount, matchesDto);
        }
        pageCount = PaginationUtil.pageCountCalculate(totalItems, pageSize);
        return new MatchPageDto(playerName, page, pageCount, matchesDto);
    }
}
