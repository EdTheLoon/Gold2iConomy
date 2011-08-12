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
					}
				}

				// Command = /gi <item> all - Convert all <item> - <item> is either iron, gold or diamond
				// TODO: ADD command code

				// Command = /gi <amount> - Convert <amount> of gold
				// TODO: ADD command code
			}
		} else { // This part will run if the plugin is not 'enabled'
			sender.sendMessage(ChatColor.RED + "Gold2eConomy is disabled because no currency sytem is enabled");
			return true;
		}
		return false;
	}

}
