package com.github.kill05.essenceapi.core.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Random;
import java.util.stream.IntStream;

public final class NumberUtils {

    public static final Random DEFAULT_RANDOM = new Random();
    private static final int[] romanValues = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] romanLiterals = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    private static final String[] numberLiterals = {"", "k", "M", "B", "t", "q", "Q"};

    private NumberUtils() {

    }

    public static boolean isInteger(Object obj) {
        if(obj instanceof Integer) return true;

        try {
            Integer.parseInt(String.valueOf(obj));
            return true;
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(Object obj) {
        if(obj instanceof Double) return true;

        try {
            Double.parseDouble(String.valueOf(obj));
            return true;
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
    }


    public static int randInt(int min, int max, Random random) {
        return random.nextInt((max - min) + 1) + min;
    }

    public static int randInt(int min, int max) {
        return randInt(min, max, DEFAULT_RANDOM);
    }

    public static long randLong(long min, long max, Random random) {
        return random.nextLong((max - min) + 1L) + min;
    }

    public static long randLong(long min, long max) {
        return randLong(min, max, DEFAULT_RANDOM);
    }

    public static double randDouble(double min, double max, Random random) {
        return min + (max - min) * random.nextDouble();
    }

    public static double randDouble(double min, double max) {
        return randDouble(min, max, DEFAULT_RANDOM);
    }

    public static float randFloat(float min, float max, Random random) {
        return min + (max - min) * random.nextFloat();
    }

    public static float randFloat(float min, float max) {
        return randFloat(min, max, DEFAULT_RANDOM);
    }


    public static int[] integersBetween(int start, int end) {
        return IntStream.range(start, end).toArray();
    }

    public static String romanFormat(int num) {
        StringBuilder roman = new StringBuilder();

        for(int i = 0; i < romanValues.length; i++) {
            while(num >= romanValues[i]) {
                num -= romanValues[i];
                roman.append(romanLiterals[i]);
            }
        }

        return roman.toString();
    }

    public static String letterFormat(double value) {
        int index = 0;
        while ((value / 1000) >= 1) {
            value = value / 1000;
            index++;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.US));
        return String.format("%s%s", decimalFormat.format(value), numberLiterals[index]);
    }

    public static String commaFormat(double value) {
        return String.format(Locale.ENGLISH, "%,.02f", value);
    }

    public static String commaFormat(int value) {
        return NumberUtils.commaFormat((long) value);
    }

    public static String commaFormat(long value) {
        return String.format(Locale.ITALIAN, "%,d", value);
    }

}
