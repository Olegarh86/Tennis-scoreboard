package ru.tennis.dto;

import lombok.Getter;
import ru.tennis.model.Player;

@Getter
public class SecondPlayer extends PlayerState {

    // Класс является частью доменной модели, поэтому более уместным было бы разместить его в пакете 'model'.
        // (см. файл "model-types.md" в этом же пакете)

    // TODO: Классы FirstPlayer и SecondPlayer не имеют собственной ответственности — это просто классы-маркеры.
        // Они наследуются от PlayerState и не добавляют никакой новой логики или полей. Их единственная цель —
        // позволить создать в классе CurrentMatch поля с разными типами: FirstPlayer firstPlayer и SecondPlayer secondPlayer.
        // Это избыточное и запутывающее использование наследования. Проще и понятнее было бы иметь в CurrentMatch
        // два поля одного типа: PlayerState firstPlayerState и PlayerState secondPlayerState.

    // TODO: Объекты классов FirstPlayer, SecondPlayer и Winner представляют одну и ту же сущность в теннисном матче — игрока.
        // Для этой задачи достаточно иметь один класс доменной модели игрока и давать переменным этого типа разные имена
        // в зависимости от того, данные какого конкретно игрока в них находятся.

    // Отсутствует явный модификатор доступа
    SecondPlayer(Player player) {
        super(player);
    }
}
