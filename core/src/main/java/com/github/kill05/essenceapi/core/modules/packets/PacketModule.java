package com.github.kill05.essenceapi.core.modules.packets;

import io.netty.channel.Channel;
import org.bukkit.entity.Player;

public abstract class PacketModule {

    public abstract void sendPacket(Object packet, Player player);

    public abstract Channel getChannel(Player player);

}
