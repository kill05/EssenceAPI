package com.github.kill05.essenceapi.core.commands.arguments;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OfflinePlayerArgument extends CommandArgument{

    private final boolean executorDefault;

    public OfflinePlayerArgument(boolean executorDefault){
        super("offlineplayer", null);
        this.executorDefault = executorDefault;

        setTabCompleter(() -> {
            List<String> completers = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> completers.add(player.getName()));
            return completers;
        });
    }

    public OfflinePlayerArgument(){
        this(false);
    }

    @Override
    public boolean hasDefaultValue(CommandSender sender){
        if(sender instanceof Player) {
            return executorDefault;
        }
        return false;
    }

    @Override
    public String getDefaultValue(CommandSender sender){
        return sender.getName();
    }

    @Override
    @SuppressWarnings("deprecation")
    public String getValidatedArg(String arg) {
        OfflinePlayer p = Bukkit.getOfflinePlayer(arg);
        if(p != null) {
            return p.getName();
        }
        return null;
    }

}
