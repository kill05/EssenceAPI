package com.github.kill05.essenceapi.core.commands.executors;

import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface SenderExecutor extends IExecutor {

    void execute(CommandSender sender, String[] args);
}
