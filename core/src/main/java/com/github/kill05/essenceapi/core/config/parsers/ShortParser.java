package com.github.kill05.essenceapi.core.config.parsers;

public class ShortParser implements IConfigParser<Short>{

    @Override
    public Short parse(Object obj) {
        return (obj instanceof Number) ? ((Number) obj).shortValue() : null;
    }

}
