package com.github.kill05.essenceapi.core.config.parsers;

public class IntegerParser implements IConfigParser<Integer> {

    @Override
    public Integer parse(Object obj) {
        return (obj instanceof Number) ? ((Number) obj).intValue() : null;
    }

}
