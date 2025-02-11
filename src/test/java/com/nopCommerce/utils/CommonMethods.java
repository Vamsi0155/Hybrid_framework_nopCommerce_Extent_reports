package com.nopCommerce.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Map;

public class CommonMethods {

    public static Map<String, String> parseGivenInput(Map<String, String> input) {

        for (Map.Entry<String, String> entry : input.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if(value.contains("Password#")) {
                value = RandomStringUtils.randomAlphabetic(5, 8) + "@" + RandomStringUtils.randomNumeric(2, 4);
            }
            input.put(key, value);
        }
        return input;
    }
}
