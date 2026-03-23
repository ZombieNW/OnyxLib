package com.zombienw.onyxLib.blocks;

import org.bukkit.plugin.java.JavaPlugin;

public class RegisteredBlock {

    private final String fullId;
    private final String namespace;
    private final CustomBlock block;
    private final JavaPlugin owningPlugin;

    public RegisteredBlock(String namespace, CustomBlock block, JavaPlugin owningPlugin) {
        this.namespace = namespace;
        this.block = block;
        this.fullId = namespace + ":" + block.getId();
        this.owningPlugin = owningPlugin;
    }

    public String getFullId() { return fullId; }
    public String getNamespace() { return namespace; }
    public CustomBlock getBlock() { return block; }
    public JavaPlugin getOwningPlugin() { return owningPlugin; }

    @Override
    public String toString() { return "RegisteredBlock{" + fullId + "}"; }
}
