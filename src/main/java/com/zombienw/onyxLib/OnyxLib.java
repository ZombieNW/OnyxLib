package com.zombienw.onyxLib;

import org.bukkit.plugin.java.JavaPlugin;

public record OnyxLib(JavaPlugin plugin) {

    public void hello() {
        plugin.getLogger().info("Hello from OnyxLib API");
    }
}