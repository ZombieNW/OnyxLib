package com.zombienw.onyxLib.items;

import com.zombienw.onyxLib.core.CMDRegistry;
import com.zombienw.onyxLib.OnyxLibPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ItemService {
    private final Map<String, RegisteredItem> items = new HashMap<>();
    private final NamespacedKey itemIdKey;
    private final CMDRegistry cmdRegistry = new CMDRegistry();

    public ItemService(OnyxLibPlugin plugin) {
        this.itemIdKey = new NamespacedKey(plugin, "item_id");
    }

    /// Registration
    public RegisteredItem register(String namespace, CustomItem item, JavaPlugin owningPlugin) {
        String fullId = namespace + ":" + item.getId();

        if (items.containsKey(fullId)) {
            throw new IllegalArgumentException("Duplicate custom item id: " + fullId);
        }

        RegisteredItem registered = new RegisteredItem(namespace, item, owningPlugin);
        items.put(fullId, registered);
        return registered;
    }

    /// Lookup
    public Optional<RegisteredItem> get(String fullId) {
        return Optional.ofNullable(items.get(fullId));
    }

    public Collection<RegisteredItem> all() {
        return Collections.unmodifiableCollection(items.values());
    }

    /// ItemStack Creation
    public ItemStack create(String fullId) {
        return create(fullId, 1);
    }

    public ItemStack create(String fullId, int amount) {
        RegisteredItem registered = items.get(fullId);
        if (registered == null) {
            throw new IllegalArgumentException("Unknown custom item id: " + fullId);
        }

        ItemStack stack = registered.getItem().cloneTemplate();
        stack.setAmount(Math.max(1, amount));

        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return stack;

        // Stamp PDC tag to identify the item
        meta.getPersistentDataContainer().set(itemIdKey, PersistentDataType.STRING, fullId);

        // Stamp CMD for pretty visuals :3
        if (registered.getItem().hasAsset()) {
            CustomModelDataComponent cmd = meta.getCustomModelDataComponent();
            String modelPath = registered.getNamespace() + ":item/" + registered.getItem().getId();
            cmd.setStrings(List.of(modelPath));
            meta.setCustomModelDataComponent(cmd);
        }

        stack.setItemMeta(meta);
        return stack;
    }

    /// Identification
    public Optional<String> getId(ItemStack stack) {
        if (stack == null || stack.getType() == Material.AIR || !stack.hasItemMeta()) {
            return Optional.empty();
        }
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return Optional.empty();
        return Optional.ofNullable(
                meta.getPersistentDataContainer().get(itemIdKey, PersistentDataType.STRING)
        );
    }

    public Optional<RegisteredItem> fromStack(ItemStack stack) {
        return getId(stack).flatMap(this::get);
    }

    // isCustomItem(stack) - is it an onyxlib item at all
    public boolean isCustomItem(ItemStack stack) {
        return getId(stack).isPresent();
    }

    // is(stack, "plugin:sword") - is it this specific item
    public boolean is(ItemStack stack, String fullId) {
        return getId(stack).map(id -> id.equals(fullId)).orElse(false);
    }

    // isInNameSpace(stack, "plugin") - is it an item from this plugin
    public boolean isInNamespace(ItemStack stack, String namespace) {
        return getId(stack).map(id -> id.startsWith(namespace + ":")).orElse(false);
    }
}
