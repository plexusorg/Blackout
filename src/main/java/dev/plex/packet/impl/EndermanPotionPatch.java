package dev.plex.packet.impl;

import dev.plex.Blackout;
import dev.plex.packet.IPacketListener;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PotionSplashEvent;

public class EndermanPotionPatch implements IPacketListener<ClientboundUpdateMobEffectPacket>
{
    @EventHandler
    public void onPotionThrow(PotionSplashEvent event)
    {
        if (event.getAffectedEntities().isEmpty())
        {
            return;
        }
        Blackout.debug("Hit entity");
        if (event.getAffectedEntities().stream().noneMatch(affected -> affected.getType() == EntityType.ENDERMAN))
        {
            return;
        }
        Blackout.debug("Enderman");

        ItemStack item = CraftItemStack.asNMSCopy(event.getPotion().getItem());
        if (!item.hasTag())
        {
            return;
        }
//        Blackout.debug(item.getTag().getAsString());

        AtomicInteger count = new AtomicInteger();

        Blackout.debug(MobEffects.DAMAGE_BOOST.getDescriptionId());
        if (!item.getTag().contains("CustomPotionEffects"))
        {
            return;
        }
        ListTag tag = (ListTag)item.getTag().get("CustomPotionEffects");
        tag.forEach(effect ->
        {
            CompoundTag compoundTag = (CompoundTag)effect;
            Blackout.debug(compoundTag.getAsString());
            if (compoundTag.getByte("Id") == 7)
            {
                Blackout.debug("Hi");
                count.incrementAndGet();
            }
        });
        if (count.get() >= 20)
        {
            event.setCancelled(true);
        }
    }
}
