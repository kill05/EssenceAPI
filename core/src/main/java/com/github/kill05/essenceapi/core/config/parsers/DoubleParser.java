package com.github.kill05.essenceapi.core.config.parsers;

public class DoubleParser implements IConfigParser<Double> {

    @Override
    public Double parse(Object obj) {
        return (obj instanceof Number) ? ((Number) obj).doubleValue() : null;
    }

}
