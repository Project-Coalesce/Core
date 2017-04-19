package com.coalesce.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import net.minecraft.server.v1_11_R1.NBTTagByte;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.NBTTagDouble;
import net.minecraft.server.v1_11_R1.NBTTagInt;
import net.minecraft.server.v1_11_R1.NBTTagList;
import net.minecraft.server.v1_11_R1.NBTTagLong;
import net.minecraft.server.v1_11_R1.NBTTagString;
import net.minecraft.server.v1_11_R1.Tuple;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A wrapper for Bukkit's ItemStack class. Makes it easier to make an item in few lines.
 * @author PaulBGD
 */
@EqualsAndHashCode
public class ItemBuilder implements Cloneable {

    /**
     * Gets the data.
     *
     * @return the data
     */
    @Getter
    private short data;
    /**
     * Gets the type.
     *
     * @return the type
     */
    @Getter
    private Material type;
    /**
     * Gets the amount.
     *
     * @return the amount
     */
    @Getter
    private int amount;
    /**
     * Gets the title.
     *
     * @return the title
     */
    @Getter
    private String title = null;

    /**
     * Gets the lore.
     *
     * @return the lore
     */
    @Getter
    private List<String> lore = new ArrayList<>();

    /**
     * Gets all of the enchantments.
     *
     * @return the enchantments
     */
    @Getter
    private Map<Enchantment, Integer> allEnchantments = new HashMap<>();

    /**
     * Gets all of the Attributes.
     *
     * @return the attributes
     */
    @Getter
    private Map<Attribute, Tuple<Operation, Double>> allAttributes = new HashMap<>();

    /**
     * Gets all of the hidden flags.
     *
     * @return the hidden flags
     */
    @Getter
    private Set<Flag> allHiddenFlags = new HashSet<>();

    /**
     * Gets the color.
     *
     * @return the color
     */
    @Getter
    private Color color;

    /**
     * Gets the potion.
     *
     * @return the potion
     */
    @Getter
    private Potion potion;

    /**
     * Gets the owner.
     *
     * @return the owner
     */
    @Getter
    private String owner;

    /**
     * Checks if is shiny.
     *
     * @return true, if is shiny
     */
    @Getter
    private boolean shiny;

    /**
     * Checks if is unbreakable
     *
     * @return true, if is unbreakable
     */
    @Getter
    private boolean unbreakable;

    @Getter
    private final List<FireworkEffect> effects = new ArrayList<>(1);

    @Getter
    private final List<Page> pages = new ArrayList<>();

    private boolean changed = true;
    private ItemStack compiled;

    /**
     * Instantiates a new item builder.
     *
     * @param item the item
     */
    public ItemBuilder(ItemStack item) {
        this(item.getType(), item.getAmount(), item.getDurability());

        if (item.getItemMeta().hasDisplayName()) {
            this.title = item.getItemMeta().getDisplayName();
        }
        if (item.getItemMeta().hasLore()) {
            this.lore = item.getItemMeta().getLore();
        }
        this.allEnchantments = new HashMap<>(item.getEnchantments());
        if (item.getType().name().startsWith("LEATHER_")) {
            this.color = ((LeatherArmorMeta) item.getItemMeta()).getColor();
        } else if (item.getType() == Material.POTION) {
            // not supported
        } else if (item.getType() == Material.SKULL_ITEM && data == (short) 3) {
            this.owner = ((SkullMeta) item.getItemMeta()).getOwner();
        }
        net.minecraft.server.v1_11_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (nmsItem.hasTag() && nmsItem.getTag().hasKey("ench")) {
            // shiny :o
            this.shiny = true;
        }
        build();
    }

    /**
     * Instantiates a new item builder.
     *
     * @param mat the mat
     */
    public ItemBuilder(Material mat) {
        this(mat, 1);
    }

    /**
     * Instantiates a new item builder.
     *
     * @param mat    the mat
     * @param amount the amount
     */
    public ItemBuilder(Material mat, int amount) {
        this(mat, amount, (short) 0);
    }

