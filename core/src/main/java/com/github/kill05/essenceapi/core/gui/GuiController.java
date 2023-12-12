package com.github.kill05.essenceapi.core.gui;

import com.github.kill05.essenceapi.core.gui.events.GuiClickEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.io.Closeable;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class GuiController implements Listener, Closeable {

    private final Map<Inventory, ActiveGuiInfo> guiMap;

    public GuiController() {
        this.guiMap = new WeakHashMap<>();
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ActiveGuiInfo guiInfo = guiMap.get(event.getInventory());
        Inventory clickedInv = event.getClickedInventory();
        if(guiInfo == null) return;

        EssenceGui gui = guiInfo.getGui();

        //check for top inventory (gui inventory) clicks
        if(clickedInv == event.getInventory()) {
            gui.handleGuiClick(new GuiClickEvent(event, gui, guiInfo.getPage()));
        }

        //check for bottom inventory (player inventory) clicks
        if(clickedInv == event.getView().getBottomInventory()) {
            gui.handlePlayerInvClick(event);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        ActiveGuiInfo guiInfo = guiMap.get(event.getInventory());
        if(guiInfo != null) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        ActiveGuiInfo guiInfo = guiMap.get(event.getInventory());
        if(guiInfo == null) return;

        guiInfo.getGui().handleClose(event);
        removeActiveGui(event.getInventory());
    }


    public void addActiveGui(EssenceGui gui, Inventory inventory, int page) {
        guiMap.put(inventory, new ActiveGuiInfo(inventory, gui, page));
    }

    public void removeActiveGui(Inventory inventory) {
        guiMap.remove(inventory);
    }

    @SuppressWarnings("WhileLoopReplaceableByForEach")
    @Override
    public void close() {
        Iterator<Inventory> iterator = guiMap.keySet().iterator();
        while(iterator.hasNext()) {
            Iterator<HumanEntity> humanIterator = iterator.next().getViewers().iterator();
            while(humanIterator.hasNext()) {
                humanIterator.next().closeInventory();
            }
            iterator.remove();
        }
    }

    public Map<Inventory, ActiveGuiInfo> getGuiMap() {
        return guiMap;
    }
}
