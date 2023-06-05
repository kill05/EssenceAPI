package com.github.kill05.essenceapi.core.commands;

import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class CommandController implements Listener {

    CommandMap bukkitCommandMap;
    private final Map<Plugin, List<EssenceCommand>> commandMap = new HashMap<>();
    //String is command name
    private final Map<String, EssenceCommand> knownCommands;


    @SuppressWarnings({"unchecked"})
    public CommandController() {
        try {
            this.bukkitCommandMap = (CommandMap) FieldUtils.readField(Bukkit.getServer(), "commandMap", true);
            this.knownCommands = (Map<String, EssenceCommand>) FieldUtils.readField(bukkitCommandMap, "knownCommands", true);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }


    @EventHandler
    public void onPluginDisable(PluginDisableEvent event){
        unregisterCommands(event.getPlugin());
    }


    public void registerCommand(EssenceCommand command) {
        JavaPlugin plugin = command.getPlugin();
        commandMap.putIfAbsent(plugin, new ArrayList<>());
        commandMap.get(plugin).add(command);

        bukkitCommandMap.register(plugin.getName(), command);
    }


    /**
     * Unregisters a specific command.
     * @param command the EssenceCommand to be removed.
     */
    public void unregisterCommand(EssenceCommand command) {
        JavaPlugin plugin = command.getPlugin();
        commandMap.getOrDefault(plugin, new ArrayList<>()).remove(command);
        if(commandMap.getOrDefault(plugin, new ArrayList<>()).size() == 0) commandMap.remove(plugin);

        knownCommands.remove(plugin.getName().toLowerCase(Locale.ENGLISH) + ":" + command.getName());
        knownCommands.remove(command.getName());
        for (String alias : command.getAliases()) {
            knownCommands.remove(plugin.getName().toLowerCase(Locale.ENGLISH) + ":" + alias);
            knownCommands.remove(alias);
        }

    }

    /**
     * Unregisters all essenceAPI commands correlated with a plugin.
     *
     * @param plugin the plugin that should have all essenceAPI commands unregistered.
     */
    public void unregisterCommands(Plugin plugin) {
        for(EssenceCommand executor : new ArrayList<>(commandMap.getOrDefault(plugin, new ArrayList<>()))){
            unregisterCommand(executor);
        }
    }

    /**
     * Unregisters all essenceAPI commands.
     * This method is only meant for internal usage and should not be called by other plugins.
     */
    @Deprecated
    public void unregisterCommands() {
        for(Plugin plugin : new ArrayList<>(commandMap.keySet())){
            unregisterCommands(plugin);
        }
    }

}
