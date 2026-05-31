package ru.tennis.dto;

import ru.tennis.model.Match;

import java.util.List;

public record MatchesDto(String playerName, int page, int pageCount, List<Match> allMatches, boolean needsRedirect) {

    // Можно назвать MatchPageDto

    // TODO: Класс содержит JPA Entity (`List<Match> allMatches`). Это смешивает слои.
        // Все поля в DTO тоже должны быть DTO или примитивными/простыми типами.
        // (см. файл "model-types.md" в этом же пакете)

    // Значение `boolean needsRedirect` не нужно для JSP страниц, а значит и не должно быть в этом DTO.
        // Знать о том, когда выполнять редирект — это обязанность сервлета.

}
