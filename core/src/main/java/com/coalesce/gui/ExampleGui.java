package com.coalesce.gui;

import com.coalesce.plugin.CoPlugin;
import org.bukkit.inventory.ItemStack;

/**
 * A contrived example to display the usage of {@link PlayerGui}.
 */
public class ExampleGui extends PlayerGui {

    private static final int SIZE = 9;
    
    public ExampleGui(CoPlugin plugin) {
        super(plugin, SIZE, "Shop"); 
        
        ItemStack[] items = new ItemStack[0]; // some series of item stacks to display 
        for (ItemStack stack : items) {
            
            setItem(0, player -> {
                // can do per-player things here, like displaying their currency or some such
                int money = 42; // someCurrencyApi.getBalance(someCurrency, player);
                return new ItemBuilder(stack.getType())
                        .displayName("Balance: " + money)
                        .lore(" ", "Click to buy", " ", "Costs x", " ", "You have $" + money)
                        .build();
            }, (p, c) -> {
                // on click
                // update user's balance, add item to inventory, etc.
            });
        }
    }
}