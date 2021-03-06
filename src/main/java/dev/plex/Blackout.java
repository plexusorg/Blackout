package dev.plex;

import dev.plex.listener.PlayerListener;
import dev.plex.packet.PacketManager;
import dev.plex.packet.impl.*;
import java.util.List;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Blackout extends JavaPlugin
{
    @Getter
    private static Blackout plugin;

    private static FileConfiguration config;

    @Getter
    private PacketManager packetManager;

    private static boolean debugEnabled;

    public static void debug(String message)
    {
        if (debugEnabled)
        {
            Blackout.getPlugin().getServer().getConsoleSender().sendMessage(Component.text("[Blackout Debug] ").color(NamedTextColor.GOLD).append(LegacyComponentSerializer.legacyAmpersand().deserialize(message)));
        }
    }

    @Override
    public void onLoad()
    {
        plugin = this;
        config = this.getConfig();
        debugEnabled = config.getBoolean("debug");
    }

    @Override
    public void onEnable()
    {
        loadConfig();
        this.packetManager = new PacketManager();
        this.packetManager.registerListener(ServerboundUseItemOnPacket.class, new PaintingPatch());
        this.packetManager.registerListener(ServerboundUseItemOnPacket.class, new LecternPatch());
        this.packetManager.registerListener(ServerboundUseItemOnPacket.class, new KnowledgeBookPatch());
        this.packetManager.registerListener(ServerboundSetCreativeModeSlotPacket.class, new SkullOwnerPatch());
        this.packetManager.registerListener(ClientboundUpdateMobEffectPacket.class, new EndermanPotionPatch());
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    private void loadConfig()
    {
        config.options().setHeader(List.of("Blackout Configuration"));
        config.addDefault("debug", false);
        config.options().copyDefaults(true);
        saveConfig();
        Bukkit.getLogger().info("Loaded configuration file");
    }
}
