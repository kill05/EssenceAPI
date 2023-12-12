package com.github.kill05.essenceapi.core.gui.slot;

import com.github.kill05.essenceapi.core.gui.EssenceGui;
import com.github.kill05.essenceapi.core.gui.events.GuiClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public abstract class GuiSlot implements Cloneable {

    protected EssenceGui gui;
    protected int index;
    private Consumer<GuiClickEvent> clickEvent;

    public GuiSlot(int index) {
        this.index = index;
    }


    public abstract ItemStack getItem(int page);

    public abstract void setItem(ItemStack item, int page);

    public ItemStack getItem() {
        return getItem(0);
    }

    public void setItem(ItemStack item) {
        setItem(item, 0);
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        if(gui != null) throw new IllegalStateException("Slot index changed while assigned!");
        this.index = index;
    }


    public EssenceGui getGui() {
        return gui;
    }

    public void setGui(EssenceGui gui) {
        if(this.gui != null) throw new IllegalStateException("This slot already has a gui assigned!");
        this.gui = gui;
    }


    public Consumer<GuiClickEvent> getClickEvent() {
        return clickEvent;
    }

    public GuiSlot setClickEvent(Consumer<GuiClickEvent> clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }

    public void handleClick(GuiClickEvent event) {
        if(clickEvent != null) clickEvent.accept(event);
    }


    @Override
    public GuiSlot clone() {
        try {
            return (GuiSlot) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public GuiSlot clone(int newIndex) {
        GuiSlot slot = clone();
        slot.setIndex(newIndex);

        return slot;
    }
}
