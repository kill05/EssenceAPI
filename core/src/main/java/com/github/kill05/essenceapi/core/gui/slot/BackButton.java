package com.github.kill05.essenceapi.core.gui.slot;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BackButton extends GuiButton {

    public BackButton(int index) {
        super(index);
    }

    @Override
    public boolean handleClick(InventoryClickEvent event) {
        return false;
    }

}
