// Plugin: Gold2iConomy
// Author: EdTheLoon
// Date (last modified): 13/07/11 07:43 by EdTheLoon
// License : GNU GPL v3

package com.edtheloon.gold2economy;

import java.util.HashMap;
import org.bukkit.ChatColor;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.iConomy.*;
import com.iConomy.system.Holdings;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import cosine.boseconomy.BOSEconomy;

public class gold2economy extends JavaPlugin {

	// Permission nodes
	public final String PERMISSION_USE = "Gold2Economy.use";
	public final String PERMISSION_ADMIN = "Gold2Economy.admin";

	// Config Handler, External APIs and class variables
	public configHandler config = new configHandler(this);
	public iConomy iConomyPlugin = null;
	public BOSEconomy BOSEconomyPlugin = null;
	public static PermissionHandler permissionHandler;
	public static boolean enabled = false;
	public static PluginManager pm = null;
	public static boolean permissionsEnabled = false;

	// Minecraft Log
	public static Logger log = Logger.getLogger("Minecraft");

	public void onEnable() { 

		// Check to see if configuration exists. If it exists then load it; if not then create it
		if (config.checkConfig()) {
			config.loadConfig();
		} else {
			config.createConfig();
		}

		// Register plugin enable and disable events
		pm = getServer().getPluginManager();
		pm.registerEvent(Type.PLUGIN_ENABLE, new server(this), Priority.Monitor, this);
		pm.registerEvent(Type.PLUGIN_DISABLE, new server(this), Priority.Monitor, this);
		log.info("[Gold2Economy] Enabled. Version " + this.getDescription().getVersion().toString());

		// Hook into iConomy
		if (config.iConomy && iConomyPlugin == null) {
			if (pm.getPlugin("iConomy").isEnabled()) {
				iConomyPlugin = (iConomy) pm.getPlugin("iConomy");
				enabled = true;
				log.info("[Gold2Economy] Hooked into " + iConomyPlugin.getDescription().getName() + " Version " + iConomyPlugin.getDescription().getVersion());
			} else {
				log.info("[Gold2Economy] iConomy not detected. Disabling.");
				enabled = false;
			}
		}

		// Hook into BOSEconomy
		if (config.BOSEconomy && BOSEconomyPlugin == null) {
			if (pm.getPlugin("BOSEconomy").isEnabled()) {
				iConomyPlugin = (iConomy) pm.getPlugin("BOSEconomy");
				enabled = true;
				log.info("[Gold2Economy] Hooked into " + BOSEconomyPlugin.getDescription().getName() + " Version " + BOSEconomyPlugin.getDescription().getVersion());
			} else {
				log.info("[Gold2Economy] BOSEconomy not detected. Disabling.");
				enabled = false;
			}
		}

		// Hook into Permissions
		if (config.usePermissions && permissionHandler == null) {
			Plugin PermissionsPlugin = getServer().getPluginManager().getPlugin("Permissions");
			if (PermissionsPlugin.isEnabled()) {
				permissionsEnabled = true;
				permissionHandler = ((Permissions) PermissionsPlugin).getHandler();
				log.info("[Gold2Economy] Hooked into " + PermissionsPlugin.getDescription().getName() + " Version " + PermissionsPlugin.getDescription().getVersion());
			} else {
				permissionsEnabled = false;
				config.usePermissions = false;
				log.info("[Gold2Economy] Permissions not detected. Falling back to OP for reload command");
			}
		}
	}

