package com.edtheloon.gold2economy;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Commands implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		// First check to see whether the plugin is 'enabled'
		if (gold2economy.enabled) {

			// COMMAND - /gi
			if (cmd.getName().equalsIgnoreCase("gi")) {

				// Command = /gi - Tells player the conversion rate
				if (args.length == 0) {
					Functions.displayRates(sender);
				}

				// Command = /gi reload - Reload configuration
				if (args[0].equalsIgnoreCase("reload")) {
					if (Permissions.check(sender, gold2economy.PERMISSION_ADMIN) || sender instanceof ConsoleCommandSender) {
						Functions.giReload(sender);
						return true;
					} else {
						sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
						return true;
					}
				}

				// Command = /gi <item> all - Convert all <item> - <item> is either iron, gold or diamond
				// TODO: ADD command code
				if (args[1].equalsIgnoreCase("all") && sender instanceof Player) {

					int itemID = 0;
					int amount = 0;
					String permNeeded = "";
					
					// Determine what itemID to use
					if (args[0].equalsIgnoreCase("iron")) {
						itemID = 265;
						permNeeded = gold2economy.PERMISSION_IRON;
					} else if (args[0].equalsIgnoreCase("gold")) {
						itemID = 266;
						permNeeded = gold2economy.PERMISSION_GOLD;
					} else if (args[0].equalsIgnoreCase("diamond")) {
						itemID = 264;
						permNeeded = gold2economy.PERMISSION_DIAMOND;
					} else { // User did not type iron, gold or diamond
						sender.sendMessage(ChatColor.RED + "You can only convert iron, gold or diamond!");
						return true;
					}
					
					// Don't continue if player doesn't have required permission (if enabled)
					if (gold2economy.config.Permissions && !Permissions.check(sender, permNeeded)) {
						sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
						return false;
					}
					
					// Prepare to loop through player inventory
					Player player = ((Player) sender);
					PlayerInventory pi = player.getInventory();
					ItemStack items[] = pi.getContents();
					
					// First check to see that user has at least 1 of <item>
					if (pi.contains(itemID)) {
						
						// Loop through player's inventory to look for <item>
						for (ItemStack item : items) {
							// if the inventory slot is not null AND it is the item we are looking for then change amount
							if (item != null && item.getTypeId() == itemID) amount += item.getAmount();
						}
						Converter.convertItem(sender, itemID, amount);
						return true; // finally say we've handled command correctly
						
					} else {
						sender.sendMessage(ChatColor.RED + "You don't have any of that item!");
						return true;
					}
				}

				// Command = /gi <item> <amount> - Convert <amount> of <item> - <item> is either iron, gold or diamond
				// TODO: ADD command code
				
				// Regular expression to check if args[1] is an integer
				if (args[1].matches("\\d+") && sender instanceof Player) {
					int amount = 0;
					int itemID = 0;
					String permNeeded = "";
					
					// Use a try-catch to safely retrieve amount to convert
					try {
						amount = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						// DEBUG LINE
						gold2economy.log.severe("[Gold2Economy] Error: " + e.toString());
						return false;
					}
					
					// Determine what itemID to use
					if (args[0].equalsIgnoreCase("iron")) {
						itemID = 265;
						permNeeded = gold2economy.PERMISSION_IRON;
					} else if (args[0].equalsIgnoreCase("gold")) {
						itemID = 266;
						permNeeded = gold2economy.PERMISSION_GOLD;
					} else if (args[0].equalsIgnoreCase("diamond")) {
						itemID = 264;
						permNeeded = gold2economy.PERMISSION_DIAMOND;
					} else { // User did not type iron, gold or diamond
						sender.sendMessage(ChatColor.RED + "You can only convert iron, gold or diamond!");
						return true;
					}
					
					// Check if player has permission first
					if (gold2economy.config.Permissions && !Permissions.check(sender, permNeeded)) {
						sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
						return false;
					}
					
					// Finally convert item into money
					Converter.convertItem(sender, itemID, amount)	;			
					
				}
				
			}
		} else { // This part will run if the plugin is not 'enabled'
			sender.sendMessage(ChatColor.RED + "Gold2eConomy is disabled because no currency sytem is enabled");
			return true;
		}
		return false;
	}

}
