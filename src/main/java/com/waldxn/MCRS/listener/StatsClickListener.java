package com.waldxn.MCRS.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StatsClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String title = ChatColor.stripColor(event.getView().getTitle());
        if (!title.contains("Total Level")) return;

        Inventory top = event.getView().getTopInventory();

        // Cancel any direct clicks inside the GUI to prevent item movement
        if (event.getClickedInventory() == top) {
            event.setCancelled(true);
            return;
        }

        // Block shift-clicks from moving items into the GUI
        if (event.isShiftClick()) {
            for (int i = 0; i < top.getSize(); i++) {
                ItemStack item = top.getItem(i);
                if (item == null || item.getType() == Material.AIR) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        String skillName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
        player.sendMessage("You clicked on: " + skillName); // TODO: Add chat message with ranks and levels - if not wanted, delete code up to event.setCancelled();
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        String title = ChatColor.stripColor(event.getView().getTitle());
        if (!title.contains("Total Level")) return;

        // Block all drag events
        for (int slot : event.getRawSlots()) {
            if (slot < event.getView().getTopInventory().getSize()) {
                event.setCancelled(true);
                return;
            }
        }
    }
}