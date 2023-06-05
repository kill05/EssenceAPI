package com.github.kill05.essenceapi.craftbukkit_1_8_R3;

import com.github.kill05.essenceapi.core.modules.packets.PacketModule;
import io.netty.channel.Channel;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketModule_1_8_R3 extends PacketModule {

    @SuppressWarnings("rawtypes")
    @Override
    public void sendPacket(Object packet, Player player) {
        if(!(packet instanceof Packet)) throw new IllegalArgumentException(String.format("Invalid packet: %s", packet));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet) packet);
    }

    @Override
    public Channel getChannel(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
    }

}
