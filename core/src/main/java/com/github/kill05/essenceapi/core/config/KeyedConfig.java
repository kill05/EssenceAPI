package com.github.kill05.essenceapi.core.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class KeyedConfig<C extends KeyedConfig<C>> extends ConfigFile {

    public KeyedConfig(JavaPlugin plugin, String localPath) {
        super(plugin, localPath, true);
    }

    public KeyedConfig(JavaPlugin plugin, File file) {
        super(plugin, file, true);
    }

    public <V> V getValue(ConfigKey<C, V> key, V def) {
        return key.getConfigValue(this, def);
    }

    public <V> V getValue(ConfigKey<C, V> key) {
        return key.getConfigValue(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected FileConfiguration generateDefault(FileConfiguration config) {
        List<ConfigKey<C, ?>> list = ConfigKey.getKeys(getClass());
        for(ConfigKey<C, ?> key : list) {
            config.set(key.getConfigPath(), key.getDefaultValue(this));
        }

        return config;
    }
}
