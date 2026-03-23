package com.zombienw.onyxLib;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class OnyxLib {

    private final OnyxLibPlugin plugin;
    private final ItemService itemService;
    private final PackGenerator packGenerator;
    private final Map<String, OnyxNamespace> namespaces = new HashMap<>();

    OnyxLib(OnyxLibPlugin plugin) {
        this.plugin = plugin;
        this.itemService = new ItemService(plugin);
        this.packGenerator = new PackGenerator(itemService, plugin.getDataFolder());

        OnyxCommand giveCommand = new OnyxCommand(itemService, packGenerator);
        var onyxCommand = plugin.getCommand("onyx");
        if (onyxCommand != null) {
            onyxCommand.setExecutor(giveCommand);
            onyxCommand.setTabCompleter(giveCommand);
        }
    }

    public OnyxLibPlugin getPlugin() { return plugin; }

    public ItemService items() { return itemService; }

    public OnyxNamespace namespace(String namespace, JavaPlugin owningPlugin) {
        return namespaces.computeIfAbsent(
                namespace.toLowerCase(),
                ns -> new OnyxNamespace(ns, this, owningPlugin)
        );
    }
}