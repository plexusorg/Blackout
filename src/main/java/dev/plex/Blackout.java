package dev.plex;

import dev.plex.listener.PlayerListener;
import dev.plex.packet.PacketManager;
import dev.plex.packet.impl.EndermanPotionPatch;
import dev.plex.packet.impl.KnowledgeBookPatch;
import dev.plex.packet.impl.LecternPatch;
import dev.plex.packet.impl.PaintingPatch;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.network.protocol.game.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Blackout extends JavaPlugin
{
    @Getter
    private static Blackout plugin;

    @Getter
    private PacketManager packetManager;

    @Override
    public void onLoad()
    {
        plugin = this;
        debug("Loading");
    }

    @Override
    public void onEnable()
    {
        this.packetManager = new PacketManager();
        this.packetManager.registerListener(ServerboundUseItemOnPacket.class, new PaintingPatch());
        this.packetManager.registerListener(ServerboundUseItemOnPacket.class, new LecternPatch());
        this.packetManager.registerListener(ServerboundUseItemOnPacket.class, new KnowledgeBookPatch());
        this.packetManager.registerListener(ClientboundUpdateMobEffectPacket.class, new EndermanPotionPatch());

        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public static void debug(String message)
    {
        Blackout.getPlugin().getServer().getConsoleSender().sendMessage(Component.text("[Blackout Debug] ").color(NamedTextColor.GOLD).append(LegacyComponentSerializer.legacyAmpersand().deserialize(message)));
    }
}
