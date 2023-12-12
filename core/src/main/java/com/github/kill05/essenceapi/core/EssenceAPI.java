package com.github.kill05.essenceapi.core;

import com.github.kill05.essenceapi.core.commands.CommandRegistry;
import com.github.kill05.essenceapi.core.gui.GuiController;
import com.github.kill05.essenceapi.core.modules.ModuleLoader;
import com.github.kill05.essenceapi.core.modules.packets.PacketModule;
import com.github.kill05.essenceapi.core.nms.NMSHelper;
import com.github.kill05.essenceapi.core.nms.packets.PacketListener;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

public final class EssenceAPI {

    private static EssenceAPI instance = new EssenceAPI();

    private JavaPlugin plugin;
    private PacketListener packetListener;
    private CommandRegistry commandRegistry;
    private GuiController guiController;

    private EssenceAPI() {

    }


    public static void staticEnable(JavaPlugin plugin) {
        (instance = new EssenceAPI()).enable(plugin);
    }

    public static void staticDisable() {
        instance.disable();
        instance = null;
    }

    
    @SuppressWarnings("deprecation")
    public void enable(JavaPlugin plugin) {
        Validate.notNull(plugin, "Plugin can't be null!");
        if(this.plugin != null) throw new IllegalStateException("EssenceAPI has already been enabled! This is a bug!");
        if(plugin != Bukkit.getPluginManager().getPlugin("EssenceAPI")) throw new IllegalArgumentException("EssencePlugin instance required!");

        System.out.println("Enabling EssenceAPI...");
        this.plugin = plugin;

        //hook NMS implementations
        hookNMS();

        //init modules if nms hooks were found
        if(ModuleLoader.isLoaded(PacketModule.class)) {
            Bukkit.getPluginManager().registerEvents(this.packetListener = new PacketListener(), plugin);
            NMSHelper.setPacketModule(ModuleLoader.getModule(PacketModule.class));
            packetListener.injectPlayers();
        }

        //register events
        Arrays.asList(
                this.commandRegistry = new CommandRegistry(),
                this.guiController = new GuiController()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, plugin));

        System.out.println("Successfully enabled EssenceAPI!");
    }
    
    public void disable() {
        guiController.close();
        commandRegistry.close();

        if(ModuleLoader.isLoaded(PacketModule.class)) {
            packetListener.uninjectPlayers();
        }
    }


    private void hookNMS() {
        EssenceAPI.log(Level.INFO, String.format("Loading implementations for NMS version %s...", NMSHelper.getVersion()));
        ModuleLoader.load(PacketModule.class);
    }

    public static void log(Level level, String message, Throwable thrown) {
        getPlugin().getLogger().log(level, message, thrown);
    }

    public static void log(Level level, String message) {
        getPlugin().getLogger().log(level, message);
    }

    public static JavaPlugin getPlugin() {
        return instance.plugin;
    }

    public static PacketListener getPacketListener() {
        checkEnabled(PacketModule.class);
        return instance.packetListener;
    }

    public static CommandRegistry getCommandController() {
        checkEnabled();
        return instance.commandRegistry;
    }

    public static GuiController getGuiController() {
        checkEnabled();
        return instance.guiController;
    }


    private static void checkEnabled() {
        if(instance == null || !instance.isEnabled()) throw new IllegalStateException("EssenceAPI is not enabled! This is a bug!");
    }

    private static void checkEnabled(Class<?> clazz) {
        checkEnabled();
        ModuleLoader.checkLoaded(clazz);
    }


    public boolean isEnabled() {
        return plugin != null && plugin.isEnabled();
    }
}
