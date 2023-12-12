package com.github.kill05.essenceapi.core.gui.slot;

import com.github.kill05.essenceapi.core.gui.events.GuiClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class PageButton extends SimpleSlot {

    private final Direction direction;
    private final ItemStack defaultItem;

    public PageButton(int index, ItemStack arrowItem, ItemStack defaultItem, Direction direction) {
        super(index, arrowItem);
        this.direction = direction;
        this.defaultItem = defaultItem;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public ItemStack getItem(int page) {
        return canBeClicked(page) ? super.getItem(page) : defaultItem;
    }

    @Override
    public void handleClick(GuiClickEvent event) {
        if(canBeClicked(event.getPage())) openPage(event, event.getPage() + direction.getValue());
    }

    public boolean canBeClicked(int page) {
        int lastPage = 0;

        for(GuiSlot slot : gui.getSlotMap().values()) {
            if(!(slot instanceof ListPagedSlot)) continue;
            ListPagedSlot pagedSlot = (ListPagedSlot) slot;

            int i = (pagedSlot.getItems().size() - 1) / pagedSlot.getAmountPerPage();
            if(i > lastPage) lastPage = i;
        }

        if(direction == Direction.PREVIOUS && page <= 0) {
            return false;
        }

        return direction != Direction.NEXT || page < lastPage;
    }

    public abstract void openPage(GuiClickEvent event, int page);


    public enum Direction {
        PREVIOUS(-1),
        NEXT(1);

        private final int value;

        Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
