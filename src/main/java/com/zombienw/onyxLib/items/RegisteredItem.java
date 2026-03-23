package com.zombienw.onyxLib.items;

import org.bukkit.plugin.java.JavaPlugin;

public class RegisteredItem {

    private final String fullId;
    private final String namespace;
    private final CustomItem item;
    private final JavaPlugin owningPlugin;

    public RegisteredItem(String namespace, CustomItem item, JavaPlugin owningPlugin) {
        this.namespace = namespace;
        this.item = item;
        this.fullId = namespace + ":" + item.getId();
        this.owningPlugin = owningPlugin;
    }

    public String getFullId() { return fullId; }
    public String getNamespace() { return namespace; }
    public CustomItem getItem() { return item; }
    public JavaPlugin getOwningPlugin() { return owningPlugin; }


    @Override
    public String toString() {
        return "RegisteredItem{" + fullId + "}";
    }
}
