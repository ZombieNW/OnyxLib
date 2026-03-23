package com.zombienw.onyxLib.core;

import com.zombienw.onyxLib.items.ItemService;
import com.zombienw.onyxLib.items.RegisteredItem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class OnyxCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = List.of("give", "generatepack");
    private static final List<String> AMOUNT_SUGGESTIONS = List.of("1", "8", "16", "32", "64");

    private final ItemService itemService;
    private final PackGenerator packGenerator;

    public OnyxCommand(ItemService itemService, PackGenerator packGenerator) {
        this.itemService = itemService;
        this.packGenerator = packGenerator;
    }

    /// Execution
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /onyx <give|generatepack>");
            return true;
        }

        return switch (args[0].toLowerCase()) {
            case "give" -> handleGive(sender, args);
            case "generatepack" -> handleGeneratePack(sender);
            default -> {
                sender.sendMessage("Unknown subcommand. Usage: /onyx <give|generatepack>");
                yield true;
            }
        };
    }

    private boolean handleGive(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("Usage: /onyx give <player> <namespace:item> [amount]");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage("Player not found: " + args[1]);
            return true;
        }

        String fullId = args[2];
        int amount = 1;

        if (args.length >= 4) {
            try {
                amount = Integer.parseInt(args[3]);
                if (amount < 1 || amount > 64) {
                    sender.sendMessage("Amount must be between 1 and 64.");
                    return true;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid amount: " + args[3]);
                return true;
            }
        }

        final int finalAmount = amount;
        itemService.get(fullId).ifPresentOrElse(
                registered -> {
                    ItemStack stack = itemService.create(fullId, finalAmount);
                    target.getInventory().addItem(stack);
                    sender.sendMessage("Gave " + finalAmount + "x " + fullId + " to " + target.getName() + ".");
                },
                () -> sender.sendMessage("Unknown item: " + fullId)
        );

        return true;
    }

    private boolean handleGeneratePack(CommandSender sender) {
        sender.sendMessage("Generating resource pack...");
        try {
            String result = packGenerator.generate();
            sender.sendMessage(result);
        } catch (IOException e) {
            sender.sendMessage("Pack generation failed: " + e.getMessage());
        }
        return true;
    }

    /// Tab Completion
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return partialMatch(args[0], SUBCOMMANDS);
        }

        if (args[0].equalsIgnoreCase("give")) {
            return switch (args.length) {
                case 2 -> partialMatch(args[1], Bukkit.getOnlinePlayers()
                        .stream().map(Player::getName).toList());
                case 3 -> partialMatch(args[2], itemService.all()
                        .stream().map(RegisteredItem::getFullId).toList());
                case 4 -> partialMatch(args[3], AMOUNT_SUGGESTIONS);
                default -> Collections.emptyList();
            };
        }

        return Collections.emptyList();
    }

    private List<String> partialMatch(String input, Collection<String> options) {
        String lower = input.toLowerCase();
        return options.stream()
                .filter(o -> o.toLowerCase().startsWith(lower))
                .toList();
    }
}
