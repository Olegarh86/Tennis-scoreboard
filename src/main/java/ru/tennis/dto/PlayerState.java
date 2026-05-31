package ru.tennis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tennis.gameState.Game;
import ru.tennis.gameState.Score;
import ru.tennis.gameState.GameSet;
import ru.tennis.model.Player;

@Getter
@NoArgsConstructor // Не нужен, так как позволяет создать объект в некорректном состоянии.
public class PlayerState {

    // Класс является доменной моделью, поэтому более уместным было бы разместить его в пакете 'model'.
        // (см. файл "model-types.md" в этом же пакете)

    // Более понятным было бы название TennisPlayer

    // TODO: Класс знает о JPA-сущности (принимает в конструктор и разбирает на части объект `Player`).
        // Это создаёт прямую зависимость доменного слоя от слоя персистентности (долговременного хранения данных)
        // и смешивает слои приложения, что нарушает чистоту архитектуры.
        // Доменные модели должны оперировать другими доменными моделями, а не сущностями, привязанными к базе данных.

    // ID игрока в этом объекте — это ID из БД. Внутренняя информация из БД не должна "протекать" в доменные модели.
        // Вместо этого можно использовать ID 1 и 2 для первого и второго игрока соответственно, или просто их имена.
    private Integer id;
    private String name;

    @Setter // TODO: Сеттер позволяет бесконтрольно изменить состояние объекта
    private Score score;

    @Setter // TODO: Сеттер позволяет бесконтрольно изменить состояние объекта
    private Game game;

    @Setter // TODO: Сеттер позволяет бесконтрольно изменить состояние объекта
    private GameSet gameSet;

    // Отсутствует явный модификатор доступа
    // Все "магические" значения лучше вынести в именованные константы
    PlayerState(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.score = new Score(0);
        this.game = Game.ZERO;
        this.gameSet = GameSet.ZERO;
    }
}