    /**
     * Instantiates a new item builder.
     *
     * @param mat  the mat
     * @param data the data
     */
    public ItemBuilder(Material mat, short data) {
        this(mat, 1, data);
    }

    /**
     * Instantiates a new item builder.
     *
     * @param mat    the mat
     * @param amount the amount
     * @param data   the data
     */
    public ItemBuilder(Material mat, int amount, short data) {
        this.type = mat;
        this.amount = amount;
        this.data = data;
        build();
    }

    /**
     * Sets the type.
     *
     * @param mat the mat
     * @return the item builder
     */
    public ItemBuilder setType(Material mat) {
        if (this.type != mat) {
            this.changed = true;
        }
        this.type = mat;
        return this;
    }

    public ItemBuilder setData(short data) {
        if (this.data != data) {
            this.changed = true;
        }
        this.data = data;
        return this;
    }

    /**
     * Append title.
     *
     * @param appending the appending
     * @return the item builder
     */
    public ItemBuilder appendTitle(String appending) {
        if (StringUtils.length(appending) > 0) {
            this.changed = true;
        }
        this.title += appending;
        return this;
    }

    /**
     * Append title.
     *
     * @param appending the appending
     * @param color     the color
     * @return the item builder
     */
    public ItemBuilder appendTitle(String appending, ChatColor color) {
        this.changed = true;
        this.title += color + "" + ChatColor.BOLD + appending;
        return this;
    }

    /**
     * Sets the title.
     *
     * @param title the title
     * @return the item builder
     */
    public ItemBuilder setTitle(String title) {
        return this.setTitle(title, null);
    }

    /**
     * Sets the title.
     *
     * @param title the title
     * @param color the color
     * @return the item builder
     */
    public ItemBuilder setTitle(String title, ChatColor color) {
        String newTitle = (color != null ? color : "") + "" + title;
        if (!newTitle.equals(this.title)) {
            this.changed = true;
        }
        this.title = newTitle;
        return this;
    }

    /**
     * Sets the lore.
     *
     * @param lore  the lore
     * @param index the index
     * @return the item builder
     */
    public ItemBuilder setLore(String lore, int index) {
        if (this.lore.size() < index + 1) {
            this.lore.add(index, lore);
            this.changed = true;
        } else {
            String previous = this.lore.set(index, lore);
            if (!StringUtils.equals(lore, previous)) {
                this.changed = true;
            }
        }
        return this;
    }
    
    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    /**
     * Adds the lore.
     *
     * @param lore the lore
     * @return the item builder
     */
    public ItemBuilder addLore(String... lore) {
        if (lore.length > 0) {
            this.changed = true;
        }
        for (String line : lore) {
            this.lore.add(ChatColor.GRAY + line);
        }
        return this;
    }
    
    public ItemBuilder addLore(List<String> lore) {
        return addLore(lore.toArray(new String[lore.size()]));
    }
    
    /**
     * Replaces each instance of the supplied variable in the
     * item's lore with the supplied object
     * 
     * @param var the variable to look for i.e. %player%
     * @param replace the object to replace the variable 
     * with, i.e. player.getName()
     * 
     * @return the item builder
     */
    public ItemBuilder replaceLore(Object[] vars, Object[] replace) {
       /** List<String> newLore = new ArrayList<>(lore.size());
        for(int i = 0; i < vars.length; i++){
            for(String s : this.lore) {
                String line = s.replace(vars[i] + "", replace[i] + "");
                if (!s.equals(line) && !lore.contains(line)) {
                    newLore.add(line);
                } 
            }
        } */
        
        for (int i = 0; i < vars.length; i++) {
            replaceLore(vars[i].toString(), replace[i]);
        }
        return this;
    }
    
    public ItemBuilder replaceLore(String var, Object replace) {
        IntStream.range(0, lore.size()).forEach(i -> this.setLore(ChatColor.translateAlternateColorCodes('&', lore.get(i).replace(var, String.valueOf(replace))), i));
        this.changed = true;
        return this;
    }

