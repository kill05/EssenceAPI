package com.github.kill05.essenceapi.core.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Timespan {

    private static final Pattern PATTERN = Pattern.compile("([0-9]+)\\s*([a-z]+)");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    private long millis;

    public Timespan(long millis) {
        this.millis = millis;
    }

    public Timespan(String string) {
        Matcher matcher = PATTERN.matcher(string);

        while(matcher.find()) {
            int amount = Integer.parseInt(matcher.group(1));
            TimeUnit unit = TimeUnit.fromName(matcher.group(2));
            if(unit == null) continue;

            millis += unit.amount * amount;
        }
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    //todo: implement lowest value parameter
    public String toString(int maxUnitAmount, TimeUnit lowestValue) {
        StringBuilder builder = new StringBuilder();
        long remainingMillis = millis;

        int matches = 0;
        for(TimeUnit unit : TimeUnit.VALUES) {
            double amount = (double) remainingMillis / unit.amount;

            if(matches + 1 == maxUnitAmount) {
                DECIMAL_FORMAT.setMaximumFractionDigits(2);
            } else {
                amount = Math.floor(amount);
                DECIMAL_FORMAT.setMaximumFractionDigits(0);
            }

            if(amount < 1 && unit != lowestValue) continue;

            if(matches != 0) builder.append(' ');
            builder.append(DECIMAL_FORMAT.format(amount));
            builder.append(unit.name);
            remainingMillis -= Math.floor(amount) * unit.amount;

            if(++matches >= maxUnitAmount) break;
        }

        if(builder.length() == 0) builder.append('0').append(TimeUnit.MILLISECOND.name);
        return builder.toString();
    }

    @Override
    public String toString() {
        return toString(8, TimeUnit.MILLISECOND);
    }

    public enum TimeUnit {
        YEAR        (31_556_952_000L, "y"),
        MONTH       (2_629_746_000L,  "mo"),
        WEEK        (604_800_000L,    "w"),
        DAY         (86_400_000L,     "d"),
        HOUR        (3_600_000L,      "h"),
        MINUTE      (60_000L,         "m"),
        SECOND      (1000L,           "s"),
        TICK        (50L,             "t"),
        MILLISECOND (1L,              "ms");

        public static final TimeUnit[] VALUES = values();
        public static final Map<String, TimeUnit> NAME_MAP = new HashMap<>();
        private final long amount;
        private final String name;

        TimeUnit(long amount, String name) {
            this.amount = amount;
            this.name = name;
        }


        public static TimeUnit fromName(String name) {
            return NAME_MAP.get(name);
        }


        public long getAmount() {
            return amount;
        }

        public String getName() {
            return name;
        }

        static {
            for(TimeUnit unit : values()) {
                if(NAME_MAP.containsKey(unit.name)) throw new IllegalArgumentException("Duplicate time unit name: " + unit.name);
                NAME_MAP.put(unit.name, unit);
            }
        }
    }

}
