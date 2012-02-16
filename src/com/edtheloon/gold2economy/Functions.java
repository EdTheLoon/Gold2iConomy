package com.edtheloon.gold2economy;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Methods;

public class Functions {

	// Show conversion rates
	public static void displayRates(CommandSender sender, VaultSupport vault, EnhancedConfiguration config){ // Added argument: VaultSupport, configHandler (turt2live)
		// Setup variables (turt2live)
		boolean convertIron = config.getBoolean("convert.iron");
		boolean convertGold = config.getBoolean("convert.gold");
		boolean convertDiamond = config.getBoolean("convert.diamond");
		double ironRate = config.getDouble("rates.iron");
		double goldRate = config.getDouble("rates.gold");
		double diamondRate = config.getDouble("rates.diamond");

		if(vault.hasRegister()){ // Added by turt2live (rate table for Register is EdTheLoon)
			Method method = Methods.getMethod();

			// Display the rates here
			if(!convertDiamond && !convertGold && !convertIron){
				sender.sendMessage(ChatColor.RED + "This server doesn't allow anything to be converted");
			}else{
				sender.sendMessage("--- Conversion Rates ---");

				if(convertIron){
					sender.sendMessage("1 " + ChatColor.GRAY + "Iron" + ChatColor.WHITE + " = "
							+ ChatColor.GREEN + method.format(ironRate));
				}

				if(convertGold){
					sender.sendMessage("1 " + ChatColor.YELLOW + "Gold" + ChatColor.WHITE + " = "
							+ ChatColor.GREEN + method.format(goldRate));
				}

				if(convertDiamond){
					sender.sendMessage("1 " + ChatColor.AQUA + "Diamond" + ChatColor.WHITE + " = "
							+ ChatColor.GREEN + method.format(diamondRate));
				}
			}
		}else if(!convertDiamond && !convertGold && !convertIron){
			sender.sendMessage(ChatColor.RED + "This server doesn't allow anything to be converted");
		}else{
			sender.sendMessage("--- Conversion Rates ---");

			if(convertIron){
				sender.sendMessage("1 " + ChatColor.GRAY + "Iron" + ChatColor.WHITE + " = "
						+ ChatColor.GREEN + vault.format(ironRate));
			}

			if(convertGold){
				sender.sendMessage("1 " + ChatColor.YELLOW + "Gold" + ChatColor.WHITE + " = "
						+ ChatColor.GREEN + vault.format(goldRate));
			}

			if(convertDiamond){
				sender.sendMessage("1 " + ChatColor.AQUA + "Diamond" + ChatColor.WHITE + " = "
						+ ChatColor.GREEN + vault.format(diamondRate));
			}
		}
	}

	// Reload configuration
	@Deprecated
	// See configHandler.reload(CommandSender) (turt2live)
	public static boolean giReload(CommandSender sender, configHandler config){ // Added argument: configHandler (turt2live)
		config.loadConfig();
		if(sender != null){
			sender.sendMessage(ChatColor.GREEN + "[Gold2Economy]" + ChatColor.WHITE + " Configuration reloaded");
		}
		return true;
	}
}