    /**
     * Sets the shiny.
     *
     * @param shiny the shiny
     * @return the item builder
     */
    public ItemBuilder setShiny(boolean shiny) {
        if (this.shiny != shiny) {
            this.changed = true;
        }
        this.shiny = shiny;
        return this;
    }

    /**
     * Adds the enchantment.
     *
     * @param enchant the enchant
     * @param level   the level
     * @return the item builder
     */
    public ItemBuilder addEnchantment(Enchantment enchant, int level) {
        Integer put = allEnchantments.put(enchant, level);
        if (put == null || put != level || level == 0) { // if it wasn't in there, it returns 0 anyways. nothing we can do if we're putting in 0
            this.changed = true;
        }
        return this;
    }
    
    public ItemBuilder addEnchantments(int level, Enchantment... enchants) {
        for (Enchantment enchant : enchants) {
            addEnchantment(enchant, level);
        }
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        Integer remove = allEnchantments.remove(enchantment);
        if (remove >= 0) { // if it wasn't in there, it returns 0 anyways. nothing we can do if we're putting in 0
            this.changed = true;
        }
        return this;
    }

    /**
     * Sets the color.
     *
     * @param color the color
     * @return the item builder
     */
    public ItemBuilder setColor(Color color) {
        if (!this.type.name().contains("LEATHER_")) {
            throw new IllegalArgumentException("Can only dye leather armor!");
        }
        if (this.color != color) {
            this.changed = true;
        }
        this.color = color;
        return this;
    }

    /**
     * Sets the dye color.
     *
     * @param color the dye color
     * @return the item builder
     */
    public ItemBuilder setDyeColor(DyeColor color) {
        short durability;
        if (this.type == Material.INK_SACK) {
            durability = color.getDyeData();
        } else {
            durability = color.getWoolData();
        }
        return this.setData(durability);
    }

    /**
     * Sets the potion.
     *
     * @param potion the potion
     * @return the item builder
     */
    public ItemBuilder setPotion(Potion potion) {
        if (this.type != Material.POTION) {
            setType(Material.POTION);
        } else if (this.potion != potion) {
            this.changed = true;
        }
        this.potion = potion;
        return this;
    }

    /**
     * Sets the entity type
     *
     * @param type the entity type
     * @return the item builder
     */
    public ItemBuilder setEntityType(EntityType type) {
        return this.setData(type.getTypeId());
    }

    /**
     * Sets the amount.
     *
     * @param amount the amount
     * @return the item builder
     */
    public ItemBuilder setAmount(int amount) {
        if (this.amount != amount) {
            this.changed = true;
        }
        this.amount = amount;
        return this;
    }

    /**
     * Sets the owner.
     *
     * @param owner the owner
     * @return the item builder
     */
    public ItemBuilder setOwner(String owner) {
        if (!StringUtils.equals(owner, this.owner)) {
            this.changed = true;
        }
        this.owner = owner;
        return this;
    }

    public ItemBuilder addPage(String title, ChatColor titleColor, String... lines) {
        this.changed = true;
        StringBuilder text = new StringBuilder();
        for (String line : lines) {
            text.append("\n").append(line);
        }
        this.pages.add(new Page(title, titleColor, text));
        return this;
    }

    public ItemBuilder addEffect(FireworkEffect effect) {
        this.changed = true;
        this.effects.add(effect);
        return this;
    }

    public ItemBuilder addAttribute(Attribute att, Operation op, double value) {
        this.changed = true;
        allAttributes.put(att, new Tuple<>(op, value));
        return this;
    }

    public ItemBuilder hideFlags(Flag... flags) {
        this.changed = true;
        Arrays.stream(flags).forEach(allHiddenFlags::add);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        if (this.unbreakable != unbreakable) {
            this.changed = true;
        }
        this.unbreakable = unbreakable;
        return this;
    }

