package com.github.kill05.essenceapi.core.commands.executors;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PlayerExecutor extends IExecutor {

    void execute(Player player, String[] args);

}
