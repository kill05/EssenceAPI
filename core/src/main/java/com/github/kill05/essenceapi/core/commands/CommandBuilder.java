package com.github.kill05.essenceapi.core.commands;

import com.github.kill05.essenceapi.core.EssenceAPI;
import com.github.kill05.essenceapi.core.commands.arguments.CommandArgument;
import com.github.kill05.essenceapi.core.commands.executors.IExecutor;
import com.github.kill05.essenceapi.core.commands.executors.ConsoleExecutor;
import com.github.kill05.essenceapi.core.commands.executors.PlayerExecutor;
import com.github.kill05.essenceapi.core.commands.executors.SenderExecutor;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandBuilder {

    private final CommandController commandController;
    private final JavaPlugin plugin;
    private final String name;
    private final List<String> aliases = new ArrayList<>();
    private final List<CommandArgument> args = new ArrayList<>();
    private CommandArgument listArg;
    private IExecutor executor;
    private String description = "";
    private String usage = "";
    private String permission = "";
    private String permissionMessage = ChatColor.RED + "You don't have the required permissions to execute this command.";


    public CommandBuilder(JavaPlugin plugin, String commandName) {
        Validate.notNull(plugin, "Plugin can't be null.");
        Validate.notNull(commandName, "Command name can't be null.");
        this.plugin = plugin;
        this.commandController = EssenceAPI.getCommandController();
        this.name = commandName;
    }


    public CommandBuilder setDescription(String description){
        this.description = description;
        return this;
    }

    public CommandBuilder setUsage(String usage){
        this.usage = usage;
        return this;
    }

    public CommandBuilder setAliases(String... aliases){
        for(String alias : aliases) {
            this.aliases.add(alias.toLowerCase(Locale.ENGLISH));
        }
        return this;
    }

    public CommandBuilder setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public CommandBuilder setPermissionMessage(String permissionMessage) {
        this.permissionMessage = permissionMessage;
        return this;
    }


    public CommandBuilder appendArgument(CommandArgument argument){
        args.add(argument);
        return this;
    }

    public CommandBuilder setListArgument(CommandArgument argument){
        this.listArg = argument;
        return this;
    }


    public void executes(SenderExecutor executor){
        this.executor = executor;
        build();
    }

    public void executesPlayer(PlayerExecutor executor){
        this.executor = executor;
        build();
    }

    public void executesConsole(ConsoleExecutor executor){
        this.executor = executor;
        build();
    }


    private void build(){
        EssenceCommand command = new EssenceCommand(
                plugin, name, executor, args, listArg,
                aliases, description, usage,
                permission, permissionMessage);

        commandController.registerCommand(command);
    }

}
