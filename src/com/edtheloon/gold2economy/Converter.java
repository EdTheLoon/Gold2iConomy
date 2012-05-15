package com.edtheloon.gold2economy;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.feildmaster.lib.configuration.EnhancedConfiguration;

public class Converter {

	// Convert items into money
	public static void convertItem(CommandSender sender, int itemID, int amount, VaultSupport vault, EnhancedConfiguration config){ // Added argument: configHandler (turt2live)
		// Setup variables (turt2live)
		double ironRate = config.getDouble("rates.iron");
		double goldRate = config.getDouble("rates.gold");
		double diamondRate = config.getDouble("rates.diamond");

		// Declare local variables
		Double conversion = 0.0;
		Player player = (Player) sender;
		PlayerInventory pi = player.getInventory();

		// Calculate conversion rate
		// Change this from a switch (itemID) to if conditions because it was causing bugs
		if(itemID == 265){
			conversion = ironRate * amount; // Fixed for argument change (turt2live)
		}else if(itemID == 266){
			conversion = goldRate * amount; // Fixed for argument change (turt2live)
		}else if(itemID == 264){
			conversion = diamondRate * amount; // Fixed for argument change (turt2live)
		}else if(isAllowed(itemID)){ // For customized/other items (like gold nuggets) - Turt2Live
			conversion = getRate(itemID) * amount;
		}

		// Check to see if they have enough of <item>
		if(pi.contains(itemID, amount)){
			// Remove item
			HashMap<Integer, ItemStack> difference = pi.removeItem(new ItemStack(itemID, amount));
			difference.clear();
			// Start turt2live edit
			Double balance = 0.0;
			String formattedBalance = "";
			String formattedConversion = "";
			if(vault != null){ // Cause I can't think of any other way... (turt2live)
				if(vault.isActive()){ // Do we have Vault? (turt2live)
					VaultSupport v = vault;
					v.deposit(player.getName(), conversion);
					balance = v.balance(player.getName());
					formattedBalance = v.format(balance);
					formattedConversion = v.format(conversion);
				}
			}
			sender.sendMessage(ChatColor.GREEN + "You converted " + amount + " item(s) into " + formattedConversion); // Renamed variable used
			sender.sendMessage(ChatColor.GREEN + "You now have " + formattedBalance); // Renamed variable used
			// End turt2live edit
		}else{
			sender.sendMessage(ChatColor.DARK_RED + "You do not have " + Integer.toString(amount) + ChatColor.DARK_RED + " of that item!");
		}
	}

	// Added by turt2live, gets the rate of a custom item
	public static Double getRate(int itemID){
		gold2economy plugin = (gold2economy) Bukkit.getServer().getPluginManager().getPlugin("Gold2Economy-Vault");
		EnhancedConfiguration config = plugin.getConversionChart();
		return config.getDouble(String.valueOf(itemID));
	}

	// Added by turt2live, gets the allowance of an item ID (for custom items)
	public static boolean isAllowed(int itemID){
		gold2economy plugin = (gold2economy) Bukkit.getServer().getPluginManager().getPlugin("Gold2Economy-Vault");
		EnhancedConfiguration config = plugin.getConversionChart();
		return config.get(String.valueOf(itemID)) != null;
	}

	// Added by turt2live, checks if the item can be bought
	public static boolean canBuy(int itemID){
		gold2economy plugin = (gold2economy) Bukkit.getServer().getPluginManager().getPlugin("Gold2Economy-Vault");
		EnhancedConfiguration config = plugin.getSellChart();
		return config.get(String.valueOf(itemID)) != null;
	}

	// Added by turt2live, gets the rate of a custom item (buying)
	public static Double getBuyRate(int itemID){
		gold2economy plugin = (gold2economy) Bukkit.getServer().getPluginManager().getPlugin("Gold2Economy-Vault");
		EnhancedConfiguration config = plugin.getSellChart();
		return config.getDouble(String.valueOf(itemID));
	}

}
