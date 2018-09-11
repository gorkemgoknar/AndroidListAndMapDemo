package com.example.gorkemgoknar.carmapdemo.utility;

import java.security.SecureRandom;
import java.util.Random;

/*
  Random string and coordinate generator for testing
 */
public class RandomGenerator {

    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generateRandomString(int length) {
        Random random = new SecureRandom();
        if (length <= 0) {
            throw new IllegalArgumentException("String length must be a positive integer");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
    }


    public static Double[] generateRandomCoordinate() {
        Random random = new SecureRandom();

        return new Double[]{random.nextDouble(), random.nextDouble()};

            }

}
