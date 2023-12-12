package com.github.kill05.essenceapi.core.gui.events;

import com.github.kill05.essenceapi.core.gui.EssenceGui;
import com.github.kill05.essenceapi.core.gui.slot.GuiSlot;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiClickEvent {

    private final InventoryClickEvent bukkitEvent;
    private final EssenceGui gui;
    private final int page;

    public GuiClickEvent(InventoryClickEvent bukkitEvent, EssenceGui gui, int page) {
        this.bukkitEvent = bukkitEvent;
        this.gui = gui;
        this.page = page;
    }


    public void refreshGui() {
        gui.open(getPlayer(), getPage());
    }

    public InventoryClickEvent getBukkitEvent() {
        return bukkitEvent;
    }

    public EssenceGui getGui() {
        return gui;
    }

    public GuiSlot getGuiSlot() {
        return gui.getSlot(bukkitEvent.getSlot());
    }

    public Player getPlayer() {
        return (Player) bukkitEvent.getWhoClicked();
    }

    public int getPage() {
        return page;
    }
}
