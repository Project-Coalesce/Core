package com.coalesce.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Icon {
	
	protected ItemStack item;
	
	public Icon(ItemStack item) {

		this.item = item;

	}
	
	public Icon(ItemStack item, String name){
		
		this.item = item;
		
		setDisplayName(name);
	}
	
	public Icon(Material material){
        this.item = new ItemStack(material);
	}

	public Icon(Material material, int amount){
        this.item = new ItemStack(material, amount);
	}

    public Icon(Material material, int amount, int damage){
        this.item = new ItemStack(material, amount, (short) damage);
    }

    public Icon(Material material, int amount, int damage, String name){
        this.item = new ItemStack(material, amount, (short) damage);

        setDisplayName(name);
    }
	
	public Icon(Icon that){
		
		this.item = that.item;
		item.setType(that.getMaterial());
		item.setAmount(that.getAmount());
		setDisplayName(that.getDisplayName());
		setLore(that.getLore());
		
	}

	//Click Methods
	//These are all empty because they are meant to be overridden
	public void onClick(Player player){
		
	}
	
	public void onRightClick(Player player){
		
	}
	
	public void onLeftClick(Player player){
		
	}
	
	public void onShiftRightClick(Player player){
		
	}
	
	public void onShiftLeftClick(Player player){
		
	}
	
	public void onMiddleMouseClick(Player player){
		
	}
	
	//Getters and Setters
	public ItemStack getItem() {
		if (item != null){
			return item;
		}
		return null;
	}
	
	public boolean hasItem(){
		if (item != null){
			return true;
		}
		return false;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public Material getMaterial() {
		return item.getType();
	}

	public void setMaterial(Material material) {
		item.setType(material);
	}

	public int getAmount() {
		return item.getAmount();
	}

	public void setAmount(int amount) {
		item.setAmount(amount);
	}

	public List<String> getLore() {
		return item.getItemMeta().getLore() == null ? new ArrayList<>() : item.getItemMeta().getLore();
	}

	public void setLore(List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

    public void setLore(String... lore){
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
    }

	public String getDisplayName() {
		return item.getItemMeta().getDisplayName();
	}

	public void setDisplayName(String displayName) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		item.setItemMeta(meta);
	}

    public void addItemFlag(ItemFlag itemFlag){
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(itemFlag);
        item.setItemMeta(meta);

    }
	
}
