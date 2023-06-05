package com.github.kill05.essenceapi.core.nms.packets;

import com.github.kill05.essenceapi.core.EssenceAPI;
import com.github.kill05.essenceapi.core.nms.NMSHelper;
import com.github.kill05.essenceapi.core.nms.packets.events.PacketEvent;
import io.netty.channel.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.logging.Level;

public final class PacketListener implements Listener {

    private static final String CHANNEL_NAME = "essenceapi_packet_listener";


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        injectPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        uninjectPlayer(event.getPlayer());
    }


    public void injectPlayers(Collection<? extends Player> players) {
        for(Player player : players) {
            injectPlayer(player);
        }
    }

    public void uninjectPlayers(Collection<? extends Player> players) {
        for(Player player : players) {
            uninjectPlayer(player);
        }
    }

    public void injectPlayers() {
        injectPlayers(Bukkit.getOnlinePlayers());
    }

    public void uninjectPlayers() {
        uninjectPlayers(Bukkit.getOnlinePlayers());
    }

    public void injectPlayer(Player player) {
        ChannelPipeline pipeline = NMSHelper.getChannel(player).pipeline();
        if(pipeline.get(CHANNEL_NAME) != null) {
            EssenceAPI.log(Level.WARNING, String.format("Packet listener channel already exists for player %s!", player.getName()));
            return;
        }

        pipeline.addBefore("packet_handler", CHANNEL_NAME, getChannelHandler(player));
    }

    public void uninjectPlayer(Player player) {
        Channel channel = NMSHelper.getChannel(player);
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(CHANNEL_NAME);
            return null;
        });
    }

    public ChannelDuplexHandler getChannelHandler(Player player) {
        return new ChannelDuplexHandler() {
            //Packets sent to the server (client -> server)
            @Override
            public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
                PacketEvent event = new PacketEvent(player, packet, PacketEvent.PacketType.INCOMING);
                Bukkit.getServer().getPluginManager().callEvent(event);

                if(!event.isCancelled()) super.channelRead(context, packet);
            }

            //Packets sent by the server (server -> client)
            @Override
            public void write(ChannelHandlerContext context, Object packet, ChannelPromise channelPromise) throws Exception {
                PacketEvent event = new PacketEvent(player, packet, PacketEvent.PacketType.OUTGOING);
                Bukkit.getServer().getPluginManager().callEvent(event);

                if(!event.isCancelled()) super.write(context, packet, channelPromise);
            }
        };
    }

}
