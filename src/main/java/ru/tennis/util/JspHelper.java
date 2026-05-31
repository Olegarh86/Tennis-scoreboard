package ru.tennis.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JspHelper {

    // С аннотацией @UtilityClass можно не писать static в коде

    // Более понятным было бы название PATH_TEMPLATE
    private final static String MASK = "/WEB-INF/jsp/%s.jsp";

    public static String getPath(String jspName) {

        // Внутри класса нет необходимости писать JspHelper.MASK — достаточно просто MASK
        return String.format(JspHelper.MASK, jspName);
    }
}
