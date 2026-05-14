package ru.tennis.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TennisUtil {
    @Getter
    private static final int pageSize = 7;
    public static int pageCountCalculate(Long totalItems, int pageSize) {
        return (int) Math.ceil(totalItems / (double) pageSize);
    }

    public static int offsetCalculate(int page) {
        return  (page - 1) * pageSize;
    }

    public static int parsePage(String pageNumber) {
        int page = 1;

        if (!pageNumber.isEmpty()) {
            page = Integer.parseInt(pageNumber);
        }

        if (page < 1) {
            page = 1;
        }
        return page;
    }
}
