package com.github.kill05.essenceapi.core.config.parsers;

public class ByteParser implements IConfigParser<Byte> {

    @Override
    public Byte parse(Object obj) {
        return (obj instanceof Number) ? ((Number) obj).byteValue() : null;
    }

}
