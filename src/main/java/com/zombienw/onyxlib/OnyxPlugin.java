package com.zombienw.onyxlib;

import com.zombienw.onyxlib.impl.block.OnyxBlockListener;
import com.zombienw.onyxlib.impl.registry.NamespaceRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
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
        PluginCommand pluginCommand = getCommand("onyx");
        if (pluginCommand == null) {
            getLogger().severe("Command '/onyx' is missing from plugin.yml");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        OnyxCommand command = new OnyxCommand(this);
        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);

        getLogger().info("OnyxLib has been initialized.");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((JavaPlugin) this);
        NamespaceRegistry.clear();
        getLogger().info("OnyxLib namespaces cleared and plugin disabled.");
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        getLogger().info("Server startup complete. Locking OnyxLib namespaces.");
        NamespaceRegistry.lockAll();
    }
}
