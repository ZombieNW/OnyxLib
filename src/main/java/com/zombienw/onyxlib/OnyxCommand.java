package com.zombienw.onyxlib;

import com.zombienw.onyxlib.api.OnyxElement;
import com.zombienw.onyxlib.impl.pack.PackGenerator;
import com.zombienw.onyxlib.impl.registry.NamespaceRegistry;
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

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Stream;

/**
 * Executes/completes the give and generatePack command.
 */
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

        return switch (subCommand) {
            case "generatepack" -> handleGeneratePack(sender);
            case "give" -> handleGive(sender, args);
            default -> false;
        };
    }

    private boolean handleGeneratePack(CommandSender sender) {
        if (!sender.hasPermission("onyxlib.admin")) {
            sender.sendMessage(Component.text("No permission.", NamedTextColor.RED));
            return true;
        }

        sender.sendMessage(Component.text("Starting pack generation...", NamedTextColor.YELLOW));

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PackGenerator generator = new PackGenerator(plugin);
                String result = generator.generate();
                sender.sendMessage(Component.text(result, NamedTextColor.GREEN));
            } catch (Exception e) {
                sender.sendMessage(Component.text("Pack generation failed! Check console.", NamedTextColor.RED));
                plugin.getLogger().log(Level.SEVERE, "Failed to generate pack", e);
            }
        });
        return true;
    }

    private boolean handleGive(CommandSender sender, String[] args) {
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
                if (amount < 1) amount = 1;
            } catch (NumberFormatException ignored) {}
        }

        ItemStack stack = element.create(amount);
        player.getInventory().addItem(stack);
        sender.sendMessage(Component.text("Given " + amount + " " + key, NamedTextColor.GREEN));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String alias, String @NonNull [] args) {
        if (!sender.hasPermission("onyxlib.admin")) {
            return Collections.emptyList();
        }

        String currentArg = args[args.length - 1].toLowerCase();

        if (args.length == 1) {
            return Stream.of("give", "generatePack")
                    .filter(sub -> sub.toLowerCase().startsWith(currentArg))
                    .toList();
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            return NamespaceRegistry.getAllNamespaces().stream()
                    .flatMap(ns -> ns.getElements().stream())
                    .map(element -> element.getKey().toString())
                    .filter(keyStr -> keyStr.toLowerCase().startsWith(currentArg) ||
                            keyStr.toLowerCase().contains(":" + currentArg))
                    .toList();
        }

        return Collections.emptyList();
    }
}
