package com.coalesce.gui;

import com.coalesce.plugin.CoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class SinglePlayerGui implements Listener, Gui<Function<Player, ItemStack>, SinglePlayerGui>, InventoryHolder {

	/**
	 * The plugin
	 */
	protected final CoPlugin plugin;

	/**
	 * The size of the inventory
	 */
	protected final int size;
	/**
	 * The function to apply to get the title
	 */
	protected final Function<Player, String> title;
	/**
	 * The functions to apply to get each item
	 */
	private final Function<Player, ItemStack>[] items;

	/**
	 * The consumers to run when each item is clicked
	 */
	private final Consumer<InventoryClickEvent>[] listeners;

	/**
	 * The inventory
	 */
	private final Inventory inventory;

	/**
	 * The player who this inventory belongs to
	 */
	private final Player player;

	public SinglePlayerGui(CoPlugin plugin, int size, Function<Player, String> title, Player player) {

		this.player = player;
		this.plugin = plugin;
		this.size = size;
		this.title = title;
		this.items = new Function[size];
		this.listeners = new Consumer[size];

		//Using this class as an inventory holder allows for easier identification of the inventory
		inventory = Bukkit.createInventory(this, size, title.apply(player));

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public SinglePlayerGui(CoPlugin plugin, int size, String title, Player player) {
		this(plugin, size, p -> title, player);
	}

	public void open() {
		player.openInventory(inventory);
	}

	public void update() {
		for (int i = 0; i < items.length; i++) {
			Function<Player, ItemStack> function = items[i];
			if (function != null) {
				ItemStack item = function.apply(player);
				inventory.setItem(i, item);
			}
		}
		player.updateInventory();
	}

	@Override
	public SinglePlayerGui addItem(Function<Player, ItemStack> item, Consumer<InventoryClickEvent> onClick) {
		int i = inventory.firstEmpty();
		this.items[i] = item;
		this.listeners[i] = onClick;
		return this;
	}

	@Override
	public SinglePlayerGui setItem(int index, Function<Player, ItemStack> item, Consumer<InventoryClickEvent> onClick) {
		this.items[index] = item;
		this.listeners[index] = onClick;
		return this;
	}

	public Inventory getInventory() {
		return inventory;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {

		//Make sure that the inventory exists and that the inventory is our inventory
		if (event.getInventory() == null || !(event.getInventory().getHolder().equals(this))) {
			return;
		}

		event.setCancelled(true);
		Consumer<InventoryClickEvent> onClick = listeners[event.getSlot()];
		if (onClick != null) {
			try {
				onClick.accept(event);
			} catch (Exception e) {
				throw new RuntimeException("Failed to handle inventory click event", e);
			}
		}

	}

}
