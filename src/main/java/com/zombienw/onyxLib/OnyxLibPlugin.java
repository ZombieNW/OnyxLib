package com.zombienw.onyxLib;

import org.bukkit.plugin.java.JavaPlugin;

public final class OnyxLibPlugin extends JavaPlugin {
    private static OnyxLibPlugin instance;
    private OnyxLib api;

    @Override
    public void onEnable() {
        this.api = new OnyxLib(this);
        OnyxLibProvider.set(this.api);
        getLogger().info("OnyxLib enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("OnyxLib disabled.");
    }

    public static OnyxLibPlugin getInstance() {
        return instance;
    }

    public OnyxLib getApi() {
        return api;
    }
}
