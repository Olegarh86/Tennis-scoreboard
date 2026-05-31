package ru.tennis.gameState;

public enum Game {

    // Класс называется Game, но отвечает за счёт в сете. Более понятным было бы название TennisSet.

    // TODO: Этому классу лучше не быть перечислением, достаточно хранить значение в переменной int.

    // TODO: Класс является анемичной моделью — он является лишь контейнером для данных, а вся значимая логика находится в сервисном слое.
        // Если бы у класса вместо простых сеттеров были методы, совершающие необходимую работу над полями,
        // это больше соответствовало бы ООП стилю и обязанности класса (в роли доменной модели).
        // Также, эту часть логики было бы легче тестировать.
        // (см. файл "reach-anemic-model.md" в этом же пакете)

    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7);

    private final Integer points;

    Game(Integer points) {
        this.points = points;
    }

    public Game next() {
        return switch (this) {
            case ZERO -> ONE;
            case ONE -> TWO;
            case TWO -> THREE;
            case THREE -> FOUR;
            case FOUR -> FIVE;
            case FIVE -> SIX;
            case SIX -> SEVEN;
            default -> ZERO; // В перечислении по умолчанию не должно тихо возвращаться корректное значение.
                                // Здесь должно выбрасываться исключение, чтобы клиентский код смог понять,
                                // что что-то пошло по не предусмотренному сценарию.
        };
    }

    // Нет аннотации @Override
    // Метод toString() лучше оставить для отладки и логирования, а для получения строкового значения — создать другой.
    public String toString(){
        return this.points.toString();
    }

    // Можно использовать @Getter
    public int getValue(){
        return points;
    }
}
