package com.github.kill05.essenceapi.core.gui.slot;

import com.github.kill05.essenceapi.core.gui.events.GuiClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ListPagedSlot extends GuiSlot {

    private final List<ItemStack> items;
    private int amountPerPage;
    private int pageIndex;

    public ListPagedSlot(int index, List<ItemStack> items) {
        super(index);
        this.items = items;
        this.amountPerPage = 1;
        this.pageIndex = 0;
    }


    @Override
    public ItemStack getItem(int page) {
        int index = getListIndex(page);
        return isIndexInBounds(page) ? items.get(index) : null;
    }

    @Override
    public void setItem(ItemStack item, int page) {
        items.set(getListIndex(page), item);
    }

    public int getListIndex(int page) {
        return page * amountPerPage + pageIndex;
    }

    private boolean isIndexInBounds(int page) {
        int index = getListIndex(page);
        return (index >= 0) && (index < items.size());
    }


    @Override
    public void handleClick(GuiClickEvent event) {
        if(isIndexInBounds(event.getPage())) handlePagedClick(event, getListIndex(event.getPage()));
    }

    public void handlePagedClick(GuiClickEvent event, int pagedIndex) {

    }

    public List<ItemStack> getItems() {
        return items;
    }

    public int getAmountPerPage() {
        return amountPerPage;
    }

    public void setAmountPerPage(int amountPerPage) {
        this.amountPerPage = amountPerPage;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}
