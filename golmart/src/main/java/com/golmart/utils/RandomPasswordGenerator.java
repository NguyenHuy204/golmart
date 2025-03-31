package com.golmart.utils;

import java.security.SecureRandom;

public class RandomPasswordGenerator {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()+|}{[]:;?><,./-=";
    private static final String PASSWORD_CHARS = CHAR_LOWER + CHAR_UPPER + NUMBER + SPECIAL_CHARS;


    public static String generateRandomPassword(int length) {
        if (length <= 4) {
            throw new IllegalArgumentException("Password length must be greater than 0.");
        }
        length = length - 4;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        int randomNumber = random.nextInt(NUMBER.length());
        int randomChartUpper = random.nextInt(CHAR_UPPER.length());
        int randomChartLower = random.nextInt(CHAR_LOWER.length());
        int randomChartSpecial = random.nextInt(SPECIAL_CHARS.length());
        sb.append(NUMBER.charAt(randomNumber));
        sb.append(CHAR_UPPER.charAt(randomChartUpper));
        sb.append(CHAR_LOWER.charAt(randomChartLower));
        sb.append(SPECIAL_CHARS.charAt(randomChartSpecial));
        for (int i = 0; i < length-4; i++) {
            int randomIndex = random.nextInt(PASSWORD_CHARS.length());
            char randomChar = PASSWORD_CHARS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
