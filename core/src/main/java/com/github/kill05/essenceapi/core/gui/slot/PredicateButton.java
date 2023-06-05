package com.github.kill05.essenceapi.core.gui.slot;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Predicate;

public class PredicateButton extends GuiButton {

    private final Predicate<InventoryClickEvent> consumer;

    public PredicateButton(int index, Predicate<InventoryClickEvent> consumer) {
        super(index);
        this.consumer = consumer;
    }

    @Override
    public boolean handleClick(InventoryClickEvent event) {
        return consumer.test(event);
    }
}
