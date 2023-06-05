package com.github.kill05.essenceapi.core.commands.executors;

import org.bukkit.command.ConsoleCommandSender;

@FunctionalInterface
public interface ConsoleExecutor extends IExecutor {

    void execute(ConsoleCommandSender console, String[] args);

}
