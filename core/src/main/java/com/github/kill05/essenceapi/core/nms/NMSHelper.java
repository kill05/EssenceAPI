package com.github.kill05.essenceapi.core.nms;

import com.github.kill05.essenceapi.core.modules.packets.PacketModule;
import io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public final class NMSHelper {

    private static PacketModule packetModule;

    private NMSHelper() {

    }

    @Deprecated
    public static void setPacketModule(PacketModule packetModule) {
        if(NMSHelper.packetModule != null) throw new IllegalStateException("Packet Module has already been set!");
        NMSHelper.packetModule = packetModule;
    }


    public static String getVersion() {
        String a = Bukkit.getServer().getClass().getPackage().getName();
        return a.substring(a.lastIndexOf('.') + 2);
    }

    public static void sendPacket(final @NotNull Object packet, final @NotNull Collection<? extends Player> players){
        for (Player player : players){
            packetModule.sendPacket(packet, player);
        }
    }

    public static void sendPacket(final @NotNull Object packet, final @NotNull Player player) {
        packetModule.sendPacket(packet, player);
    }

    public static void sendPacket(final @NotNull Object packet) {
        sendPacket(packet, Bukkit.getOnlinePlayers());
    }

    public static Channel getChannel(Player player) {
        return packetModule.getChannel(player);
    }

}
