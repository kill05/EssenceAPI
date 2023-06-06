package com.github.kill05.essenceapi.core.gui;

import com.github.kill05.essenceapi.core.EssenceAPI;
import com.github.kill05.essenceapi.core.gui.slot.DefaultButton;
import com.github.kill05.essenceapi.core.gui.slot.GuiButton;
import com.github.kill05.essenceapi.core.gui.slot.PredicateButton;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EssenceGui {

    private final Inventory inventory;
    private final Map<Integer, GuiButton> buttonMap;
    private Consumer<InventoryCloseEvent> closeEvent;
    private Consumer<InventoryClickEvent> playerInvClickEvent;

    public EssenceGui(InventoryType type, String title) {
        this.inventory = Bukkit.createInventory(null, type, title);
        this.buttonMap = new HashMap<>();
        setup();
    }

    public EssenceGui(int rows, String title) {
        this.inventory = Bukkit.createInventory(null, rows * 9, title);
        this.buttonMap = new HashMap<>();
        setup();
    }

    public EssenceGui(InventoryType type) {
        this(type, null);
    }

    public EssenceGui(int rows) {
        this(rows, null);
    }


    private void setup() {
        EssenceAPI.getGuiController().addGui(this);
        for(int i = 0; i < inventory.getSize(); i++) {
            addButton(new DefaultButton(i));
        }
    }


    public void open(Player player) {
        player.openInventory(getInventory());
    }

    public void close(Player player) {
        if(player.getOpenInventory().getTopInventory() == inventory) player.closeInventory();
    }

    public void close() {
        for(HumanEntity human : getInventory().getViewers()) {
            human.closeInventory();
        }
    }

    public void handleGuiClick(InventoryClickEvent event) {
        GuiButton button = getButton(event.getSlot());

        if(button.handleClick(event)) {
            event.setCancelled(true);
        }
    }

    public void handlePlayerInvClick(InventoryClickEvent event) {
        if(playerInvClickEvent == null) return;
        playerInvClickEvent.accept(event);
    }


    public void handleClose(InventoryCloseEvent event) {
        if(closeEvent == null) return;
        closeEvent.accept(event);
    }


    public Consumer<InventoryCloseEvent> getCloseEvent() {
        return closeEvent;
    }

    public void setCloseEvent(Consumer<InventoryCloseEvent> closeEvent) {
        this.closeEvent = closeEvent;
    }

    public Consumer<InventoryClickEvent> getPlayerInvClickEvent() {
        return playerInvClickEvent;
    }

    public void setPlayerInvClickEvent(Consumer<InventoryClickEvent> playerInvClickEvent) {
        this.playerInvClickEvent = playerInvClickEvent;
    }


    public void setItem(int index, ItemStack item, Boolean locked) {
        inventory.setItem(index, item);
        GuiButton button = getButton(index);
        if(locked != null && button instanceof DefaultButton) {
            ((DefaultButton) button).setLocked(locked);
        }
    }

    public void setItem(int index, ItemStack item) {
        setItem(index, item, null);
    }

    public ItemStack getItem(int index) {
        return inventory.getItem(index);
    }


    public void addButton(GuiButton button) {
        button.setGui(this);
        buttonMap.put(button.getIndex(), button);
    }

    public GuiButton getButton(int index) {
        return buttonMap.getOrDefault(index, new DefaultButton(index));
    }


    public Inventory getInventory() {
        return inventory;
    }

    public Map<Integer, GuiButton> getButtonMap() {
        return buttonMap;
    }


    public static class Builder {

        private final EssenceGui gui;

        public Builder(InventoryType type, String title) {
            this.gui = new EssenceGui(type, title);
        }

        public Builder(int rows, String title) {
            this.gui = new EssenceGui(rows, title);
        }

        public Builder(InventoryType type) {
            this.gui = new EssenceGui(type);
        }

        public Builder(int rows) {
            this.gui = new EssenceGui(rows);
        }


        public Builder addButtons(GuiButton button, ItemStack item, int... indices) {
            for(int index : indices) {
                addButton(button.clone(index), item);
            }

            return this;
        }

        public Builder addButton(GuiButton button, ItemStack item) {
            addButton(button);
            gui.setItem(button.getIndex(), item);
            return this;
        }

        public Builder addButton(GuiButton button) {
            gui.addButton(button);
            return this;
        }

        public Builder addButton(int index, ItemStack item, Predicate<InventoryClickEvent> predicate) {
            return addButton(new PredicateButton(index, predicate), item);
        }

        public Builder addButton(int index, Predicate<InventoryClickEvent> predicate) {
            return addButton(new PredicateButton(index, predicate));
        }


        public Builder setItems(Material material, int amount, boolean locked, int... indices) {
            return setItems(new ItemStack(material, amount), locked, indices);
        }

        public Builder setItems(Material material, boolean locked, int... indices) {
            return setItems(new ItemStack(material), locked, indices);
        }

        public Builder setItems(ItemStack item, boolean locked, int... indices) {
            for(int index : indices) {
                setItem(index, item, locked);
            }

            return this;
        }

        public Builder setItems(Material material, int amount, int... indices) {
            return setItems(new ItemStack(material, amount), indices);
        }

        public Builder setItems(Material material, int... indices) {
            return setItems(new ItemStack(material), indices);
        }

        public Builder setItems(ItemStack item, int... indices) {
            for(int index : indices) {
                setItem(index, item);
            }

            return this;
        }


        public Builder setItem(int index, Material material, int amount, boolean locked) {
            gui.setItem(index, new ItemStack(material, amount), locked);
            return this;
        }

        public Builder setItem(int index, Material material, boolean locked) {
            gui.setItem(index, new ItemStack(material), locked);
            return this;
        }

        public Builder setItem(int index, ItemStack item, boolean locked) {
            gui.setItem(index, item, locked);
            return this;
        }

        public Builder setItem(int index, Material material, int amount) {
            gui.setItem(index, new ItemStack(material, amount));
            return this;
        }

        public Builder setItem(int index, Material material) {
            gui.setItem(index, new ItemStack(material));
            return this;
        }

        public Builder setItem(int index, ItemStack item) {
            gui.setItem(index, item);
            return this;
        }


        public Builder setItems(int start, int end, ItemStack item) {
            for(int i = Math.min(start, end); i <= Math.max(start, end); i++) {
                setItem(i, item);
            }

            return this;
        }


        public Builder setRow(int row, ItemStack item) {
            return setRow(row, 0, 8, item);
        }

        public Builder setRow(int row, int start, int end, ItemStack item) {
            if(start < 0) throw new IllegalArgumentException(String.format("Start can't be smaller than 0! (start: %s)", start));
            if(end > 8) throw new IllegalArgumentException(String.format("End can't be bigger than 8! (end: %s)", end));
            if(start > end) throw new IllegalArgumentException(String.format("Start can't be bigger than end! (start: %s, end: %s).", start, end));
            return setItems(row * 9 + start, row * 9 + end, item);
        }

        public Builder setColumn(int column, ItemStack item) {
            return setColumn(column, 0, gui.inventory.getSize() / 9, item);
        }

        public Builder setColumn(int column, int start, int end, ItemStack item) {
            column = column % 9;
            for(int i = Math.min(start, end); i <= Math.max(start, end); i++) {
                setItem(i * 9 + column, item);
            }

            return this;
        }

        public Builder setSquare(int index1, int index2, ItemStack item) {
            int minRow = Math.min(index1 / 9, index2 / 9);
            int maxRow = Math.max(index1 / 9, index2 / 9);
            int minColumn = Math.min(index1 % 9, index2 % 9);
            int maxColumn = Math.max(index1 % 9, index2 % 9);

            setItems(minRow * 9 + minColumn, minRow * 9 + maxColumn, item);
            setItems(maxRow * 9 + minColumn, maxRow * 9 + maxColumn, item);

            if(maxRow - minRow >= 2) {
                setColumn(minColumn, minRow + 1, maxRow - 1, item);
                setColumn(maxColumn, minRow + 1, maxRow - 1, item);
            }

            return this;
        }


        public Builder onClose(Consumer<InventoryCloseEvent> closeEvent) {
            gui.closeEvent = closeEvent;
            return this;
        }

        public Builder onPlayerInvClick(Consumer<InventoryClickEvent> clickEvent) {
            gui.playerInvClickEvent = clickEvent;
            return this;
        }

        public EssenceGui build() {
            return gui;
        }

        public void buildAndOpen(Player player) {
            build().open(player);
        }

    }
}
