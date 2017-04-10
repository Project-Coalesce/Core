package com.coalesce.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class IconMenu implements InventoryHolder, Cloneable {

    private Icon[][] icons;
    private String title;
    private int rows;

    private Inventory inventory;

    // Some settings for when the plugin is clicked outside of, or something
    // like that
    public boolean closeOnOutsideClick = true;
    public boolean canEditInventory = false;

    public IconMenu(String title, int rows) {

        this.rows = rows;
        this.title = title;

        inventory = Bukkit.getServer().createInventory(this, rows * 9, title);
        icons = new Icon[9][rows];
    }

    public IconMenu(String title, int rows, boolean closeOnOutsideClick) {
        this.closeOnOutsideClick = closeOnOutsideClick;
        this.rows = rows;
        this.title = title;

        inventory = Bukkit.getServer().createInventory(this, rows * 9, title);
        icons = new Icon[9][rows];
    }

    public IconMenu(String title, int rows, boolean closeOnOutsideClick, boolean canEditInventory) {
        this.closeOnOutsideClick = closeOnOutsideClick;
        this.canEditInventory = canEditInventory;
        this.rows = rows;
        this.title = title;

        inventory = Bukkit.getServer().createInventory(this, rows * 9, title);
        icons = new Icon[9][rows];
    }

    public IconMenu(IconMenu original) {
        this.closeOnOutsideClick = original.closeOnOutsideClick;
        this.canEditInventory = original.closeOnOutsideClick;
        this.rows = original.getRows();
        this.title = original.getTitle();

        inventory = original.getInventory();
        icons = original.icons;
    }

    //Overrideable meathods
    //These meathods are ment to be overridden
    public void onClose(Player whoClosed) {

    }

    public void onOpen(Player whoOpened) {

    }

    // Pretty-ness meathods
    // These meathods allow you to do things to menus faster
    public void fillMenu(Icon icon) {

        for (int i = 0; i < icons.length; i++) {
            for (int k = 0; k < icons[i].length; k++) {

                setIcon(icon, i, k);

            }
        }
    }

    public void fillRow(Icon icon, int row) {

        if (row > (icons[0].length - 1)) {
            return;
        }

        for (int i = 0; i < icons.length; i++) {
            setIcon(icon, i, row);
        }

    }

    public void fillColumn(Icon icon, int column) {
        if (column > (icons.length - 1)) {
            return;
        }

        for (int i = 0; i < icons[0].length; i++) {
            setIcon(icon, column, i);
        }
    }

    public void fillbackground(Icon icon) {
        for (int i = 0; i < icons.length; i++) {
            for (int k = 0; k < icons[i].length; k++) {

                if (getIcon(i, k) == null) {
                    setIcon(icon, i, k);
                }
            }
        }
    }

    // player methods
    public boolean hasThisOpen(Player player) {
        return player.getOpenInventory() == this.getInventory();
    }

    public void openForPlayer(Player player) {
        onOpen(player);
        player.openInventory(inventory);
    }

    public void closeForPlayer(Player player) {
        player.closeInventory();
    }

    public void clickMenuItem(Player player, InventoryClickEvent event, int slot) {

        int y = ((slot) / 9);
        int x = (slot - ((y * 9)));

        Icon clickedIcon = icons[x][y];
        ClickType click = event.getClick();

        clickedIcon.getPlayerClickCallback().accept((Player) event.getWhoClicked(), event.getClick());
    }

    // Numbering starts at the bottom, left to right

    // getters and setters
    public ArrayList<ItemStack> getItems(int x1, int y1, int x2, int y2) {

        ArrayList<ItemStack> items = new ArrayList<ItemStack>();

        for (int i = x1; i < x2 + 1; i++) {
            for (int k = y1; k < y2 + 1; k++) {

                ItemStack item = inventory.getItem((k * 9) + i);
                if (item != null) {
                    items.add(item);
                }

            }
        }
        return items;
    }

    public Icon getIcon(int x, int y) {
        return icons[x][y];
    }

    public void setIcon(Icon icon, int x, int y) {
        icons[x][y] = icon;
        inventory.setItem((y * 9) + x, (icon == null) ? null : icon.getItemStack());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Inventory newInv = Bukkit.getServer().createInventory(this, rows * 9, title);

        newInv.setContents(inventory.getContents());

        inventory = newInv;

        this.title = title;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
