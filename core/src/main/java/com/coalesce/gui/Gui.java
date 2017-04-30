package com.coalesce.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

/**
 * Provides a general contract for a graphical user interface based around icons arrayed on a menu.
 *
 * @param <F> The type of clickable icons.
 * @param <T> The type of this gui (used to allow chaining method calls).
 */
public interface Gui<F, T>
{

    /**
     * Add an item to the first available slot, and set the method to run when the item is clicked
     * 
     * @param item the item
     * @param onClick the Consumer to run on click
     * 
     * @return this, for chaining
     */
    T addItem(F item, Consumer<InventoryClickEvent> onClick);

    /**
     * Set an item in the given slot, and set the method to run when the item is clicked
     * 
     * @param index the index in the inventory
     * @param item the item
     * @param onClick the Consumer to run on click
     * 
     * @return this, for chaining
     */
    T setItem(int index, F item, Consumer<InventoryClickEvent> onClick);

    /**
     * Open the inventory
     * 
     * @param player the player
     */
    void open(Player player);

    /**
     * Clear the inventory(s)
     */
    void clear();

}