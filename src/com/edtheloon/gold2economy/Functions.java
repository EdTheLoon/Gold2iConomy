package com.edtheloon.gold2economy;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.iConomy.iConomy;

public class Functions {

	// Reload configuration
	public static boolean giReload(CommandSender sender) {
		gold2economy.config.loadConfig();
		sender.sendMessage(ChatColor.GREEN + "[Gold2Economy]" + ChatColor.WHITE + " Configuration reloaded");
		return true;
	}
	
	// Show conversion rates
	public static void displayRates(CommandSender sender) {
		
		// If using iConomy
		if (gold2economy.config.iConomy && gold2economy.iConomyPlugin != null) {
			
			sender.sendMessage("--- Conversion Rates ---");
			
			sender.sendMessage("1 " + ChatColor.GRAY + "Iron" + ChatColor.WHITE + " = "
					+ ChatColor.GREEN + iConomy.format(gold2economy.config.ironRate));
			
			sender.sendMessage("1 " + ChatColor.YELLOW + "Gold" + ChatColor.WHITE + " = "
					+ ChatColor.GREEN + iConomy.format(gold2economy.config.goldRate));
			
			sender.sendMessage("1 " + ChatColor.AQUA + "Diamond" + ChatColor.WHITE + " = "
					+ ChatColor.GREEN + iConomy.format(gold2economy.config.diamondRate));
		
		} else if (gold2economy.config.BOSEconomy && gold2economy.BOSEconomyPlugin != null) { // If using BOSEconomy
			
			sender.sendMessage("--- Conversion Rates ---");
			
			sender.sendMessage("1 " + ChatColor.GRAY + "Iron" + ChatColor.WHITE + " = "
					+ ChatColor.GREEN + gold2economy.BOSEconomyPlugin.getMoneyFormatted(gold2economy.config.ironRate));
			
			sender.sendMessage("1 " + ChatColor.YELLOW + "Gold" + ChatColor.WHITE + " = "
					+ ChatColor.GREEN + gold2economy.BOSEconomyPlugin.getMoneyFormatted(gold2economy.config.goldRate));
			
			sender.sendMessage("1 " + ChatColor.AQUA + "Diamond" + ChatColor.WHITE + " = "
					+ ChatColor.GREEN + gold2economy.BOSEconomyPlugin.getMoneyFormatted(gold2economy.config.diamondRate));
			
		} else if (!gold2economy.enabled) { // If no economy is found

			sender.sendMessage("--- Conversion Rates ---");
			
			sender.sendMessage("1 " + ChatColor.GRAY + "Iron" + ChatColor.WHITE + " = "
					+ ChatColor.GREEN + gold2economy.config.ironRate.toString());
			
			sender.sendMessage("1 " + ChatColor.YELLOW + "Gold" + ChatColor.WHITE + " = "
					+ ChatColor.GREEN + gold2economy.config.goldRate.toString());
			
			sender.sendMessage("1 " + ChatColor.AQUA + "Diamond" + ChatColor.WHITE + " = "
					+ ChatColor.GREEN + gold2economy.config.diamondRate.toString());
			
			sender.sendMessage(ChatColor.RED + "Gold2Economy is currently disabled because no economy system was found");
			
		}
	}
}
