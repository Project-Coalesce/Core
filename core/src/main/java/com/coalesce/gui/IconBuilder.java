package com.coalesce.gui;

import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public final class IconBuilder {

	private Icon icon;

	public IconBuilder(Material material){

		icon = new Icon(material);
	}
	
	public IconBuilder(ItemStack stack)
	{
	    icon = new Icon(stack);
	}

	public IconBuilder amount(int amount){
		icon.setAmount(amount);
		return this;
	}

	public IconBuilder durability(short durability){
		icon.setDurability(durability);
		return this;
	}

	public IconBuilder durability(int durability){
		icon.setDurability((short)durability);
		return this;
	}

	public IconBuilder name(String displayName){
		icon.setDisplayName(displayName);
		return this;
	}

	public IconBuilder lore(String... lore){
		icon.setLore(lore);
		return this;
	}

	public IconBuilder lore(List<String> lore){
		icon.setLore(lore);
		return this;
	}

	public IconBuilder itemFlag(ItemFlag itemFlag){
		icon.addItemFlag(itemFlag);
		return this;
	}

	public IconBuilder enchantment(Enchantment enchantment, int level){
		icon.getItemStack().addEnchantment(enchantment, level);
		return this;
	}

	public IconBuilder onClick(BiConsumer<Player, ClickType> clickCallback){
		icon.setOnClick(clickCallback);
		return this;
	}

	public Icon build(){
		return icon;
	}

}
