package com.github.kill05.essenceapi.core.config;

import com.github.kill05.essenceapi.core.config.parsers.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ConfigKey<C extends KeyedConfig<C>, V> {

    private static final Map<Class<? extends KeyedConfig<?>>, List<ConfigKey<?, ?>>> registeredKeys = new HashMap<>();
    private static final Map<Class<?>, IConfigParser<?>> parserMap = new HashMap<>();
    protected final Class<C> configClass;
    protected final Class<V> valueClass;
    protected final String configPath;
    protected final Function<KeyedConfig<C>, V> defaultValue;

    public ConfigKey(Class<C> configClass, Class<V> valueClass, String configPath, Function<KeyedConfig<C>, V> defaultValue) {
        this.configClass = configClass;
        this.valueClass = valueClass;
        this.configPath = configPath;
        this.defaultValue = defaultValue;
        addKey(configClass, this);
    }

    public ConfigKey(Class<C> configClass, Class<V> valueClass, String configPath, V defaultValue) {
        this(configClass, valueClass, configPath, (config) -> defaultValue);
    }

    public ConfigKey(Class<C> configClass, Class<V> valueClass, String configPath) {
        this(configClass, valueClass, configPath, (config) -> null);
    }

    private static <C extends KeyedConfig<C>> void addKey(Class<C> configClass, ConfigKey<C, ?> configKey) {
        if(configClass.equals(KeyedConfig.class)) return;
        getKeys(configClass).add(configKey);
    }

    @SuppressWarnings("unchecked")
    public static <T extends KeyedConfig<T>> List<ConfigKey<T, ?>> getKeys(Class<T> configClass) {
        registeredKeys.putIfAbsent(configClass, new ArrayList<>());
        return (List<ConfigKey<T, ?>>) (List<?>) registeredKeys.get(configClass);
    }

    public static <P> void registerParser(Class<P> clazz, IConfigParser<P> parser) {
        if(parserMap.containsKey(clazz)) throw new IllegalArgumentException(String.format("A parser for %s is already registered!", clazz));
        if(parserMap.containsValue(parser)) throw new IllegalArgumentException(String.format("The parser %s is already registered!", parser.getClass()));
        parserMap.put(clazz, parser);
    }

    @SuppressWarnings("unchecked")
    public <P extends IConfigParser<V>> P getParser() {
        return (P) parserMap.get(valueClass);
    }

    public Class<C> getConfigClass() {
        return configClass;
    }

    public Class<V> getValueClass() {
        return valueClass;
    }

    public String getConfigPath() {
        return configPath;
    }

    public V getDefaultValue(KeyedConfig<C> config) {
        return defaultValue.apply(config);
    }

    public V getConfigValue(KeyedConfig<C> config) {
        return getConfigValue(config, getDefaultValue(config));
    }

    public V getConfigValue(KeyedConfig<C> config, V defaultValue) {
        if(!getConfigClass().isAssignableFrom(config.getClass())) {
            throw new IllegalArgumentException(String.format("Config key %s is not compatible with %s.", this, config.getClass()));
        }

        FileConfiguration fileCfg = config.getConfig();
        IConfigParser<V> parser = getParser();
        Object value = fileCfg.get(configPath, defaultValue);

        if(value == null) {
            return defaultValue;
        }

        try {
            return valueClass.cast(value);
        } catch (ClassCastException e) {
            V parsed = parser.parse(value);
            return parsed != null ? parsed : defaultValue;
        }

    }


    @Override
    public String toString() {
        return "ConfigKey{" +
                "configClass=" + configClass +
                ", valueClass=" + valueClass +
                ", configPath='" + configPath + '\'' +
                ", defaultValue=" + defaultValue +
                '}';
    }

    static {
        registerParser(Byte.class, new ByteParser());
        registerParser(Short.class, new ShortParser());
        registerParser(Integer.class, new IntegerParser());
        registerParser(Long.class, new LongParser());
        registerParser(Float.class, new FloatParser());
        registerParser(Double.class, new DoubleParser());

        registerParser(Material.class, new MaterialParser());
    }

}
