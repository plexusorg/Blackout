package dev.plex.packet;

import dev.plex.Blackout;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Player;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;

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

    default <J> J getFromField(Object object, String name)
    {
        try
        {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return (J) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
