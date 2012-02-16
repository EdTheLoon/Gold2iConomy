package com.edtheloon.gold2economy;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Method.MethodAccount;
import com.nijikokun.register.payment.Methods;

public class Converter {

	// Convert Gold into money
	public static void convertItem(CommandSender sender, int itemID, int amount, VaultSupport vault, EnhancedConfiguration config) // Added argument: configHandler (turt2live)
	{
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
		}else if(itemID == 264)
		{
			conversion = diamondRate * amount; // Fixed for argument change (turt2live)
		}

		// If user has enough ingots then convert gold, otherwise inform user that they do not have enough gold ingots
		if(pi.contains(itemID, amount)){

			// Remove gold ingots
			HashMap<Integer, ItemStack> difference = pi.removeItem(new ItemStack(itemID, amount));
			difference.clear();
			// Start turt2live edit
			Double balance = 0.0;
			String formattedBalance = "";
			String formattedConversion = "";
			if(vault != null){ // Cause I can't think of any other way... (turt2live)
				if(vault.isActive()){ // Do we have Vault? or Register? (turt2live)
					VaultSupport v = vault;
					v.deposit(player.getName(), conversion);
					balance = v.balance(player.getName());
					formattedBalance = v.format(balance);
					formattedConversion = v.format(conversion);
				}else{ // Duplicated from null check IF (turt2live)
					Method method = Methods.getMethod();
					MethodAccount account = method.getAccount(player.getName());
					account.add(conversion);
					balance = account.balance();
					// Format values (turt2live)
					formattedBalance = method.format(balance);
					formattedConversion = method.format(conversion);
				}
			}else{ // Code in here written by EdTheLoon, just thrown into an else
				Method method = Methods.getMethod();
				MethodAccount account = method.getAccount(player.getName());
				account.add(conversion);
				balance = account.balance();
				// Format values (turt2live)
				formattedBalance = method.format(balance);
				formattedConversion = method.format(conversion);
			}
			sender.sendMessage(ChatColor.GREEN + "You converted " + amount + " item(s) into " + formattedConversion); // Renamed variable used
			sender.sendMessage(ChatColor.GREEN + "You now have " + formattedBalance); // Renamed variable used
			// End turt2live edit
		}else{
			sender.sendMessage(ChatColor.DARK_RED + "You do not have " + Integer.toString(amount) + ChatColor.DARK_RED + " of that item!");
		}
	}

}
