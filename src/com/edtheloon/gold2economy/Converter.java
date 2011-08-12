package com.edtheloon.gold2economy;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import com.iConomy.iConomy;
import com.iConomy.system.Holdings;

public class Converter {
	
	// Convert Gold into money
	public static void convertItem(CommandSender sender, Integer itemID, Integer amount)
	{
		
		// Declare local variables
		Double conversion = 0.0;
		Player player = (Player)sender;
		PlayerInventory pi = player.getInventory();
		
		// Calculate conversion rate
		switch (itemID) {
		case 265: conversion = gold2economy.config.ironRate * amount; // IRON_INGOT
		case 266: conversion = gold2economy.config.goldRate * amount; // GOLD_INGOT
		case 264: conversion = gold2economy.config.diamondRate * amount; // DIAMOND
		}

		// If user has enough ingots then convert gold, otherwise inform user that they do not have enough gold ingots
		if (pi.contains(itemID, amount))
		{
			
			// Remove gold ingots
			HashMap<Integer,ItemStack> difference = pi.removeItem(new ItemStack(itemID, amount));			
			difference.clear();
			
			// If using iConomy
			if (gold2economy.config.iConomy && gold2economy.iConomyPlugin != null) {
				
				Holdings balance = iConomy.getAccount(player.getName()).getHoldings();
				balance.add(conversion);
				sender.sendMessage(ChatColor.GREEN + "You converted " + amount + " item(s) into " + iConomy.format(conversion));
				sender.sendMessage(ChatColor.GREEN + "You now have " + iConomy.format(player.getName()));
				
			// If using BOSEconomy
			} else if (gold2economy.config.BOSEconomy && gold2economy.BOSEconomyPlugin != null) { 
				
				gold2economy.BOSEconomyPlugin.addPlayerMoney(player.getName(), conversion, true);
				sender.sendMessage(ChatColor.GREEN + "You converted " + amount + " item(s) into " + gold2economy.BOSEconomyPlugin.getMoneyNameCapsProper(conversion));
				sender.sendMessage(ChatColor.GREEN + "You now have " 
						+ gold2economy.BOSEconomyPlugin.getMoneyNameCapsProper(gold2economy.BOSEconomyPlugin.getPlayerMoneyDouble(player.getName())));
			}			
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have " + Integer.toString(amount) + ChatColor.DARK_RED + " of that item!");
		}
	}

}
