package com.coalesce.gui;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemBuilder {

	private ItemStack itemStack;

	public ItemBuilder(Material material) {
		this.itemStack = new ItemStack(material);
	}

	public ItemBuilder amount(int amount){
		itemStack.setAmount(amount);
		return this;
	}

	public ItemBuilder durability(int durability){
		itemStack.setDurability((short)durability);
		return this;
	}

	public ItemBuilder displayName(String displayName){
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(displayName);
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder lore(String... lore){
		ItemMeta meta = itemStack.getItemMeta();
		meta.setLore(Stream.of(lore).collect(Collectors.toList()));
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder lore(List<String> lore){
		ItemMeta meta = itemStack.getItemMeta();
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder itemFlags(ItemFlag... flags){
		ItemMeta meta = itemStack.getItemMeta();
		meta.addItemFlags(flags);
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder enchant(Enchantment enchantment, int level){
		itemStack.addEnchantment(enchantment, level);
		return this;
	}

	public ItemStack build(){
		return itemStack;
	}
}