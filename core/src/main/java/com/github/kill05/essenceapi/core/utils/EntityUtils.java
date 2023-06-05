package com.github.kill05.essenceapi.core.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.HashSet;

public final class EntityUtils {

    public static void addEffect(LivingEntity entity, PotionEffectType type, int duration, int amplifier) {
        entity.addPotionEffect(new PotionEffect(type, duration, amplifier, false, false));
    }

    public static Collection<Entity> getNearbyEntities(Location l, int radius) {
        int chunkRadius = (int) Math.ceil(radius / 16.0);
        HashSet<Entity> radiusEntities = new HashSet<>();

        for (int chX = -chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = -chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
                        radiusEntities.add(e);
                }
            }
        }

        return radiusEntities;
    }

    public static void setHealth(LivingEntity entity, double amount){
        entity.setHealth(Math.min(Math.max(amount,0), entity.getMaxHealth()));
    }

    public static void addHealth(LivingEntity entity, double amount){
        setHealth(entity, entity.getHealth() + amount);
    }
}
