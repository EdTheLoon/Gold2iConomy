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
	
	// Convert Gold into iConomy money
	@SuppressWarnings("deprecation")
	public static boolean convertGold(CommandSender sender, Integer ingots)
	{

		Double conversion = gold2economy.config.cRate * ingots;
		Player player = (Player)sender;
		PlayerInventory pi = player.getInventory();

		// If user has enough ingots then convert gold, otherwise inform user that they do not have enough gold ingots
		if (pi.contains(266, ingots))
		{
			// If using iConomy
			if (gold2economy.config.iConomy && gold2economy.iConomyPlugin != null) {
				Holdings balance = iConomy.getAccount(player.getName()).getHoldings();
				balance.add(conversion);
				sender.sendMessage(ChatColor.GREEN + "You converted " + ingots + " ingots into " + iConomy.format(conversion));
				sender.sendMessage(ChatColor.GREEN + "You now have " + iConomy.format(player.getName()));
			} else if (gold2economy.config.BOSEconomy && gold2economy.BOSEconomyPlugin != null) { // If using BOSEconomy
				int money = Math.round(conversion.floatValue());
				gold2economy.BOSEconomyPlugin.addPlayerMoney(player.getName(), money, true);
				sender.sendMessage(ChatColor.GREEN + "You converted " + ingots + " ingots into " + money + " " + gold2economy.BOSEconomyPlugin.getMoneyNamePluralCaps());
				sender.sendMessage(ChatColor.GREEN + "You now have " + gold2economy.BOSEconomyPlugin.getPlayerMoney(player.getName()) + " " + gold2economy.BOSEconomyPlugin.getMoneyNamePluralCaps());
			}

			// Remove gold ingots
			HashMap<Integer,ItemStack> difference = pi.removeItem(new ItemStack(266, ingots));			
			difference.clear();
			return true;
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have " + Integer.toString(ingots) + ChatColor.DARK_RED + " gold ingots!");
			return true;
		}
	}

}