	public void onDisable() { 
		log.info("[Gold2Economy] Plugin disabled. Version " + this.getDescription().getVersion().toString());
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// Command = /gi
		if (enabled) {
			if (cmd.getName().equalsIgnoreCase("gi")) {
				if (args.length == 0) {
					if (config.iConomy && iConomyPlugin != null) {
						sender.sendMessage(ChatColor.GREEN + "Conversion rate: 1 gold ingot = " + iConomy.format(config.cRate));
						return true;
					}
					if (config.BOSEconomy && BOSEconomyPlugin != null) {
						sender.sendMessage(ChatColor.GREEN + "Conversion rate: 1 gold ingot = " + config.cRate.toString());
						return true;
					}
				}

				// Reload configuration
				if (args[0].equalsIgnoreCase("reload")) {
					if (permissionsEnabled && permissionHandler.has((Player)sender, PERMISSION_ADMIN) || sender instanceof ConsoleCommandSender) {
						giReload(sender);
						return true;
					} else if (!config.usePermissions && sender.isOp()) {
						giReload(sender);
						return true;
					}
				}

				// Convert all gold
				if (args[0].equalsIgnoreCase("all")) {
					if (config.usePermissions && permissionsEnabled && permissionHandler.has((Player)sender, PERMISSION_USE) || !config.usePermissions) {
						// Declare and initialise local variables
						Player player = (Player)sender;
						PlayerInventory pi = player.getInventory();				
						ItemStack items[] = pi.getContents();

						// Only go ahead if the players inventory contains at least 1 gold ingot
						// Displays a message saying that the player doesn't have any gold if none is found
						if (pi.contains(266)) {
							Integer ingots = 0;

							// Loop through players inventory
							for (Integer i=0; i < items.length; i++) {
								if (items[i] != null)
								{
									// If at least 1 gold ingot is in the inventory slot then add the amount of gold in this slot to total ingots found
									if (items[i].getTypeId() == 266) {
										ingots = ingots + items[i].getAmount();
									}
								}
							}
							// Call convertGold to convert the gold ingots
							convertGold(sender, ingots);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have any gold ingots to convert!");
							return true;
						}
					}
				}

				// Convert <amount> of gold
				if (sender instanceof Player) {

					Integer ingots = 0;
					try {
						ingots = Integer.parseInt(args[0]);
						// if Permissions is not detected then all players can convert gold
						if (!config.usePermissions) {
							convertGold(sender, ingots);
							return true;
						} 
						// if Permissions is detected only players with permission can convert gold
						else if (config.usePermissions && permissionsEnabled && permissionHandler.has((Player)sender, PERMISSION_USE))
						{
							convertGold(sender, ingots);
							return true;
						}
					} catch (NumberFormatException e) { // This should only be done if anything other than reload, all or a number was entered.
						// Below line for debugging only
						//log.info("[Gold2iConomy] ERROR: " + e.toString());
						return false;
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Gold2iConomy is disabled because no currency sytem is enabled");
				return true;
			}
			return false;
		}
		return false;
	}

	// Convert Gold into iConomy money
	public boolean convertGold(CommandSender sender, Integer ingots)
	{

		Double conversion = config.cRate * ingots;
		Player player = (Player)sender;
		PlayerInventory pi = player.getInventory();

		// If user has enough ingots then convert gold, otherwise inform user that they do not have enough gold ingots
		if (pi.contains(266, ingots))
		{
			// If using iConomy
			if (config.iConomy && iConomyPlugin != null) {
				Holdings balance = iConomy.getAccount(player.getName()).getHoldings();
				balance.add(conversion);
				sender.sendMessage(ChatColor.GREEN + "You converted " + ingots + " ingots into " + iConomy.format(conversion));
				sender.sendMessage(ChatColor.GREEN + "You now have " + iConomy.format(player.getName()));
			} else if (config.BOSEconomy && BOSEconomyPlugin != null) { // If using BOSEconomy
				int money = Math.round(conversion.floatValue());
				BOSEconomyPlugin.addPlayerMoney(player.getName(), money, true);
				sender.sendMessage(ChatColor.GREEN + "You converted " + ingots + " ingots into " + money + " " + BOSEconomyPlugin.getMoneyNamePluralCaps());
				sender.sendMessage(ChatColor.GREEN + "You now have " + BOSEconomyPlugin.getPlayerMoney(player.getName()) + " " + BOSEconomyPlugin.getMoneyNamePluralCaps());
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

	// Reload configuration
	public boolean giReload(CommandSender sender) {
		config.loadConfig();
		sender.sendMessage(ChatColor.GREEN + "[Gold2Economy] " + ChatColor.WHITE + "Reloaded. Rate is " + config.cRate.toString());
		return true;
	}
}
