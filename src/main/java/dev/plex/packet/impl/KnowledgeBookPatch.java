package dev.plex.packet.impl;

import dev.plex.packet.IPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class KnowledgeBookPatch implements IPacketListener<ServerboundUseItemOnPacket>
{
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (event.getCurrentItem() == null) return;

        org.bukkit.inventory.@Nullable ItemStack bukkitItem = event.getCurrentItem();
        if (bukkitItem.getType() != Material.KNOWLEDGE_BOOK) return;

        CraftItemStack craftItemStack = (CraftItemStack) bukkitItem;
        ItemStack item = craftItemStack.handle;
        if (!item.hasTag()) return;
        CompoundTag tag = item.getTag();
        assert tag != null;
        if (tag.contains("Recipes"))
        {
            ListTag recipes = (ListTag) tag.get("Recipes");
            AtomicBoolean remove = new AtomicBoolean();
            assert recipes != null;
            recipes.forEach(recipe -> {
                if (!recipe.getAsString().startsWith("minecraft:"))
                {
                    remove.set(true);
                    return;
                }
                if (NamespacedKey.fromString(recipe.getAsString()) == null)
                {
                    remove.set(true);
                    return;
                }
                if (!EnumUtils.isValidEnum(Material.class, recipe.getAsString().split(":")[1].toUpperCase(Locale.ROOT)))
                {
                    remove.set(true);
                }
            });
            if (remove.get())
            {
                event.getWhoClicked().getInventory().remove(bukkitItem);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event)
    {
        if (event.getItem() == null) return;

        org.bukkit.inventory.@Nullable ItemStack bukkitItem = event.getItem();
        if (bukkitItem.getType() != Material.KNOWLEDGE_BOOK) return;

        CraftItemStack craftItemStack = (CraftItemStack) bukkitItem;
        ItemStack item = craftItemStack.handle;
        if (!item.hasTag()) return;
        CompoundTag tag = item.getTag();
        assert tag != null;
        if (tag.contains("Recipes"))
        {
            ListTag recipes = (ListTag) tag.get("Recipes");
            AtomicBoolean remove = new AtomicBoolean();
            assert recipes != null;
            recipes.forEach(recipe -> {
                if (!recipe.getAsString().startsWith("minecraft:"))
                {
                    remove.set(true);
                    return;
                }
                if (NamespacedKey.fromString(recipe.getAsString()) == null)
                {
                    remove.set(true);
                    return;
                }
                if (!EnumUtils.isValidEnum(Material.class, recipe.getAsString().split(":")[1].toUpperCase(Locale.ROOT)))
                {
                    remove.set(true);
                }
            });
            if (remove.get())
            {
                event.getPlayer().getInventory().remove(bukkitItem);
                event.setCancelled(true);
            }
        }
    }

}
