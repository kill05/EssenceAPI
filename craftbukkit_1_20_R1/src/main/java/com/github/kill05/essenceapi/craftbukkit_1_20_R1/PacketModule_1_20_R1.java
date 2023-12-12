package com.github.kill05.essenceapi.craftbukkit_1_20_R1;

import com.github.kill05.essenceapi.core.modules.packets.PacketModule;
import io.netty.channel.Channel;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.Packet;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketModule_1_20_R1 extends PacketModule {

    @SuppressWarnings("rawtypes")
    @Override
    public void sendPacket(Object packet, Player player) {
        if(!(packet instanceof Packet)) throw new IllegalArgumentException(String.format("Invalid packet: %s", packet));
        ((CraftPlayer) player).getHandle().c.a((Packet) packet);
    }

    @Override
    public Channel getChannel(Player player) {
        try {
            NetworkManager netManager = (NetworkManager) FieldUtils.readField(((CraftPlayer) player).getHandle().c, "h", true);
            return netManager.n;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to get NetworkManager field. This is a bug!", e);
        }    }

}
