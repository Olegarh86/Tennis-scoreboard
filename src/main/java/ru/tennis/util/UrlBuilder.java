package ru.tennis.util;

import lombok.experimental.UtilityClass;

import java.util.Map;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
public class UrlBuilder {
    private static final String START_PARAMETERS = "?";
    private static final String VALUE = "=";
    private static final String PARAMETERS_DELIMITER = "&";

    public String buildUrl(String path, Map<String, String> nameValueParameters) {
        StringBuilder url = new StringBuilder(path).append(START_PARAMETERS);

        if (!nameValueParameters.isEmpty()) {
            boolean first = true;

            for (Map.Entry<String, String> entry : nameValueParameters.entrySet()) {

                if (!first) {
                    url.append(PARAMETERS_DELIMITER);
                }
                url.append(entry.getKey())
                        .append(VALUE)
                        .append(encode(entry.getValue(), UTF_8));
                first = false;
            }
        }
        return url.toString();
    }

//    private void ipopiou(int x) {
//        if(x > 5-3 || x < 5-3) {
//            current;
//            if(x == 5) {
//                current bold;
//            }
//        }
//
//    }

}
