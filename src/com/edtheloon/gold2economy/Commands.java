package com.edtheloon.gold2economy;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.feildmaster.lib.configuration.EnhancedConfiguration;

public class Commands implements CommandExecutor {

	private gold2economy plugin = null; // Added by turt2live
	private VaultSupport vault = null; // Added by turt2live

	public Commands(gold2economy plugin, VaultSupport vault){ // Added by turt2live
		this.plugin = plugin;
		this.vault = vault;
	}

	// *** Removed static access calls to gold2economy.config EVERYWHERE here (turt2live)

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		// Initilize variables (turt2live)
		EnhancedConfiguration config = plugin.getConfig();
		boolean convertIron = config.getBoolean("convert.iron");
		boolean convertGold = config.getBoolean("convert.gold");
		boolean convertDiamond = config.getBoolean("convert.diamond");

		// Reload configuration to not cause issues (turt2live)
		config.load();

		// First check to see whether our plugin is 'enabled' and Register has a payment method
		if(gold2economy.enabled){ // Modified by turt2live (removed && Methods.hasMethod()
			// because the if should not continue of the plugin is not enabled

			// COMMAND - /gi
			if(cmd.getName().equalsIgnoreCase("gi")){

				// Command = /gi - Shows the help menu
				if(args.length == 0){
					return false;
				}

				// Command = /gi rates - Tells player the conversion rate
				if(args.length == 1 && args[0].equalsIgnoreCase("rates")){
					//					if(vault.hasRegister() || vault.isActive()){ // Modified by turt2live (Removed Methods.hasMethod())
					//						Functions.displayRates(sender, vault, config); // Fixed for argument change (turt2live)
					//						return true;
					//					}else{
					//						sender.sendMessage(ChatColor.RED + "Gold2Economy was unable to find a supported economy plugin");
					//						return true;
					//					}
					// ADDED FOR R5 SUPPORT
					if(vault.isActive()){
						Functions.displayRates(sender, vault, config); // Fixed for argument change (turt2live)
						return true;
					}else{
						sender.sendMessage(ChatColor.RED + "Gold2Economy was unable to find a supported economy plugin");
						return true;
					}
				}

				// Command = /gi reload - Reload configuration
				if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
					if(sender instanceof ConsoleCommandSender){
						//Functions.giReload(sender, config); // Fixed for argument change (turt2live)
						plugin.api.reloadConfig(sender); // removed old call, replaced with newer one (turt2live)
						return true;
					}else if(Permissions.check(sender, gold2economy.PERMISSION_ADMIN, config)){ // Fixed for argument change (turt2live)
						//Functions.giReload(sender, config); // Fixed for argument change (turt2live)
						plugin.api.reloadConfig(sender); // removed old call, replaced with newer one (turt2live)
						return true;
					}else{
						sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
						return true;
					}
				}

				// Command = /gi <item> all - Convert all <item> - <item> is either iron, gold or diamond
				if(args.length == 2 && args[1].equalsIgnoreCase("all") && sender instanceof Player){

					int itemID = 0;
					int amount = 0;
					String permNeeded = "";

					// Determine what itemID to use
					if(args[0].equalsIgnoreCase("iron")){
						itemID = 265;
						permNeeded = gold2economy.PERMISSION_IRON;
					}else if(args[0].equalsIgnoreCase("gold")){
						itemID = 266;
						permNeeded = gold2economy.PERMISSION_GOLD;
					}else if(args[0].equalsIgnoreCase("diamond")){
						itemID = 264;
						permNeeded = gold2economy.PERMISSION_DIAMOND;
					}else{
						try{
							itemID = Integer.parseInt(args[0]);
						}catch(Exception e){
							sender.sendMessage(ChatColor.RED + "That is not a valid item ID!");
						}
					}

					// Don't continue if server config says we can't convert item
					// Change this from a switch (itemID) to if conditions because it was causing bugs
					if(itemID == 265){ // IRON
						if(!convertIron){
							sender.sendMessage(ChatColor.RED + "This server doesn't allow iron to be converted");
							return true;
						}
					}else if(itemID == 266){ // GOLD
						if(!convertGold){
							sender.sendMessage(ChatColor.RED + "This server doesn't allow gold to be converted");
							return true;
						}
					}else if(itemID == 264){
						if(!convertDiamond){
							sender.sendMessage(ChatColor.RED + "This server doesn't allow diamond to be converted");
							return true;
						}
					}else if(!Converter.isAllowed(itemID)){
						sender.sendMessage(ChatColor.RED + "This server doesn't allow that to be converted! ");
						return true;
					}else if(Converter.isAllowed(itemID)){
						permNeeded = "Gold2Economy." + itemID;
					}else{ // Failsafe
						sender.sendMessage(ChatColor.RED + "This server doesn't allow that to be converted!");
						return true;
					}

					// Don't continue if player doesn't have required permission (if enabled)
					if(vault.hasPermission(sender, permNeeded)){ // Fixed for argument change (turt2live)
						sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
						return true;
					}

					// Prepare to loop through player inventory
					Player player = ((Player) sender);
					PlayerInventory pi = player.getInventory();
					ItemStack items[] = pi.getContents();

					// First check to see that user has at least 1 of <item>
					if(pi.contains(itemID)){

						// Loop through player's inventory to look for <item>
						for(ItemStack item : items){
							// if the inventory slot is not null AND it is the item we are looking for then change amount
							if(item != null && item.getTypeId() == itemID){
								amount += item.getAmount();
							}
						}
						Converter.convertItem(sender, itemID, amount, vault, config); // Added by turt2live & fixed for argument change
						return true; // finally say we've handled command correctly

					}else{
						sender.sendMessage(ChatColor.RED + "You don't have any of that item!");
						return true;
					}
				}

				// Command = /gi <item> <amount> - Convert <amount> of <item> - <item> is either iron, gold or diamond
				// If <amount> is left empty it will convert 1 of the item
				// Regular expression to check if args[1] is an integer
				if(args.length >= 1 && sender instanceof Player){
					int amount = 0;
					int itemID = 0;
					String permNeeded = "";

					// Use a try-catch to safely retrieve amount to convert
					try{
						if(args.length == 2){
							amount = Integer.parseInt(args[1]);
						}else if(args.length == 1){
							amount = 1;
						}
					}catch(NumberFormatException e){
						// DEBUG LINE
						gold2economy.log.severe("[" + vault.getPlugin().getDescription().getName() + "] Error: " + e.toString());
						return false;
					}

					// Determine what itemID to use
					if(args[0].equalsIgnoreCase("iron")){
						itemID = 265;
						permNeeded = gold2economy.PERMISSION_IRON;
					}else if(args[0].equalsIgnoreCase("gold")){
						itemID = 266;
						permNeeded = gold2economy.PERMISSION_GOLD;
					}else if(args[0].equalsIgnoreCase("diamond")){
						itemID = 264;
						permNeeded = gold2economy.PERMISSION_DIAMOND;
					}else{
						try{
							itemID = Integer.parseInt(args[0]);
						}catch(Exception e){
							sender.sendMessage(ChatColor.RED + "That is not a valid item ID!");
						}
					}

					// Don't continue if server config says we can't convert item
					// Change this from a switch (itemID) to if conditions because it was causing bugs
					if(itemID == 265){ // IRON
						if(!convertIron){
							sender.sendMessage(ChatColor.RED + "This server doesn't allow iron to be converted");
							return true;
						}
					}else if(itemID == 266){ // GOLD
						if(!convertGold){
							sender.sendMessage(ChatColor.RED + "This server doesn't allow gold to be converted");
							return true;
						}
					}else if(itemID == 264){
						if(!convertDiamond){
							sender.sendMessage(ChatColor.RED + "This server doesn't allow diamond to be converted");
							return true;
						}
					}else if(!Converter.isAllowed(itemID)){
						sender.sendMessage(ChatColor.RED + "This server doesn't allow that to be converted!");
						return true;
					}else if(Converter.isAllowed(itemID)){
						permNeeded = "Gold2Economy." + itemID;
					}else{ // Failsafe
						sender.sendMessage(ChatColor.RED + "This server doesn't allow that to be converted!");
						return true;
					}

					// Check if player has permission first
					if(vault.hasPermission(sender, permNeeded)){ // Fixed for argument change (turt2live)
						sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
						return true;
					}

					// Finally convert item into money
					Converter.convertItem(sender, itemID, amount, vault, config); // Fixed for argument change (turt2live)
					return true;

				}

			}
		}else if(gold2economy.enabled){ // This part will run if the plugin is not 'enabled'
			sender.sendMessage(ChatColor.RED + "Gold2Economy is disabled because no currency sytem was found");
			return true;
		}else{
			sender.sendMessage(ChatColor.RED + "Gold2Economy is disabled until Vault is enabled.");
			return true;
		}
		return false;
	}
}
