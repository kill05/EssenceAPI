package com.github.kill05.essenceapi.core.items;

import com.github.kill05.essenceapi.core.utils.EssenceUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ItemBuilder implements IItemBuilder {

    private final ItemStack item;
    private ItemMeta meta;

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }



    public static ItemBuilder namedItem(Material material, int amount, String name) {
        return new ItemBuilder(material, amount).setName(name);
    }

    public static ItemBuilder namedItem(Material material, String name) {
        return new ItemBuilder(material).setName(name);
    }

    public static ItemBuilder headItem(String texture) {
        Material material = EnumUtils.getEnum(Material.class, "PLAYER_HEAD", EnumUtils.getEnum(Material.class, "SKULL_ITEM"));
        ItemBuilder builder = new ItemBuilder(material);
        if(material.name().equals("SKULL_ITEM")) builder.setDurability((short) 3);

        return builder.setHeadTexture(texture, null);
    }


    @Override
    public ItemBuilder setDurability(short durability) {
        item.setDurability(durability);
        return this;
    }

    @Override
    public ItemBuilder setName(String name) {
        meta.setDisplayName(EssenceUtils.formatColor(name + "&r"));
        return this;
    }

    @Override
    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    @Override
    public ItemBuilder setLore(List<String> lore) {
        for(int i = 0; i < lore.size(); i++) {
            String colored = EssenceUtils.formatColor(lore.get(i));
            lore.set(i, colored);
        }

        meta.setLore(lore);
        return this;
    }

    @Override
    public ItemBuilder addLore(String... lines) {
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.addAll(Arrays.asList(lines));
        setLore(lore);

        return this;
    }

    @Override
    public ItemBuilder removeLore(int index) {
        return this;
    }

    @Override
    public ItemBuilder deleteLore() {
        meta.setLore(null);
        return this;
    }

    @Override
    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        return this;
    }

    @Override
    public ItemBuilder removeEnchant(Enchantment enchantment) {
        return this;
    }

    @Override
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        meta.spigot().setUnbreakable(unbreakable);
        return this;
    }

    @Override
    public ItemBuilder addItemFlags(ItemFlag... flags) {
        meta.removeItemFlags(flags);
        return this;
    }

    @Override
    public IItemBuilder removeItemFlags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    @Override
    public ItemBuilder setHeadOwner(String playerName) {
        throw new NotImplementedException();
    }

    @Override
    public ItemBuilder setHeadTexture(String texture, String signature) {
        if(texture == null || texture.isEmpty()) return this;

        SkullMeta headMeta = (SkullMeta) meta;
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture, signature));

        try {
            FieldUtils.writeField(headMeta, "profile", profile, true);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to modify head meta.", e);
        }

        return this;
    }


    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}
