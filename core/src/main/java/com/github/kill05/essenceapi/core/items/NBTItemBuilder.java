package com.github.kill05.essenceapi.core.items;

import com.github.kill05.essenceapi.core.utils.EssenceUtils;
import com.github.kill05.essenceapi.core.utils.ItemUtils;
import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NBTItemBuilder implements IItemBuilder {

    private final NBTItem nbtItem;

    public NBTItemBuilder(NBTItem nbtItem) {
        this.nbtItem = nbtItem;
    }


    @Override
    public NBTItemBuilder setDurability(short durability) {
        nbtItem.getItem().setDurability(durability);
        return this;
    }

    @Override
    public NBTItemBuilder setName(String name) {
        nbtItem.modifyMeta((readableNBT, meta) -> {
            meta.setDisplayName(EssenceUtils.formatColor(name + "&r"));
        });
        return this;
    }

    @Override
    public NBTItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    @Override
    public NBTItemBuilder setLore(List<String> lore) {
        nbtItem.modifyMeta((readableNBT, meta) -> {
            for(int i = 0; i < lore.size(); i++) {
                String colored = EssenceUtils.formatColor(lore.get(i));
                lore.set(i, colored);
            }

            meta.setLore(lore);
        });
        return this;
    }

    @Override
    public NBTItemBuilder addLore(String... lines) {
        ItemMeta meta = nbtItem.getItem().getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.addAll(Arrays.asList(lines));
        setLore(lore);

        return this;
    }

    @Override
    public NBTItemBuilder removeLore(int index) {
        nbtItem.modifyMeta((readableNBT, meta) -> {
            if(meta.getLore() == null) return;
            meta.getLore().remove(index);
        });
        return this;
    }

    @Override
    public NBTItemBuilder deleteLore() {
        nbtItem.modifyMeta((readableNBT, meta) -> {
            meta.setLore(null);
        });
        return this;
    }


    @Override
    public NBTItemBuilder addEnchant(Enchantment enchantment, int level) {
        nbtItem.modifyMeta((readableNBT, meta) -> {
            meta.addEnchant(enchantment, level, true);
        });
        return this;
    }

    @Override
    public NBTItemBuilder removeEnchant(Enchantment enchantment) {
        nbtItem.modifyMeta((readableNBT, meta) -> {
            meta.removeEnchant(enchantment);
        });
        return this;
    }

    @Override
    public NBTItemBuilder setUnbreakable(boolean unbreakable) {
        nbtItem.modifyMeta((readableNBT, meta) -> {
            ItemUtils.setUnbreakable(meta, unbreakable);
        });
        return this;
    }

    @Override
    public NBTItemBuilder addItemFlags(ItemFlag... flags) {
        nbtItem.modifyMeta((readableNBT, meta) -> {
            meta.addItemFlags(flags);
        });
        return this;
    }

    @Override
    public NBTItemBuilder removeItemFlags(ItemFlag... flags) {
        nbtItem.modifyMeta((readableNBT, meta) -> {
            meta.removeItemFlags(flags);
        });
        return this;
    }

    @Override
    public IItemBuilder setHeadOwner(String playerName) {
        throw new NotImplementedException();
    }

    @Override
    public IItemBuilder setHeadTexture(String texture, String signature) {
        throw new NotImplementedException();
    }

    @Override
    public ItemStack build() {
        return nbtItem.getItem();
    }
}
