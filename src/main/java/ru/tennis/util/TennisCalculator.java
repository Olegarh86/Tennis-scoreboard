package ru.tennis.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TennisCalculator {
    public static int pageCountCalculate(Long totalItems, int pageSize) {
        return (int) Math.ceil(totalItems / (double) pageSize);
    }
}
