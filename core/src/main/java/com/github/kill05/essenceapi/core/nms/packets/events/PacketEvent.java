package com.github.kill05.essenceapi.core.nms.packets.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Object packet;
    private final PacketType type;
    private boolean cancelled;

    public PacketEvent(Player player, Object packet, PacketType type) {
        super(true);
        this.player = player;
        this.packet = packet;
        this.type = type;
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }


    public Player getPlayer() {
        return player;
    }

    public Object getPacket() {
        return packet;
    }

    public PacketType getType() {
        return type;
    }


    public enum PacketType {
        INCOMING,
        OUTGOING
    }

}
