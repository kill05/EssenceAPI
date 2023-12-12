package com.github.kill05.essenceapi.core.gui.slot;

import org.bukkit.inventory.ItemStack;

public class SimpleSlot extends GuiSlot {

    private ItemStack item;

    public SimpleSlot(int index, ItemStack item) {
        super(index);
        this.item = item;
    }


    @Override
    public ItemStack getItem(int page) {
        return item;
    }

    @Override
    public void setItem(ItemStack item, int page) {
        this.item = item;
    }

}
