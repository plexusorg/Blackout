package dev.plex.packet;

import dev.plex.Blackout;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Player;
import org.bukkit.event.Listener;

public interface IPacketListener<T extends Packet<?>> extends Listener
{
    default boolean onReceive(Player player, T t)
    {
        return true;
    }

    default Blackout plugin()
    {
        return Blackout.getPlugin();
    }
}
