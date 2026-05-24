package ru.tennis.context;

import lombok.Getter;
import ru.tennis.dao.TennisDao;
import ru.tennis.dao.TennisDaoImpl;
import ru.tennis.service.*;
import ru.tennis.util.NameNormalizer;
import ru.tennis.validation.PlayerNamesValidator;

@Getter
public class ApplicationContext {
    private final TennisDao dao = new TennisDaoImpl();
    private final NameNormalizer normalizer = new NameNormalizer();
    private final PlayerNamesValidator validator = new PlayerNamesValidator(normalizer);
    private final FinishedMatchesPersistenceService persistenceService = new FinishedMatchesPersistenceService(dao);
    private final OngoingMatchesService ongoingMatchesService = new OngoingMatchesService();
    private final CurrentMatchCreator currentMatchCreator = new CurrentMatchCreator(persistenceService, ongoingMatchesService);
    private final MatchScoreCalculationService calculationService = new MatchScoreCalculationService();
    private final MatchScoreService matchScoreService = new MatchScoreService(ongoingMatchesService,
            persistenceService, calculationService);
    private final MatchesService matchesService = new MatchesService(persistenceService);
}
