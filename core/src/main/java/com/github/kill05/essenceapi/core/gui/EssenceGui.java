package com.github.kill05.essenceapi.core.gui;

import com.github.kill05.essenceapi.core.EssenceAPI;
import com.github.kill05.essenceapi.core.gui.events.GuiClickEvent;
import com.github.kill05.essenceapi.core.gui.slot.GuiSlot;
import com.github.kill05.essenceapi.core.gui.slot.ListPagedSlot;
import com.github.kill05.essenceapi.core.gui.slot.SimplePagedSlot;
import com.github.kill05.essenceapi.core.gui.slot.SimpleSlot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EssenceGui {

    private final int rows;
    private final InventoryType invType;
    private final String title;

    private final Map<Integer, GuiSlot> slotMap;
    private Consumer<InventoryCloseEvent> closeEvent;
    private Consumer<InventoryClickEvent> playerInvClickEvent;
    private boolean cancelInteractions;


    private EssenceGui(int rows, InventoryType type, String title) {
        this.rows = rows;
        this.invType = type;
        this.title = title;

        this.slotMap = new HashMap<>();
        this.cancelInteractions = true;
    }

    public EssenceGui(InventoryType type, String title) {
        this(0, type, title);
    }

    public EssenceGui(int rows, String title) {
        this(rows, null, title);
    }

    public EssenceGui(InventoryType type) {
        this(type, null);
    }

    public EssenceGui(int rows) {
        this(rows, null);
    }



    public void open(Player player, int page) {
        player.openInventory(getInventory(player, page));
    }

    public void open(Player player) {
        open(player, 0);
    }


    public void handleGuiClick(GuiClickEvent event) {
        if(cancelInteractions) event.getBukkitEvent().setCancelled(true);

        GuiSlot slot = event.getGuiSlot();
        if(slot != null) slot.handleClick(event);
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

    public boolean cancelInteractions() {
        return cancelInteractions;
    }

    public void cancelInteractions(boolean cancelDefault) {
        this.cancelInteractions = cancelDefault;
    }


    public GuiSlot getSlot(int index) {
        return getSlotMap().get(index);
    }

    public void setSlot(int index, GuiSlot slot) {
        slot.setIndex(index);
        slot.setGui(this);
        slotMap.put(slot.getIndex(), slot);
    }

    public void setSlot(GuiSlot slot) {
        setSlot(slot.getIndex(), slot);
    }


    public ItemStack getItem(int index, int page) {
        GuiSlot slot = getSlot(index);
        return slot != null ? slot.getItem(page) : null;
    }

    public ItemStack getItem(int index) {
        return getItem(index, 0);
    }

    public void setItem(int index, ItemStack item, Integer page) {
        GuiSlot slot = getSlot(index);

        if(slot == null) {
            slot = page != null ? new SimplePagedSlot(index) : new SimpleSlot(index, item);
            setSlot(index, slot);
            return;
        }

        slot.setItem(item, page != null ? page : 0);
    }

    public void setItem(int index, ItemStack item) {
        setItem(index, item, null);
    }


    public Inventory getInventory(Player player, int page) {
        if(rows < 1 && invType == null) throw new IllegalArgumentException(String.format("Chest guis must have at least 1 row (%s).", rows));

        Inventory inventory;
        if(invType == null) {
            inventory = Bukkit.createInventory(null, rows * 9, title);
        } else {
            inventory = Bukkit.createInventory(null, invType, title);
        }

        for(int i = 0; i < rows * 9; i++) {
            GuiSlot slot = getSlot(i);
            if(slot == null) continue;

            inventory.setItem(i, slot.getItem(page));
        }

        EssenceAPI.getGuiController().addActiveGui(this, inventory, page);
        return inventory;
    }

    public Map<Integer, GuiSlot> getSlotMap() {
        return slotMap;
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


        public Builder addSlots(GuiSlot slot, int... indices) {
            int amountPerPage = indices.length;
            int pageIndex = 0;

            for(int index : indices) {
                GuiSlot clone = slot.clone(index);

                if(clone instanceof ListPagedSlot) {
                    ListPagedSlot pagedSlot = (ListPagedSlot) clone;
                    pagedSlot.setAmountPerPage(amountPerPage);
                    pagedSlot.setPageIndex(pageIndex++);
                }

                addSlot(clone);
            }

            return this;
        }

        public Builder addSlot(GuiSlot slot) {
            gui.setSlot(slot);
            return this;
        }

        public Builder addSlot(int index, ItemStack item, Consumer<GuiClickEvent> clickEvent) {
            return addSlot(new SimpleSlot(index, item).setClickEvent(clickEvent));
        }

        public Builder addSlot(int index, Consumer<GuiClickEvent> clickEvent) {
            return addSlot(index, null, clickEvent);
        }

        public Builder setPagedItemSlots() {
            /*
            for(int i = 0; i < 7; i++) {
                builder.addSlot(new ListPagedSlot(37 + i, featureItems, 7, i) {
                    @Override
                    public void handlePagedClick(GuiClickEvent event, int pagedIndex) {
                        openFeatureGui(player, features.get(pagedIndex), false, pagedIndex);
                    }
                });
            }

             */
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

        public Builder setItem(int index, ItemStack item, int page) {
            gui.setItem(index, item, page);
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
            return setColumn(column, 0, gui.rows - 1, item);
        }

        public Builder setColumn(int column, int start, int end, ItemStack item) {
            column = column % 9;
            for(int i = Math.min(start, end); i <= Math.max(start, end); i++) {
                setItem(i * 9 + column, item);
            }

            return this;
        }

        public Builder setRect(int index1, int index2, ItemStack item) {
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

        public Builder fillRect(int index1, int index2, ItemStack item) {
            int minRow = Math.min(index1 / 9, index2 / 9);
            int maxRow = Math.max(index1 / 9, index2 / 9);
            int minColumn = Math.min(index1 % 9, index2 % 9);
            int maxColumn = Math.max(index1 % 9, index2 % 9);

            for(int row = minRow; row <= maxRow; row++) {
                setRow(row, minColumn, maxColumn, item);
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

        public void buildAndOpen(Player player, int page) {
            build().open(player, page);
        }

        public void buildAndOpen(Player player) {
            build().open(player);
        }

    }
}
