package com.gameszaum.login.spigot.api.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder create(Material materialType) {
        itemStack = new ItemStack(materialType);
        itemMeta = itemStack.getItemMeta();
        return this;
    }

    public ItemBuilder display(String name) {
        itemMeta.setDisplayName(name);
        return this;
    }

    public ItemBuilder changeId(int id) {
        itemStack.setDurability((short) id);
        return this;
    }

    public ItemBuilder amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder lore(String[] lore) {
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        itemMeta.setLore(new ArrayList<String>(lore));
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder unbreakable() {
        itemMeta.spigot().setUnbreakable(true);
        return this;
    }

    public ItemBuilder breakable() {
        itemMeta.spigot().setUnbreakable(false);
        return this;
    }

    /* building the item */

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}