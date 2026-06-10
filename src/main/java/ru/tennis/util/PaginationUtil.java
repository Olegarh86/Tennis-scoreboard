package ru.tennis.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PaginationUtil {

    public int pageCountCalculate(Long totalItems, int pageSize) {
        return (int) Math.ceil(totalItems / (double) pageSize);
    }

    public int offsetCalculate(int page, int pageSize) {
        return (page - 1) * pageSize;
    }
}
