package com.github.kill05.essenceapi.core.utils.heads;

import com.github.kill05.essenceapi.core.items.ItemBuilder;
import org.bukkit.inventory.ItemStack;

public class RegisteredHead {

    private final ItemStack item;

    public RegisteredHead(String texture) {
        this.item = ItemBuilder.headItem(texture).build();
    }

    public ItemStack get() {
        return item.clone();
    }
}
