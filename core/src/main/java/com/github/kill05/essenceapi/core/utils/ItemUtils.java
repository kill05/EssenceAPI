package com.github.kill05.essenceapi.core.utils;

import lombok.SneakyThrows;
import org.apache.commons.lang.reflect.MethodUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemUtils {

    public static boolean isAir(Material material) {
        return material == null || material == Material.AIR;
    }

    public static boolean isAir(ItemStack item) {
        return item == null || isAir(item.getType());
    }

    public static boolean isNotAir(Material material) {
        return !isAir(material);
    }

    public static boolean isNotAir(ItemStack item) {
        return !isAir(item);
    }

    public static void safeGive(Player player, ItemStack... items) {
        for(ItemStack item : player.getInventory().addItem(items).values()){
            player.sendMessage(ChatColor.RED + "Your inventory was full, so some items were dropped on the ground");
            player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 0.5f, 1f);

            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
    }

    @SneakyThrows
    public static void setUnbreakable(ItemMeta meta, boolean unbreakable) {
        try {
            meta.spigot().setUnbreakable(unbreakable);
        } catch (NoSuchMethodError e) {
            MethodUtils.invokeMethod(meta, "setUnbreakable", unbreakable);
        }
    }

}
