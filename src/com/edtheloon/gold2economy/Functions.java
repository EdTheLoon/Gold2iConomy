package com.edtheloon.gold2economy;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Methods;

public class Functions {

	// Show conversion rates
	public static void displayRates(CommandSender sender, VaultSupport vault, configHandler config){ // Added argument: VaultSupport, configHandler (turt2live)
		if(vault.hasRegister()){ // Added by turt2live (rate table for Register is EdTheLoon)
			Method method = Methods.getMethod();

			// Display the rates here
			if(!config.convertDiamond && !config.convertGold && !config.convertIron){
				sender.sendMessage(ChatColor.RED + "This server doesn't allow anything to be converted");
			}else{
				sender.sendMessage("--- Conversion Rates ---");

				if(config.convertIron){
					sender.sendMessage("1 " + ChatColor.GRAY + "Iron" + ChatColor.WHITE + " = "
							+ ChatColor.GREEN + method.format(config.ironRate));
				}

				if(config.convertGold){
					sender.sendMessage("1 " + ChatColor.YELLOW + "Gold" + ChatColor.WHITE + " = "
							+ ChatColor.GREEN + method.format(config.goldRate));
				}

				if(config.convertDiamond){
					sender.sendMessage("1 " + ChatColor.AQUA + "Diamond" + ChatColor.WHITE + " = "
							+ ChatColor.GREEN + method.format(config.diamondRate));
				}
			}
		}else if(!config.convertDiamond && !config.convertGold && !config.convertIron){
			sender.sendMessage(ChatColor.RED + "This server doesn't allow anything to be converted");
		}else{
			sender.sendMessage("--- Conversion Rates ---");

			if(config.convertIron){
				sender.sendMessage("1 " + ChatColor.GRAY + "Iron" + ChatColor.WHITE + " = "
						+ ChatColor.GREEN + vault.format(config.ironRate));
			}

			if(config.convertGold){
				sender.sendMessage("1 " + ChatColor.YELLOW + "Gold" + ChatColor.WHITE + " = "
						+ ChatColor.GREEN + vault.format(config.goldRate));
			}

			if(config.convertDiamond){
				sender.sendMessage("1 " + ChatColor.AQUA + "Diamond" + ChatColor.WHITE + " = "
						+ ChatColor.GREEN + vault.format(config.diamondRate));
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
