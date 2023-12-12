package com.github.kill05.essenceapi.core.config.parsers;

public interface IConfigParser<T> {

    T parse(Object obj);
}
