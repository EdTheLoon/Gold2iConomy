package com.edtheloon.gold2economy;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Methods;

public class Functions {

	// Reload configuration
	public static boolean giReload(CommandSender sender) {
		gold2economy.config.loadConfig();
		if (sender != null) sender.sendMessage(ChatColor.GREEN + "[Gold2Economy]" + ChatColor.WHITE + " Configuration reloaded");
		return true;
	}

	// Show conversion rates
	public static void displayRates(CommandSender sender, VaultSupport vault) { // Added argument: VaultSupport (turt2live)

		if (vault.hasRegister()) { // Added by turt2live (rate table for Register is EdTheLoon)
			Method method = Methods.getMethod();

			// Display the rates here
			if (!gold2economy.config.convertDiamond && !gold2economy.config.convertGold && !gold2economy.config.convertIron) sender.sendMessage(ChatColor.RED + "This server doesn't allow anything to be converted");
			else {
				sender.sendMessage("--- Conversion Rates ---");

				if (gold2economy.config.convertIron) sender.sendMessage("1 " + ChatColor.GRAY + "Iron" + ChatColor.WHITE + " = "
						+ ChatColor.GREEN + method.format(gold2economy.config.ironRate));

				if (gold2economy.config.convertGold) sender.sendMessage("1 " + ChatColor.YELLOW + "Gold" + ChatColor.WHITE + " = "
						+ ChatColor.GREEN + method.format(gold2economy.config.goldRate));

				if (gold2economy.config.convertDiamond) sender.sendMessage("1 " + ChatColor.AQUA + "Diamond" + ChatColor.WHITE + " = "
						+ ChatColor.GREEN + method.format(gold2economy.config.diamondRate));
			}
		} else if (!gold2economy.config.convertDiamond && !gold2economy.config.convertGold && !gold2economy.config.convertIron) sender.sendMessage(ChatColor.RED + "This server doesn't allow anything to be converted");
		else {
			sender.sendMessage("--- Conversion Rates ---");

			if (gold2economy.config.convertIron) sender.sendMessage("1 " + ChatColor.GRAY + "Iron" + ChatColor.WHITE + " = "
					+ ChatColor.GREEN + vault.format(gold2economy.config.ironRate));

			if (gold2economy.config.convertGold) sender.sendMessage("1 " + ChatColor.YELLOW + "Gold" + ChatColor.WHITE + " = "
					+ ChatColor.GREEN + vault.format(gold2economy.config.goldRate));

			if (gold2economy.config.convertDiamond) sender.sendMessage("1 " + ChatColor.AQUA + "Diamond" + ChatColor.WHITE + " = "
					+ ChatColor.GREEN + vault.format(gold2economy.config.diamondRate));
		}
	}
}
