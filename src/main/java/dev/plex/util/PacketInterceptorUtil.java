package dev.plex.util;

import dev.plex.Blackout;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketInterceptorUtil
{
     public static void inject(Player player) {
        ChannelDuplexHandler handler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (msg instanceof Packet<?> packet)
                {
                    boolean read = Blackout.getPlugin().getPacketManager().callPacket(((CraftPlayer)player).getHandle(), packet);
                    if (!read) return;
                }
                super.channelRead(ctx, msg);
            }
        };

        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().connection.connection.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), handler);
    }

    public static void eject(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().connection.connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
        });
    }
}