    /**
     * Builds the itemstack.
     *
     * @return the item stack
     */
    public ItemStack build() {
        if (!this.changed && this.compiled != null) { // null check, because possibilities
            return this.compiled;
        }

        Material mat = (this.type == null ? Material.AIR : this.type);
        
        ItemStack item = new ItemStack(mat, this.amount, this.data);
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) meta;
            if (this.title != null) {
                bookMeta.setTitle(title);
                bookMeta.setDisplayName(title);
            }
            
            List<String> pages = new ArrayList<>();
            for (Page page : this.pages) {
                StringBuilder pageBuilder = new StringBuilder();
                String[] words = page.getText().toString().split(" ");
                pageBuilder.append(page.getTitleColor()).append(ChatColor.BOLD).append(page.getTitle()).append(ChatColor.BLACK).append("\n").append(ChatColor.RESET);
                for (String word : words) {
                    if (pageBuilder.length() + word.length() + 1 > 240) {
                        pages.add(pageBuilder.toString());
                        pageBuilder = new StringBuilder();
                    }
                    pageBuilder.append(word).append(" ");
                }
                pages.add(pageBuilder.toString()); // for the last ones
            }
            bookMeta.setPages(pages);
        }
        if (this.title != null) {
            meta.setDisplayName(this.title);
        }
        if (!this.lore.isEmpty()) {
            meta.setLore(this.lore);
        }

        if (this.unbreakable) {
            meta.spigot().setUnbreakable(true);
        }

        if (meta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) meta).setColor(this.color);
        }
        if (owner != null && meta instanceof SkullMeta && this.data == (short) 3) {
            ((SkullMeta) meta).setOwner(owner);
        }
        
        if (meta instanceof FireworkMeta) {
            FireworkMeta fireworkMeta = (FireworkMeta) meta;
            fireworkMeta.addEffects(this.effects);
        }
        item.setItemMeta(meta);
        
        if (this.allEnchantments.size() > 0) {
            item.addUnsafeEnchantments(this.allEnchantments);
        }
        if (this.potion != null) {
            this.potion.apply(item);
        }
        
        if (this.allEnchantments.size() == 0 && this.shiny) {
            net.minecraft.server.v1_11_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
            NBTTagCompound tag = getTag(nmsStack);
            NBTTagList ench = new NBTTagList();
            tag.set("ench", ench);
            nmsStack.setTag(tag);
            item = CraftItemStack.asCraftMirror(nmsStack);
        }

        if (this.allAttributes.size() > 0) {
            net.minecraft.server.v1_11_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
            NBTTagCompound tag = getTag(nmsStack);
            NBTTagList mods = new NBTTagList();
            for (Map.Entry<Attribute, Tuple<Operation, Double>> entry : allAttributes.entrySet()) {
                String att = entry.getKey().getName();
                int op = entry.getValue().a().ordinal();
                double val = entry.getValue().b();
                UUID id = UUID.randomUUID();

                NBTTagCompound full = new NBTTagCompound();
                full.set("AttributeName", new NBTTagString(att));
                full.set("Name", new NBTTagString(att));
                full.set("Amount", new NBTTagDouble(val));
                full.set("Operation", new NBTTagInt(op));
                full.set("UUIDLeast", new NBTTagLong(id.getLeastSignificantBits()));
                full.set("UUIDMost", new NBTTagLong(id.getMostSignificantBits()));

                mods.add(full);
            }
            tag.set("AttributeModifiers", mods); // Removes existing AttributeModifiers tag
            nmsStack.setTag(tag);
            item = CraftItemStack.asCraftMirror(nmsStack);
        }

        if (allHiddenFlags.size() > 0) {
            net.minecraft.server.v1_11_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
            NBTTagCompound tag = getTag(nmsStack);

            byte b = 0;
            for (Flag f : allHiddenFlags) {
                b += f.getFlag();
            }
            tag.set("HideFlags", new NBTTagByte(b));

            nmsStack.setTag(tag);
            item = CraftItemStack.asCraftMirror(nmsStack);
        }

        this.compiled = item;
        this.changed = false;

        return item;
    }

    private NBTTagCompound getTag(net.minecraft.server.v1_11_R1.ItemStack nmsStack) {
        NBTTagCompound tag;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        } else {
            tag = nmsStack.getTag();
        }
        return tag;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public ItemBuilder clone() {
        ItemBuilder newBuilder = new ItemBuilder(this.type, this.amount, this.data);

        newBuilder.title = this.title;
        newBuilder.lore = new ArrayList<>(lore);
        newBuilder.allEnchantments = new HashMap<>(allEnchantments);
        newBuilder.allAttributes = new HashMap<>(allAttributes);
        newBuilder.allHiddenFlags = new HashSet<>(allHiddenFlags);
        newBuilder.color = color;
        newBuilder.potion = potion;
        newBuilder.owner = owner;
        newBuilder.shiny = shiny;
        newBuilder.unbreakable = unbreakable;
        newBuilder.changed = true;
        
        return newBuilder;
    }

    /**
     * Checks for enchantment.
     *
     * @param enchant the enchant
     * @return true, if successful
     */
    public boolean hasEnchantment(Enchantment enchant) {
        return this.allEnchantments.containsKey(enchant);
    }

    /**
     * Gets the enchantment level.
     *
     * @param enchant the enchant
     * @return the enchantment level
     */
    public int getEnchantmentLevel(Enchantment enchant) {
        return this.allEnchantments.get(enchant);
    }

    /**
     * Checks if is item.
     *
     * @param item the item
     * @return true, if is item
     */
    public boolean isItem(ItemStack item) {
        if (item == null) {
            return this.type == Material.AIR;
        }
        ItemMeta meta = item.getItemMeta();
        if (item.getType() != this.type) {
            return false;
        }
        if (!meta.hasDisplayName() && this.getTitle() != null) {
            return false;
        }
        if (meta.getDisplayName() != null && !meta.getDisplayName().equals(this.getTitle())) {
            return false;
        }
        if (!meta.hasLore() && !this.getLore().isEmpty()) {
            return false;
        }
        if (meta.hasLore()) {
            for (String lore : meta.getLore()) {
                if (!this.getLore().contains(lore)) {
                    return false;
                }
            }
        }
        if (meta instanceof SkullMeta && ((SkullMeta) meta).hasOwner() && !((SkullMeta) meta).getOwner().equals(this.owner)) {
            return false;
        } 
        if (!(meta instanceof PotionMeta) && this.potion != null) {
            return false;
        }
        for (Enchantment enchant : item.getEnchantments().keySet()) {
            if (!this.hasEnchantment(enchant)) {
                return false;
            }
        }

        if (unbreakable != item.getItemMeta().spigot().isUnbreakable()) return false;

        net.minecraft.server.v1_11_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return !(!shiny && nmsItem.hasTag() && nmsItem.getTag().hasKey("ench"));

        //TODO add attribute and hidden tag checks
    }

    @lombok.Data
    private static class Page {
        private final String title;
        private final ChatColor titleColor;

        private final StringBuilder text;
    }

    @lombok.AllArgsConstructor
    public static enum Attribute {
        MAX_HEALTH("generic.maxHealth"),
        KNOCKBACK_RESISTANCE("generic.knockbackResistance"),
        MOVEMENT_SPEED("generic.movementSpeed"),
        ATTACK_DAMAGE("generic.attackDamage");

        @Getter
        private String name;
    }

    public static enum Operation {
        ADD,
        ADD_PERCENTAGE,
        MULTIPLY_PERCENTAGE;
    }

    public static enum Flag {
        ENCHANTMENTS,
        ATTRIBUTES,
        UNBREAKABLE,
        CAN_DESTROY,
        CAN_PLACE_ON,
        OTHERS;

        public byte getFlag() {
            return (byte) (1 << ordinal());
        }
    }
}
