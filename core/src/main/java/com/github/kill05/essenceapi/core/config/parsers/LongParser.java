package com.github.kill05.essenceapi.core.config.parsers;

public class LongParser implements IConfigParser<Long> {

    @Override
    public Long parse(Object obj) {
        return (obj instanceof Number) ? ((Number) obj).longValue() : null;
    }

}
