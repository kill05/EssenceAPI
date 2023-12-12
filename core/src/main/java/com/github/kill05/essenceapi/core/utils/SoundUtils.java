package com.github.kill05.essenceapi.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class SoundUtils {

    public static void playSound(Sound sound, float volume, float pitch) {
        playSound(sound, volume, pitch, Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }

    public static void playSound(Sound sound, float volume, float pitch, Entity entity) {
        playSound(sound, volume, pitch, new Entity[]{entity});
    }

    public static void playSound(Sound sound, float volume, float pitch, Entity... entities) {
        for(Entity entity : entities) {
            if (!(entity instanceof Player)) continue;
            Player player = (Player) entity;
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public static void playSound(Sound sound, float volume, float pitch, Location loc) {
        loc.getWorld().playSound(loc, sound, volume, pitch);
    }

}
