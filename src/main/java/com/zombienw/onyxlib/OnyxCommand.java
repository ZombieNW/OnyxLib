package com.zombienw.onyxlib;

import com.zombienw.onyxlib.api.OnyxElement;
import com.zombienw.onyxlib.impl.item.OnyxItemImpl;
import com.zombienw.onyxlib.impl.pack.PackGenerator;
import com.zombienw.onyxlib.impl.registry.NamespaceRegistry;
import com.zombienw.onyxlib.impl.registry.OnyxNamespaceImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            sender.sendMessage(Component.text("Usage: /onyx <give|generatePack>", NamedTextColor.RED));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("generatepack")) {
            if (!sender.hasPermission("onyxlib.admin")) {
                sender.sendMessage(Component.text("No permission.", NamedTextColor.RED));
                return true;
            }

            sender.sendMessage(Component.text("Starting pack generation...", NamedTextColor.YELLOW));

            // Run asynchronously to prevent freezing the server during heavy IO operations
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    PackGenerator generator = new PackGenerator(plugin);
                    String result = generator.generate();
                    sender.sendMessage(Component.text(result, NamedTextColor.GREEN));
                } catch (Exception e) {
                    sender.sendMessage(Component.text("Pack generation failed! Check console.", NamedTextColor.RED));
                    e.printStackTrace();
                }
            });
            return true;
        }

        if (subCommand.equals("give")) {
            if (!sender.hasPermission("onyxlib.admin")) {
                sender.sendMessage(Component.text("No permission.", NamedTextColor.RED));
                return true;
            }

            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("Only players can receive items.", NamedTextColor.RED));
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage(Component.text("Usage: /onyx give <namespace:item_id> [amount]", NamedTextColor.RED));
                return true;
            }

            NamespacedKey key = NamespacedKey.fromString(args[1]);
            if (key == null) {
                sender.sendMessage(Component.text("Invalid item key format.", NamedTextColor.RED));
                return true;
            }

            OnyxElement element = NamespaceRegistry.getElement(key);
            if (element == null) {
                sender.sendMessage(Component.text("Unknown OnyxLib element: " + key, NamedTextColor.RED));
                return true;
            }

            int amount = 1;
            if (args.length >= 3) {
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException ignored) {}
            }

            ItemStack stack = element.create(amount);
            player.getInventory().addItem(stack);
            sender.sendMessage(Component.text("Given " + amount + " " + key, NamedTextColor.GREEN));
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
                for (OnyxElement element : ns.getElements()) {
                    completions.add(element.getKey().toString());
                }
            }
        }

        String currentArg = args[args.length - 1].toLowerCase();
        return completions.stream()
                .filter(c -> {
                    String lowerC = c.toLowerCase();
                    return lowerC.startsWith(currentArg) || lowerC.contains(":" + currentArg);
                })
                .toList();
    }
}
