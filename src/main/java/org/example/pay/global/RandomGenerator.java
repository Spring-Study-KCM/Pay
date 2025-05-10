package org.example.pay.global;

import java.security.SecureRandom;


public class RandomGenerator {

    private static final int LENGTH = 4;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateSecureRandomString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            sb.append(SECURE_RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}
