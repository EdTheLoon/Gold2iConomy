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
//import com.iConomy.iConomy;
//import com.iConomy.system.Holdings;

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
			
			// TODO: ADD REGISTER CODE HERE
			Method method = Methods.getMethod();
			MethodAccount account = method.getAccount(player.getName());
			account.add(conversion);
			Double balance = account.balance();
			sender.sendMessage(ChatColor.GREEN + "You converted " + amount + " item(s) into " + method.format(conversion));
			sender.sendMessage(ChatColor.GREEN + "You now have " + balance.toString());
			
			// If using iConomy
			/*if (gold2economy.config.iConomy && gold2economy.iConomyPlugin != null) {
				
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
			}*/			
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have " + Integer.toString(amount) + ChatColor.DARK_RED + " of that item!");
		}
	}

}
