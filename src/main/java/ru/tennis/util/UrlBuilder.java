package ru.tennis.util;

import lombok.experimental.UtilityClass;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
public class UrlBuilder {
    public String buildUrl(String path, String paramName, String paramValue) {
        String encodedParam = encode(paramValue, UTF_8);
        return path.concat("?").concat(paramName).concat("=").concat(encodedParam);
    }

    public String buildUrl(String path, String paramName1, String paramValue1, String paramName2, String paramValue2) {
        String encodedParam1 = encode(paramValue1, UTF_8);
        String encodedParam2 = encode(paramValue2, UTF_8);
        return path.concat("?")
                .concat(paramName1).concat("=").concat(encodedParam1).concat("&")
                .concat(paramName2).concat("=").concat(encodedParam2);
    }
}
