package com.edtheloon.gold2economy;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.feildmaster.lib.configuration.EnhancedConfiguration;

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
		EnhancedConfiguration items = vault.getPlugin().getConversionChart();
		Set<String> keys = items.getConfigurationSection("").getKeys(false);

		if(!convertDiamond && !convertGold && !convertIron && keys.size() == 0){
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
			// Display other items (turt2live)
			int i = 0;
			for(String itemID : keys){
				Material item = Material.getMaterial(Integer.valueOf(itemID.replace("'", "")));
				String itemName = item.name().substring(0, 1) + item.name().toLowerCase().substring(1);
				double rate = Converter.getRate(item.getId());
				sender.sendMessage("1 " + ChatColor.GREEN + itemName + ChatColor.WHITE + " = "
						+ ChatColor.GREEN + vault.format(rate));
				i++;
				if(i == 10){
					break;
				}
			}
			if(keys.size() > 10){
				sender.sendMessage(ChatColor.RED + "There are more items that can be converted! Contact your admin for help.");
			}
		}
	}
}
