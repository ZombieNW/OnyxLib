package com.zombienw.onyxLib;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class OnyxLib {

    private final OnyxLibPlugin plugin;
    private final ItemService itemService;
    private final Map<String, OnyxNamespace> namespaces = new HashMap<>();

    OnyxLib(OnyxLibPlugin plugin) {
        this.plugin = plugin;
        this.itemService = new ItemService(plugin);

        Bukkit.getPluginManager().registerEvents(
                new ItemInteractionListener(itemService), plugin
        );
    }

    public OnyxLibPlugin getPlugin() { return plugin; }

    public ItemService items() { return itemService; }

    public OnyxNamespace namespace(String namespace) {
        return namespaces.computeIfAbsent(
                namespace.toLowerCase(),
                ns -> new OnyxNamespace(ns, this)
        );
    }
}