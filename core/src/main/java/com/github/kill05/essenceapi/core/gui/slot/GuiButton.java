package com.github.kill05.essenceapi.core.gui.slot;

import com.github.kill05.essenceapi.core.gui.EssenceGui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class GuiButton implements Cloneable {

    protected EssenceGui gui;
    protected int index;

    public GuiButton(int index) {
        this.index = index;
    }


    public abstract boolean handleClick(InventoryClickEvent event);


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ItemStack getItem() {
        return gui.getInventory().getItem(index);
    }

    public EssenceGui getGui() {
        return gui;
    }

    public void setGui(EssenceGui gui) {
        this.gui = gui;
    }

    @Override
    public GuiButton clone() {
        try {
            return (GuiButton) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public GuiButton clone(int newIndex) {
        GuiButton button = clone();
        button.setIndex(newIndex);
        return button;
    }
}
