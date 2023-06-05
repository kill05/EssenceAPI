package com.github.kill05.essenceapi.core.commands;

import com.github.kill05.essenceapi.core.commands.arguments.CommandArgument;
import com.github.kill05.essenceapi.core.commands.executors.ConsoleExecutor;
import com.github.kill05.essenceapi.core.commands.executors.IExecutor;
import com.github.kill05.essenceapi.core.commands.executors.PlayerExecutor;
import com.github.kill05.essenceapi.core.commands.executors.SenderExecutor;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EssenceCommand extends Command {

    private final JavaPlugin plugin;
    private final String name;
    private final IExecutor executor;
    private final List<CommandArgument> expectedArguments;
    private final CommandArgument listArgument;
    private final String usageMessage;
    private final String permission;
    private final String permissionMessage;

    private final List<String> validatedArgs = new ArrayList<>();

    EssenceCommand(JavaPlugin plugin, String name, IExecutor executor, List<CommandArgument> expectedArguments, CommandArgument listArgument,
                   List<String> aliases, String description, String usageMessage, String permission, String permissionMessage) {
        super(name, description, usageMessage, aliases);
        this.plugin = plugin;
        this.name = name.toLowerCase(Locale.ENGLISH);
        this.executor = executor;
        this.expectedArguments = expectedArguments;
        this.listArgument = listArgument;
        this.usageMessage = usageMessage;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        validate();
    }

    private void validate() {
        Validate.notNull(name, "Name can't be null.");
        Validate.notNull(executor, "Executor can't be null.");

        /*
        boolean foundOptional = false;
        for(CommandArgument arg : expectedArguments) {
            if(arg.isOptional()){
                foundOptional = true;
                continue;
            }

            if(foundOptional && !arg.isOptional()) {
                throw new IllegalArgumentException("Found a non-optional argument after an optional one.");
            }
        }

         */

    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        this.tabComplete(sender, alias, args);

        if(!sender.hasPermission(this.permission)){
            sender.sendMessage(permissionMessage);
            return true;
        }

        if(executor instanceof PlayerExecutor && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return true;

        } else if(executor instanceof ConsoleExecutor && !(sender instanceof ConsoleCommandSender)){
            sender.sendMessage(ChatColor.RED + "This command can only be executed by the console.");
            return true;
        }


        if(!validateArgs(sender, args)) {
            sender.sendMessage("".equals(usageMessage) ? getDefaultUsageString(sender) : usageMessage);
            return true;
        }

        String[] validatedArgs = this.validatedArgs.toArray(new String[0]);
        if (executor instanceof PlayerExecutor) {
            Player player = (Player) sender;
            ((PlayerExecutor) executor).execute(player, validatedArgs);
            return true;
        }

        if (executor instanceof ConsoleExecutor) {
            ConsoleCommandSender console = (ConsoleCommandSender) sender;
            ((ConsoleExecutor) executor).execute(console, validatedArgs);
            return true;
        }

        ((SenderExecutor) executor).execute(sender, validatedArgs);
        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        if(!sender.hasPermission(permission)) return new ArrayList<>();
        if(args.length == 0) return new ArrayList<>();
        if(args.length > this.expectedArguments.size()) return new ArrayList<>();

        return StringUtil.copyPartialMatches(args[args.length - 1], this.expectedArguments.get(args.length - 1).getTabCompleter(), new ArrayList<>());
    }


    private boolean validateArgs(CommandSender sender, String[] providedArguments) {
        validatedArgs.clear();

        int argumentTypeAmount = this.expectedArguments.size();
        if(argumentTypeAmount < providedArguments.length && listArgument == null) return false;

        for(int j = 0; j < argumentTypeAmount; j++){
            CommandArgument superArg = this.expectedArguments.get(j);

            if(providedArguments.length <= j) {
                if(superArg.isOptional(sender)){
                    validatedArgs.add(superArg.getDefaultValue(sender));
                    continue;
                }

                return false;
            }

            if(superArg.isArgValid(providedArguments[j])) {
                validatedArgs.add(superArg.getValidatedArg(providedArguments[j]));
                continue;
            }

            return false;
        }

        if(listArgument == null) return true;

        // No provided arguments apply for the list argument
        if(expectedArguments.size() == providedArguments.length) return listArgument.isOptional(sender);
        for(int i = expectedArguments.size(); i < providedArguments.length; i++) {
            String validatedArgument = listArgument.getValidatedArg(providedArguments[i]);
            if(validatedArgument == null) return false;

            validatedArgs.add(validatedArgument);
        }

        return true;
    }

    private String getDefaultUsageString(CommandSender sender){
        StringBuilder usage = new StringBuilder(ChatColor.RED + "Usage: /" + this.name);
        for(CommandArgument arg : expectedArguments){
            boolean isOptional = arg.isOptional(sender);
            if (isOptional) {
                usage.append(" [<");
                usage.append(arg.getArgumentName());
                usage.append(">]");
            } else {
                usage.append(" <");
                usage.append(arg.getArgumentName());
                usage.append(">");
            }
        }

        if(listArgument != null){
            usage.append(" <");
            usage.append(listArgument.getArgumentName());
            usage.append("s>");
        }
        return usage.toString();
    }


    public JavaPlugin getPlugin() {
        return plugin;
    }

}