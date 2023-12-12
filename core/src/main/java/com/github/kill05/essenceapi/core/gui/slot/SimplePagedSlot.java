package com.github.kill05.essenceapi.core.gui.slot;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SimplePagedSlot extends GuiSlot {

    private final Map<Integer, ItemStack> slotItemMap;

    public SimplePagedSlot(int index, ItemStack item, int slot) {
        super(index);
        this.slotItemMap = new HashMap<>();

        if(item != null) slotItemMap.put(slot, item);
    }

    public SimplePagedSlot(int index) {
        this(index, null, -1);
    }

    @Override
    public ItemStack getItem(int page) {
        return slotItemMap.get(page);
    }

    @Override
    public void setItem(ItemStack item, int page) {
        if(item == null) slotItemMap.remove(page);
        slotItemMap.put(page, item);
    }

}
