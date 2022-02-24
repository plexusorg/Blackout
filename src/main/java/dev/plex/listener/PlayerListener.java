package dev.plex.listener;

import dev.plex.util.PacketInterceptorUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        PacketInterceptorUtil.inject(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        PacketInterceptorUtil.eject(event.getPlayer());
    }
}
