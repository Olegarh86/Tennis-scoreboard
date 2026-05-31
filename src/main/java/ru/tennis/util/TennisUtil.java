package ru.tennis.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TennisUtil {

    // Более точным было бы название PaginationUtil

    // С аннотацией @UtilityClass можно не писать static в коде

    // Размер страницы по умолчанию более уместно хранить в сервлете, так как в идеале он должен приходить с фронтенда.
        // Этот класс должен принимать это значение в методы.

    // Имена `static final` констант в Java принято писать в `UPPER_SNAKE_CASE`.
    @Getter // Использование @Getter для константы не идиоматично. Лучше объявить поле как public static final.
    private static final int pageSize = 7;

    public static int pageCountCalculate(Long totalItems, int pageSize) {
        return (int) Math.ceil(totalItems / (double) pageSize);
    }

    public static int offsetCalculate(int page) {
        return  (page - 1) * pageSize;
    }
}
