package com.zombienw.onyxlib;

import com.zombienw.onyxlib.impl.item.OnyxItemImpl;
import com.zombienw.onyxlib.impl.pack.PackGenerator;
import com.zombienw.onyxlib.impl.registry.NamespaceRegistry;
import com.zombienw.onyxlib.impl.registry.OnyxNamespaceImpl;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class OnyxCommand implements CommandExecutor, TabCompleter {

    private final OnyxPlugin plugin;

    public OnyxCommand(OnyxPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /onyx <give|generatePack>");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("generatepack")) {
            if (!sender.hasPermission("onyxlib.admin")) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }

            sender.sendMessage(ChatColor.YELLOW + "Starting pack generation...");

            // Run asynchronously to prevent freezing the server during heavy IO operations
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    PackGenerator generator = new PackGenerator(plugin);
                    String result = generator.generate();
                    sender.sendMessage(ChatColor.GREEN + result);
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Pack generation failed! Check console.");
                    e.printStackTrace();
                }
            });
            return true;
        }

        if (subCommand.equals("give")) {
            if (!sender.hasPermission("onyxlib.admin")) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }

            if (!(sender instanceof Player player)) {
                sender.sendMessage(ChatColor.RED + "Only players can receive items.");
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /onyx give <namespace:item_id> [amount]");
                return true;
            }

            NamespacedKey key = NamespacedKey.fromString(args[1]);
            if (key == null) {
                sender.sendMessage(ChatColor.RED + "Invalid item key format.");
                return true;
            }

            OnyxItemImpl item = NamespaceRegistry.getItem(key);
            if (item == null) {
                sender.sendMessage(ChatColor.RED + "Unknown OnyxLib item: " + key);
                return true;
            }

            int amount = 1;
            if (args.length >= 3) {
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException ignored) {}
            }

            ItemStack stack = item.create(amount);
            player.getInventory().addItem(stack);
            player.sendMessage(ChatColor.GREEN + "Given " + amount + "x " + key);
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("give");
            completions.add("generatePack");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            for (OnyxNamespaceImpl ns : NamespaceRegistry.getAllNamespaces()) {
                for (OnyxItemImpl item : ns.getItems()) {
                    completions.add(item.getKey().toString());
                }
            }
        }

        // Filter completions based on what the user has already typed
        String currentArg = args[args.length - 1].toLowerCase();
        return completions.stream()
                .filter(c -> c.toLowerCase().startsWith(currentArg))
                .toList();
    }
}
