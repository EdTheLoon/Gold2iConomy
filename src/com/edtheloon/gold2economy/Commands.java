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
					if (gold2economy.config.iConomy && gold2economy.iConomyPlugin != null) {
						sender.sendMessage(ChatColor.GREEN + "Conversion rate: 1 gold ingot = " + iConomy.format(gold2economy.config.cRate));
						return true;
					}
					if (gold2economy.config.BOSEconomy && gold2economy.BOSEconomyPlugin != null) {
						sender.sendMessage(ChatColor.GREEN + "Conversion rate: 1 gold ingot = " + gold2economy.config.cRate.toString());
						return true;
					}
				}

				// Command = /gi reload - Reload configuration
				if (args[0].equalsIgnoreCase("reload")) {
					if (Permissions.check(sender, gold2economy.PERMISSION_ADMIN) || sender instanceof ConsoleCommandSender) {
						Functions.giReload(sender);
						return true;
					} else {
						sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
					}
				}

				// Command = /gi all - Convert all gold
				if (args[0].equalsIgnoreCase("all")) {
					if (Permissions.check(sender, gold2economy.PERMISSION_USE) || !gold2economy.config.usePermissions || !gold2economy.permissionsEnabled) {
						// Declare and initialise local variables
						Player player = (Player)sender;
						PlayerInventory pi = player.getInventory();				
						ItemStack items[] = pi.getContents();

						// Only go ahead if the players inventory contains at least 1 gold ingot
						// Displays a message saying that the player doesn't have any gold if none is found
						if (pi.contains(266)) {
							Integer ingots = 0;

							// Loop through players inventory
							for (Integer i=0; i < items.length; i++) {
								if (items[i] != null)
								{
									// If at least 1 gold ingot is in the inventory slot then add the amount of gold in this slot to total ingots found
									if (items[i].getTypeId() == 266) {
										ingots = ingots + items[i].getAmount();
									}
								}
							}
							// Call convertGold to convert the gold ingots
							Converter.convertGold(sender, ingots);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have any gold ingots to convert!");
							return true;
						}
					} else {
						sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
					}
				}

				// Command = /gi <amount> - Convert <amount> of gold
				if (sender instanceof Player) {

					Integer ingots = 0;
					try {
						ingots = Integer.parseInt(args[0]);
						if (Permissions.check(sender, gold2economy.PERMISSION_USE) || !gold2economy.config.usePermissions || !gold2economy.permissionsEnabled) {
							Converter.convertGold(sender, ingots);
							return true;
						}
					} catch (NumberFormatException e) { // This should only be done if anything other than reload, all or a number was entered.
						// Below line for debugging only
						//log.info("[Gold2iConomy] ERROR: " + e.toString());
						return false;
					}
				}
			} else { // This part will run if the plugin is not 'enabled'
				sender.sendMessage(ChatColor.RED + "Gold2eConomy is disabled because no currency sytem is enabled");
				return true;
			}
			return false;
		}
		return false;
	}

}
