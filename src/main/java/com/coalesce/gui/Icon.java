package com.coalesce.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Icon {

    protected ItemStack itemStack;

    public Icon(ItemStack itemStack) {

        this.itemStack = itemStack;

    }

    public Icon(ItemStack itemStack, String name) {

        this.itemStack = itemStack;
        setDisplayName(name);
    }

    public Icon(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public Icon(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public Icon(Material material, int amount, int damage) {
        this.itemStack = new ItemStack(material, amount, (short) damage);
    }

    public Icon(Material material, int amount, int damage, String name) {
        this.itemStack = new ItemStack(material, amount, (short) damage);

        setDisplayName(name);
    }

    public Icon(Icon that) {

        this.itemStack = that.itemStack;

    }

    //Click Methods
    //These are all empty because they are meant to be overridden
    public void onClick(Player whoClicked, ClickType clickType) {

    }

    //Getters and Setters
    @NotNull
    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Material getMaterial() {
        return itemStack.getType();
    }

    public void setMaterial(Material material) {
        itemStack.setType(material);
    }

    public int getAmount() {
        return itemStack.getAmount();
    }

    public void setAmount(int amount) {
        itemStack.setAmount(amount);
    }

    public List<String> getLore() {
        return itemStack.getItemMeta().getLore() == null ? new ArrayList<>() : itemStack.getItemMeta().getLore();
    }

    public void setLore(List<String> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    public void setLore(String... lore) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(meta);
    }

    public String getDisplayName() {
        return itemStack.getItemMeta().getDisplayName();
    }

    public void setDisplayName(String displayName) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        itemStack.setItemMeta(meta);
    }

    public void addItemFlag(ItemFlag itemFlag) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(itemFlag);
        itemStack.setItemMeta(meta);

    }

}
