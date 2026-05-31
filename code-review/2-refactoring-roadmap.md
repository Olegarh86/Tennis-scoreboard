# Роадмап рефакторинга по файлам

Это упорядоченный список файлов, которые следует исправлять в соответствии с замечаниями в комментариях. Рекомендую двигаться последовательно.

Файлы, не указанные в списке, можно исправлять в любом порядке.

### Шаг 1: Сущности

- `/model/Player.java`
- `/model/Match.java`

### Шаг 2: Слой доступа к данным (DAO/Repositories)

- `/dao/TennisDao.java`
- `/dao/TennisDaoImpl.java`

### Шаг 3: Доменные модели

- `/gameState/Score.java`
- `/gameState/TieBreak.java`
- `/gameState/Game.java`
- `/gameState/GameSet.java`
- `/dto/PlayerState.java`
- `/dto/FirstPlayer.java`
- `/dto/SecondPlayer.java`
- `/dto/Winner.java`
- `/dto/CurrentMatch.java`
- `/service/MatchScoreCalculationService.java`

### Шаг 4: Сервисный слой

- `/service/MatchScoreService.java`
- `/service/CurrentMatchCreator.java`
- `/service/OngoingMatchesService.java`
- `/service/MatchesService.java`
- `/service/FinishedMatchesPersistenceService.java`

### Шаг 5: Утилиты и "Ручной DI-контейнер"

- `/util/HibernateUtil.java`
- `/context/ApplicationContext.java`
- `/util/UrlBuilder.java`
- `/util/TennisUtil.java`
- `/util/NameNormalizer.java`
- `/dto/ValidationResult.java`

### Шаг 6: Сервлеты

- `/servlet/NewMatchServlet.java`
- `/servlet/MatchScoreServlet.java`
- `/servlet/MatchesServlet.java`
- `/servlet/ErrorServlet.java`

### Шаг 7: Слой представления (JSP) и Тесты

- `src/main/webapp/WEB-INF/jsp/new-match.jsp`
- `src/main/webapp/WEB-INF/jsp/matches.jsp`
- `src/test/java/MatchScoreCalculationTest.java`

