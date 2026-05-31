package ru.tennis.dto;

import lombok.Getter;

@Getter
public class Winner extends PlayerState {

    // Класс является частью доменной модели, поэтому более уместным было бы разместить его в пакете 'model'.
        // (см. файл "model-types.md" в этом же пакете)

    // TODO: Класс наследуется от PlayerState, но не использует ничего из PlayerState, а объявляет свои собственные поля.
        // В результате создается пустой PlayerState с null-полями, а данные хранятся в полях самого Winner.
        // Наследование здесь некорректно. В текущей реализации этот класс мог быть простым, независимым классом или record.

    // TODO: Объекты классов FirstPlayer, SecondPlayer и Winner представляют одну и ту же сущность в теннисном матче — игрока.
        // Для этой задачи достаточно иметь один класс доменной модели игрока и давать переменным этого типа разные имена
        // в зависимости от того, данные какого конкретно игрока в них находятся.

    private final int winnerId;
    private final String winnerName;

    public Winner(int winnerId,  String winnerName) {
        this.winnerId = winnerId;
        this.winnerName = winnerName;
    }
}
