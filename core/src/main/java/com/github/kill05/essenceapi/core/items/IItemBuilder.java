package com.github.kill05.essenceapi.core.items;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IItemBuilder {

    IItemBuilder setDurability(short durability);


    IItemBuilder setName(String name);


    IItemBuilder setLore(String... lore);

    IItemBuilder setLore(List<String> lore);

    IItemBuilder addLore(String... lines);

    IItemBuilder removeLore(int index);

    IItemBuilder deleteLore();


    IItemBuilder addEnchant(Enchantment enchantment, int level);

    IItemBuilder removeEnchant(Enchantment enchantment);


    IItemBuilder setUnbreakable(boolean unbreakable);


    IItemBuilder addItemFlags(ItemFlag... flags);

    IItemBuilder removeItemFlags(ItemFlag... flags);


    IItemBuilder setHeadOwner(String playerName);

    IItemBuilder setHeadTexture(String texture, String signature);


    ItemStack build();
}
