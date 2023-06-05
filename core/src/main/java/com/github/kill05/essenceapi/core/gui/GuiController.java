package com.github.kill05.essenceapi.core.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GuiController implements Listener {

    private final Map<Inventory, EssenceGui> guiMap;

    public GuiController() {
        this.guiMap = new HashMap<>();
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        EssenceGui gui = guiMap.get(event.getInventory());
        Inventory clickedInv = event.getClickedInventory();
        if(gui == null) return;

        //check for top inventory (gui inventory) clicks
        if(clickedInv == event.getInventory()) {
            gui.handleGuiClick(event);
        }

        //check for bottom inventory (player inventory) clicks
        if(clickedInv == event.getView().getBottomInventory()) {
            gui.handlePlayerInvClick(event);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        EssenceGui gui = guiMap.get(event.getInventory());
        if(gui != null) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        EssenceGui gui = guiMap.get(event.getInventory());
        if(gui != null) {
            gui.handleClose(event);
            removeGui(gui);
        }
    }


    public void addGui(EssenceGui gui) {
        guiMap.put(gui.getInventory(), gui);
    }

    public void removeGui(EssenceGui gui) {
        guiMap.remove(gui.getInventory());
    }

    public void closeAll() {
        for(EssenceGui gui : new HashSet<>(guiMap.values())) {
            gui.close();
        }
    }


    public Map<Inventory, EssenceGui> getGuiMap() {
        return guiMap;
    }
}
