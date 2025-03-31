package com.golmart.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageUtils {
    public static String getMessage(String code, Locale locale) {
        if (ObjectUtils.isEmpty(locale)) {
            locale = Locale.forLanguageTag("vi");
        }
        ResourceBundle bundle = ResourceBundle.getBundle("message/messages", locale);
        return bundle.getString(code);
    }

    public static String getMessage(String code) {
        Locale locale = Locale.forLanguageTag("vi");
        ResourceBundle bundle = ResourceBundle.getBundle("message/messages", locale);
        return bundle.getString(code);
    }

    public static String getMessage(String code, Object... arg) {
        Locale locale = Locale.forLanguageTag("vi");
        ResourceBundle bundle = ResourceBundle.getBundle("message/messages", locale);
        String message = bundle.getString(code);
        return String.format(message, arg);
    }
}
