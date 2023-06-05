package com.github.kill05.essenceapi.core.utils;

import lombok.SneakyThrows;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public final class EssenceUtils {

    private static final Random rnd = new Random();
    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String formatColor(String string) {

        return ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('§', string));
    }

/*
    public static String hexColor(String message) {
        Matcher matcher = hexPattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, "" + net.md_5.bungee.api.ChatColor.of(color));
            matcher = hexPattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
 */

    public static String toProperCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public static String toStrictProperCase(String s){
        return toProperCase(s.toLowerCase(Locale.ENGLISH));
    }


    public static String generateRandom(int length) {
        byte[] array = new byte[length];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    public static String generateAlphanumeric(int length){
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }


    public static <T extends Enum<?>> T searchEnum(Class<T> enumeration, String search) {
        if(enumeration == null || search == null) return null;

        for (T each : enumeration.getEnumConstants()) {
            if (each.name().compareToIgnoreCase(search) == 0) return each;
        }
        return null;
    }

    @SneakyThrows
    public static int getCurrentTick() {
        return (int) FieldUtils.readDeclaredField(Bukkit.getScheduler(), "currentTick", true);
    }

}
