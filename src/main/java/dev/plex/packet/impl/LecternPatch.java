package dev.plex.packet.impl;

import dev.plex.Blackout;
import dev.plex.packet.IPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

public class LecternPatch implements IPacketListener<ServerboundUseItemOnPacket>
{
    @EventHandler
    public void onInventoryClick(BlockPlaceEvent event)
    {
        ItemStack item = ((CraftItemStack) event.getItemInHand()).handle;
//        Blackout.debug(item.getBukkitStack().getType().name());
//        Blackout.debug(String.valueOf(item.hasTag()));
        if (item.getBukkitStack().getType() != Material.LECTERN) return;
        if (!item.hasTag()) return;
        CompoundTag tag = item.getTag();
        assert tag != null;


//        Blackout.debug(StringUtils.join(tag.getAllKeys(), ", "));

        if (!tag.contains("BlockEntityTag")) return;
        if (!tag.getCompound("BlockEntityTag").contains("Book")) return;
        if (!tag.getCompound("BlockEntityTag").getCompound("Book").contains("tag")) return;
        CompoundTag bookTag = tag.getCompound("BlockEntityTag").getCompound("Book").getCompound("tag");
        ListTag listTag = (ListTag) bookTag.get("pages");
        assert listTag != null;
        AtomicInteger integer = new AtomicInteger();
        listTag.stream().forEach(pageTag ->
        {
            JSONArray array = new JSONArray(pageTag.getAsString());
            array.forEach(obj ->
            {
                JSONObject object = new JSONObject(obj.toString());
                if (!object.isNull("selector") && object.getString("selector").equalsIgnoreCase("@e"))
                {
                    integer.getAndIncrement();
                }
            });
        });

        if (integer.get() >= 2)
        {
            event.getPlayer().getInventory().remove(item.getBukkitStack());
            event.setCancelled(true);
        }
    }

}
