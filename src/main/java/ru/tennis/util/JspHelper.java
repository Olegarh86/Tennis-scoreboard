package ru.tennis.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JspHelper {
    private final static String MASK = "/WEB-INF/jsp/%s.jsp";

    public static String getPath(String jspName) {
        return String.format(JspHelper.MASK, jspName);
    }
}
