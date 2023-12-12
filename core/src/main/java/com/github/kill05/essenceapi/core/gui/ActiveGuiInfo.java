package com.github.kill05.essenceapi.core.gui;

import org.bukkit.inventory.Inventory;

import java.lang.ref.WeakReference;

public class ActiveGuiInfo {

    private final WeakReference<Inventory> inventory;
    private final EssenceGui gui;
    private final int page;

    public ActiveGuiInfo(Inventory inventory, EssenceGui gui, int page) {
        this.inventory = new WeakReference<>(inventory);
        this.gui = gui;
        this.page = page;
    }

    public Inventory getInventory() {
        return inventory.get();
    }

    public EssenceGui getGui() {
        return gui;
    }

    public int getPage() {
        return page;
    }
}
