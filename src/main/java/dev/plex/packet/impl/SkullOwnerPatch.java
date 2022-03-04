package dev.plex.packet.impl;

import com.mojang.authlib.GameProfile;
import dev.plex.Blackout;
import dev.plex.packet.IPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class SkullOwnerPatch implements IPacketListener<ServerboundSetCreativeModeSlotPacket>
{
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        org.bukkit.inventory.ItemStack bukkitItem = event.getCurrentItem();

        if (bukkitItem == null) return;
        if (event.getWhoClicked().getType() != EntityType.PLAYER) return;

        checkItem(((CraftPlayer)event.getWhoClicked()).getHandle(), bukkitItem);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event)
    {
        if (event.getView().getType() == InventoryType.PLAYER)
        {
            Blackout.debug("Checking all items...");
            Arrays.stream(event.getInventory().getContents()).forEach(item -> checkItem(((CraftPlayer)event.getPlayer()).getHandle(), item));
        }
    }

    @Override
    public boolean onReceive(Player player, ServerboundSetCreativeModeSlotPacket serverboundSetCreativeModeSlotPacket)
    {
        return checkItem(player, serverboundSetCreativeModeSlotPacket.getItem().getBukkitStack());
    }

    private boolean checkItem(Player player, org.bukkit.inventory.ItemStack bukkitItem)
    {
        Blackout.debug("Starting skull owner exploit patch...");
        if (bukkitItem == null) return true;
        ItemStack item = ((CraftItemStack)bukkitItem).handle;
        if (item.getBukkitStack().getType() != Material.PLAYER_HEAD) return true;

        SkullMeta meta = (SkullMeta) item.getBukkitStack().getItemMeta();
        GameProfile profile = getFromField(meta, "profile");
        Blackout.debug(String.valueOf(profile == null));
        if (profile == null) return true;
        if (profile.getName() == null && profile.getId() == null) return true;
        if (profile.getName().isEmpty())
        {
            Blackout.debug("Patching invalid skull owner exploit!");
            player.getInventory().removeItem(item);
            return false;
        }
        if (!profile.getName().matches("\\p{Alpha}"))
        {
            Blackout.debug("Patching invalid skull owner exploit!");
            player.getInventory().removeItem(item);
            return false;
        }
        if (profile.getId().toString().isEmpty())
        {
            Blackout.debug("Patching invalid skull owner exploit!");
            player.getInventory().removeItem(item);
            return false;
        }

        if (!item.hasTag()) return true;

        CompoundTag tag = item.getTag();
        if (!tag.contains("SkullOwner")) return true;
        String owner = tag.getString("SkullOwner");
        if (!owner.matches("\\p{Alpha}"))
        {
            Blackout.debug("Patching invalid skull owner exploit!");
            player.getInventory().removeItem(item);
            return false;
        }

        return true;
    }
}
