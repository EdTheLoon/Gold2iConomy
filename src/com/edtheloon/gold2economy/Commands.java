package com.edtheloon.gold2economy;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.iConomy.iConomy;

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
				if (args[1].equalsIgnoreCase("all")) {

					int itemID = 0;
					int amount = 0;
					
					// Determine what itemID to use
					if (args[0].equalsIgnoreCase("iron")) itemID = 265;
					if (args[0].equalsIgnoreCase("gold")) itemID = 266;
					if (args[0].equalsIgnoreCase("diamond")) itemID = 264;
					
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
				
			}
		} else { // This part will run if the plugin is not 'enabled'
			sender.sendMessage(ChatColor.RED + "Gold2eConomy is disabled because no currency sytem is enabled");
			return true;
		}
		return false;
	}

}
