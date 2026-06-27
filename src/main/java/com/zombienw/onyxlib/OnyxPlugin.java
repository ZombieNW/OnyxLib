package com.zombienw.onyxlib;

import com.zombienw.onyxlib.impl.block.OnyxBlockListener;
import com.zombienw.onyxlib.impl.registry.NamespaceRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The actual JavaPlugin for OnyxLib.
 * Manages lifecycle and triggers event listeners.
 */
public class OnyxPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new OnyxBlockListener(this), this);

        // Register command
        OnyxCommand command = new OnyxCommand(this);
        PluginCommand pluginCommand = getCommand("onyx");
        if (pluginCommand == null) throw new RuntimeException("OnyxLib command 'onyx' not found.");
        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);


        getLogger().info("OnyxLib has been initialized.");
    }

    @Override
    public void onDisable() {
        // prevent memory leak on reload
        NamespaceRegistry.clear();
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        getLogger().info("Server loaded. Locking OnyxLib namespaces.");
        NamespaceRegistry.lockAll();
    }
}
