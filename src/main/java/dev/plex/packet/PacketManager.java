package dev.plex.packet;

import com.google.common.collect.Maps;
import dev.plex.Blackout;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Player;

public class PacketManager
{
    @Getter
    private final Map<Class<? extends Packet<?>>, IPacketListener<Packet<?>>> packetListenerMap = Maps.newHashMap();

    public <T extends Packet<?>> void registerListener(Class<T> clazz, IPacketListener<T> packetListener)
    {
        this.packetListenerMap.put(clazz, (IPacketListener<Packet<?>>)packetListener);
        Blackout.getPlugin().getServer().getPluginManager().registerEvents(packetListener, Blackout.getPlugin());
    }

    public boolean callPacket(Player player, Packet<?> packet)
    {
        AtomicBoolean bool = new AtomicBoolean(true);
        packetListenerMap.forEach((key, val) ->
        {
            if (key.isAssignableFrom(packet.getClass()))
            {
                bool.set(val.onReceive(player, packet));
            }
        });
        return bool.get();
    }
}
