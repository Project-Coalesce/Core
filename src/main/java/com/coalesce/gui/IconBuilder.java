package com.coalesce.gui;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.function.BiConsumer;

public final class IconBuilder {

	private Icon icon;

	public IconBuilder(Material material){

		icon = new Icon(material);
	}

	public IconBuilder withAmount(int amount){
		icon.setAmount(amount);
		return this;
	}

	public IconBuilder withDurability(short durability){
		icon.setDurability(durability);
		return this;
	}

	public IconBuilder withDurability(int durability){
		icon.setDurability((short)durability);
		return this;
	}

	public IconBuilder withName(String displayName){
		icon.setDisplayName(displayName);
		return this;
	}

	public IconBuilder withLore(String... lore){
		icon.setLore(lore);
		return this;
	}

	public IconBuilder withLore(List<String> lore){
		icon.setLore(lore);
		return this;
	}

	public IconBuilder withItemFlag(ItemFlag itemFlag){
		icon.addItemFlag(itemFlag);
		return this;
	}

	public IconBuilder withEnchantment(Enchantment enchantment, int level){
		icon.getItemStack().addEnchantment(enchantment, level);
		return this;
	}

	public IconBuilder withClickCallback(BiConsumer<Player, ClickType> clickCallback){
		icon.setPlayerClickCallback(clickCallback);
		return this;
	}

	public Icon build(){
		return icon;
	}

}
