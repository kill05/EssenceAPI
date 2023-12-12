package com.github.kill05.essenceapi.core.config.parsers;

public class FloatParser implements IConfigParser<Float> {

    @Override
    public Float parse(Object obj) {
        return (obj instanceof Number) ? ((Number) obj).floatValue() : null;
    }

}
