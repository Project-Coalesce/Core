package com.coalesce.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;

public class IconMenuListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getInventory().getHolder() instanceof IconMenu) {
            IconMenu menu = (IconMenu) event.getInventory().getHolder();

            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();

                if (event.getSlotType().equals(SlotType.OUTSIDE)) {

                    if (menu.closeOnOutsideClick) {
                        menu.closeForPlayer(player);
                    }
                } else {
                    // This means it was in our menu
                    if (event.getRawSlot() < (menu.getRows() * 9)) {

                        int y = ((event.getSlot()) / 9);
                        int x = (event.getSlot() - ((y * 9)));

                        menu.clickMenuItem(player, event, event.getSlot());

                    } else {
                        if (!menu.canEditInventory) {
                            if (menu.closeOnOutsideClick) {
                                menu.closeForPlayer(player);
                            }
                        } else {
                            return;
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryOpen(InventoryOpenEvent event) {

        if (event.getInventory().getHolder() instanceof IconMenu) {
            IconMenu menu = (IconMenu) event.getInventory().getHolder();

            menu.onOpen((Player) event.getPlayer());

        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {

        if (event.getInventory().getHolder() instanceof IconMenu) {
            IconMenu menu = (IconMenu) event.getInventory().getHolder();

            menu.onClose((Player) event.getPlayer());

        }
    }

}
