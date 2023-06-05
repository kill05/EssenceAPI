package com.github.kill05.essenceapi.plugin;

import com.github.kill05.essenceapi.core.EssenceAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class EssencePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        EssenceAPI.staticEnable(this);
    }

    @Override
    public void onDisable() {
        EssenceAPI.staticDisable();
    }


    public static EssencePlugin getPlugin() {
        return getPlugin(EssencePlugin.class);
    }
}
