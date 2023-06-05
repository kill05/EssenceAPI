package com.github.kill05.essenceapi.core.commands.arguments;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerArgument extends CommandArgument{

    private final boolean executorDefault;

    public PlayerArgument(boolean executorDefault){
        super("player", null);
        this.executorDefault = executorDefault;
        setTabCompleter(() -> {
            List<String> completers = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> completers.add(player.getName()));
            return completers;
        });
    }

    public PlayerArgument(){
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
    public String getValidatedArg(String arg) {
        Player p = Bukkit.getPlayer(arg);
        if(p != null) {
            return p.getName();
        }
        return null;
    }

}
