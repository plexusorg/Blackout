package dev.plex.packet.impl;

import dev.plex.Blackout;
import dev.plex.packet.IPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Material;

public class PaintingPatch implements IPacketListener<ServerboundUseItemOnPacket>
{
    @Override
    public boolean onReceive(Player player, ServerboundUseItemOnPacket serverboundUseItemOnPacket)
    {
        ItemStack item = player.getItemInHand(serverboundUseItemOnPacket.getHand());
        if (!item.hasTag()) return true;
        if (item.getBukkitStack().getType() != Material.PAINTING) return true;
        CompoundTag tag = item.getTag();

        assert tag != null;
        if (tag.contains("EntityTag", Tag.TAG_COMPOUND))
        {
            CompoundTag entityData = tag.getCompound("EntityTag");
            if (entityData.contains("TileX") || entityData.contains("TileY") || entityData.contains("TileZ"))
            {
                Blackout.debug("Applying PAINTING ENTITY TAG PATCH on " + player.getName());
                player.getInventory().removeItem(item);
                return false;
            }
        }
        return false;
    }
}
