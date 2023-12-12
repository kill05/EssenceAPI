package com.github.kill05.essenceapi.core.config;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.logging.Level;

public class ConfigFile {

    protected final JavaPlugin plugin;
    protected final File file;
    protected final String defaultResourcePath;
    protected final boolean generateDefault;
    protected FileConfiguration config;


    public ConfigFile(JavaPlugin plugin, File file, String defaultResourcePath, boolean generateDefault) {
        this.plugin = plugin;
        this.file = file;
        this.defaultResourcePath = defaultResourcePath;
        this.generateDefault = generateDefault;
        reload();
    }

    public ConfigFile(JavaPlugin plugin, String localPath, String defaultResourcePath, boolean generateDefault) {
        this(plugin, new File(plugin.getDataFolder(), localPath), defaultResourcePath, generateDefault);
    }

    // From file
    public ConfigFile(JavaPlugin plugin, File file, String defaultResourcePath) {
        this(plugin, file, defaultResourcePath, false);
    }

    public ConfigFile(JavaPlugin plugin, File file, boolean generateDefault) {
        this(plugin, file, null, generateDefault);
    }

    public ConfigFile(JavaPlugin plugin, File file) {
        this(plugin, file, null);
    }


    // From local path relative to plugin folder
    public ConfigFile(JavaPlugin plugin, String localPath, String defaultResourcePath) {
        this(plugin, localPath, defaultResourcePath, false);
    }

    public ConfigFile(JavaPlugin plugin, String localPath, boolean generateDefault) {
        this(plugin, localPath, generateDefault ? null : localPath, generateDefault);
    }

    public ConfigFile(JavaPlugin plugin, String localPath) {
        this(plugin, localPath, localPath);
    }


    public void reload() {
        createDefault();
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save data to " + file, e);
        }
    }

    public void createDefault() {
        if(file.exists()) return;

        // Generate config from overridden method and then save to file
        if(generateDefault) {
            try {
                generateDefault(new YamlConfiguration()).save(file);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, String.format("Failed to save generated config file to %s.", file), e);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, String.format("Failed to generate config file located at %s.", file));
            }
            return;
        }

        // Create empty file in case default resource path is null
        if(defaultResourcePath == null) {
            try {
                FileUtils.forceMkdirParent(file);
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, String.format("Failed to create new empty config file at %s.", file), e);
            }
            return;
        }

        // Load from resources
        try {
            FileUtils.copyInputStreamToFile(plugin.getResource(defaultResourcePath), file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, String.format("Could not create default config file at %s.", file), e);
        }
    }


    protected FileConfiguration generateDefault(FileConfiguration config) {
        return config;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfig() {
        if(config == null) reload();
        return config;
    }
}
