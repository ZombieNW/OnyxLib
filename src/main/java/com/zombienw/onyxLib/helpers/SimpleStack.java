package com.zombienw.onyxLib.helpers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SimpleStack {

    private final Material material;
    private int amount = 1;
    private Component displayName;

    private SimpleStack(Material material) {
        this.material = material;
    }

    public static SimpleStack builder(Material material) {
        return new SimpleStack(material);
    }

    public SimpleStack amount(int amount) {
        this.amount = amount;
        return this;
    }

    public SimpleStack name(String name) {
        // Using !decoration to remove the default italicization of renamed items
        this.displayName = Component.text(name).decoration(TextDecoration.ITALIC, false);
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (displayName != null) {
                meta.displayName(displayName);
            }
            item.setItemMeta(meta);
        }

        return item;
    }
}
