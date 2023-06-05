package com.github.kill05.essenceapi.craftbukkit_1_19_R3;

import com.github.kill05.essenceapi.core.modules.packets.PacketModule;
import io.netty.channel.Channel;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.Packet;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketModule_1_19_R3 extends PacketModule {

    @SuppressWarnings("rawtypes")
    @Override
    public void sendPacket(Object packet, Player player) {
        if(!(packet instanceof Packet)) throw new IllegalArgumentException(String.format("Invalid packet: %s", packet));
        ((CraftPlayer) player).getHandle().b.a((Packet) packet);
    }

    @Override
    public Channel getChannel(Player player) {
        try {
            NetworkManager netManager = (NetworkManager) FieldUtils.readField(((CraftPlayer) player).getHandle().b, "h", true);
            return netManager.m;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to get NetworkManager field. This is a bug!", e);
        }
    }

}
