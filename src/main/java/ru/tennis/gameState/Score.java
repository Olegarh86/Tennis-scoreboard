package ru.tennis.gameState;

import lombok.Getter;

@Getter
public class Score {

    // Можно назвать GameScore, т.к. просто счёт (Score) есть у каждого "уровня" теннисного матча,
        // а 0-15-30-... соответствует счёту именно в гейме.

    private final int score;

    // Можно использовать @RequiredArgsConstructor
    public Score(int score) {
        this.score = score;
    }

    // Нет необходимости каждый раз в этом методе создавать новые объекты Score.
        // Этот класс может быть перечислением с константами LOVE, FIFTEEN, THIRTY, FORTY, ADVANTAGE.
    public Score next() {
        return switch (score) {
            case 0 -> new Score(15);
            case 15 -> new Score(30);
            case 30 -> new Score(40);
            case 40 -> new Score(50); // Счёта 50 нет в теннисном гейме. После счёта 40 идёт преимущество — AD.
            case 50 -> new Score(51); // Счёта 50 и 51 нет в теннисном гейме.
            default -> new Score(-1); // Даже в текущей реализации (которая хранит счёт в int, а не в enum)
                                            // здесь нужно бросать исключение, а не возвращать корректное
                                            // для переменной типа int значение
        };
    }

    // Геттер для поля score уже генерируется аннотацией @Getter
    public int getValue() {
        return score;
    }

    // Метод toString() лучше оставить для отладки и логирования, а для получения строкового значения — создать другой.
    @Override
    public String toString() {
        if (score == 50) {
            return "AD";
        }
        return String.valueOf(score);
    }
}
