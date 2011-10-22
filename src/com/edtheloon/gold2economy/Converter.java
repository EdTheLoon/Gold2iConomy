package com.edtheloon.gold2economy;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Method.MethodAccount;
import com.nijikokun.register.payment.Methods;

public class Converter {
	
	// Convert Gold into money
	public static void convertItem(CommandSender sender, int itemID, int amount)
	{
		
		// Declare local variables
		Double conversion = 0.0;
		Player player = (Player)sender;
		PlayerInventory pi = player.getInventory();
		
		// Calculate conversion rate
		// Change this from a switch (itemID) to if conditions because it was causing bugs
		if (itemID == 265) { // IRON
			conversion = gold2economy.config.ironRate * amount;
		} else if (itemID == 266) { // GOLD
			conversion = gold2economy.config.goldRate * amount;
		} else if (itemID == 264) { // DIAMOND
			conversion = gold2economy.config.diamondRate * amount;
		}

		// If user has enough ingots then convert gold, otherwise inform user that they do not have enough gold ingots
		if (pi.contains(itemID, amount))
		{
			
			// Remove gold ingots
			HashMap<Integer,ItemStack> difference = pi.removeItem(new ItemStack(itemID, amount));			
			difference.clear();
			Method method = Methods.getMethod();
			MethodAccount account = method.getAccount(player.getName());
			account.add(conversion);
			Double balance = account.balance();
			sender.sendMessage(ChatColor.GREEN + "You converted " + amount + " item(s) into " + method.format(conversion));
			sender.sendMessage(ChatColor.GREEN + "You now have " + method.format(balance));	
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have " + Integer.toString(amount) + ChatColor.DARK_RED + " of that item!");
		}
	}

}
