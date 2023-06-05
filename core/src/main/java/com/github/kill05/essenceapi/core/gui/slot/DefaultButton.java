package com.github.kill05.essenceapi.core.gui.slot;

import org.bukkit.event.inventory.InventoryClickEvent;

public class DefaultButton extends GuiButton {

    private boolean locked;

    public DefaultButton(int index, boolean locked) {
        super(index);
        this.locked = locked;
    }

    public DefaultButton(int index) {
        this(index, true);
    }

    @Override
    public boolean handleClick(InventoryClickEvent event) {
        return locked;
    }


    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
