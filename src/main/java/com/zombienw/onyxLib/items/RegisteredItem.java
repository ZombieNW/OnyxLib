package com.zombienw.onyxLib.items;

import org.bukkit.plugin.java.JavaPlugin;

public class RegisteredItem {

    private final String fullId;
    private final String namespace;
    private final CustomItem item;
    private final JavaPlugin owningPlugin;
    private int assignedCmd = 0;

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

    // CMD Value assigned at registration. 0 if no asset
    public int getAssignedCmd() { return assignedCmd; }
    public boolean hasCmd() { return assignedCmd != 0; }
    void setAssignedCmd(int cmd) { this.assignedCmd = cmd; }


    @Override
    public String toString() {
        return "RegisteredItem{" + fullId + "}";
    }
}
