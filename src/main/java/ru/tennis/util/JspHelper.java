package ru.tennis.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JspHelper {
    private final static String PATH_TEMPLATE = "/WEB-INF/jsp/%s.jsp";

    public String getPath(String jspName) {
        return String.format(PATH_TEMPLATE, jspName);
    }
}
