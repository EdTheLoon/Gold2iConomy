// Plugin: Gold2iConomy
// Author: EdTheLoon
// Date (last modified): 13/07/11 07:43 by EdTheLoon
// License : GNU GPL v3

package me.areid.gold2iconomy;

import java.util.HashMap;
import org.bukkit.ChatColor;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import com.iConomy.*;
import com.iConomy.system.Holdings;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class gold2iconomy extends JavaPlugin {

	// Permission nodes
	public final String PERMISSION_USE = "Gold2iConomy.use";
	public final String PERMISSION_ADMIN = "Gold2iConomy.admin";

	// Configuration handler and external APIs
	public configHandler config = new configHandler();
	public iConomy iConomyPlugin = null;
	public static PermissionHandler permissionHandler;
	public boolean usePermissions = false;	

	// Minecraft Log
	Logger log = Logger.getLogger("Minecraft");

	public void onEnable() { 

		// Check to see if configuration exists. If it exists then load it; if not then create it
		if (config.checkConfig()) {
			config.loadConfig();
		} else {
			config.createConfig();
		}

		// Register plugin enable and disable events
		getServer().getPluginManager().registerEvent(Type.PLUGIN_ENABLE, new server(this), Priority.Monitor, this);
		getServer().getPluginManager().registerEvent(Type.PLUGIN_DISABLE, new server(this), Priority.Monitor, this);

		// Hook into permissions (if available)
		setupPermissions();
		log.info("[Gold2iConomy] Plugin enabled.");
	}

	public void onDisable() { 
		log.info("[Gold2iConomy] Plugin disabled.");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// Command = /gi
		if (cmd.getName().equalsIgnoreCase("gi")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GREEN + "Conversion rate: 1 gold ingot = " + iConomy.format(config.cRate));
				return true;
			}

			// Reload configuration
			if (args[0].equalsIgnoreCase("reload")) {
				if (usePermissions && permissionHandler.has((Player)sender, PERMISSION_ADMIN) | sender.isOp()) {
					giReload(sender);
					return true;
				}
			}

			// Convert all gold
			if (args[0].equalsIgnoreCase("all")) {
				Player player = (Player)sender;
				PlayerInventory pi = player.getInventory();				
				ItemStack items[] = pi.getContents();

				if (pi.contains(266)) {
					Integer ingots = 0;
					Integer i = 0;

					for (i = 0; i < items.length; i++)
					{
						if (items[i].getTypeId() == 266) {
							ingots = ingots + items[i].getAmount();
						}
					}
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "You don't have any gold ingots to convert!");
					return true;
				}
			}

			// Convert <amount> of gold
			if (sender instanceof Player) {

				Integer ingots = 0;
				try {
					ingots = Integer.parseInt(args[0]);
					if (!usePermissions) {
						convertGold(sender, ingots);
						return true;
					} else if (permissionHandler.has((Player)sender, PERMISSION_USE)) {
						convertGold(sender, ingots);
						return true;
					}
				} catch (NumberFormatException e) { // This should only be done if anything other than reload, all or a number was entered.
					// Below line for debugging only
					//log.info("[Gold2iConomy] ERROR: " + e.toString());
					return false;
				}
			}
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
			Holdings balance = iConomy.getAccount(player.getName()).getHoldings();
			balance.add(conversion);
			HashMap<Integer,ItemStack> difference = pi.removeItem(new ItemStack(266, ingots));
			sender.sendMessage(ChatColor.GREEN + "You converted " + Integer.toString(ingots) + " ingots into " + iConomy.format(conversion));
			sender.sendMessage(ChatColor.GREEN + "You now have " + iConomy.format(player.getName()));
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
		sender.sendMessage(ChatColor.GREEN + "[Gold2iConomy] " + ChatColor.WHITE + "Reloaded. Rate is " + config.cRate.toString());
		return true;
	}

	// Hook into permissions (if available)
	private void setupPermissions() {
		if (permissionHandler != null) {
			return;
		}

		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

		// If Permissions is not detected display a message to server console and set usePermissions to false
		if (permissionsPlugin == null) {
			log.info("[Gold2iConomy] Permissions not detected. Everyone can use /gi. Only ops can use /gi reload");
			usePermissions = false;
			return;
		}

		// If Permissions is detected then hook in and display message to server console and set usePermissions to true
		permissionHandler = ((Permissions) permissionsPlugin).getHandler();
		usePermissions = true;
		log.info("[Gold2iConomy] Using "+((Permissions)permissionsPlugin).getDescription().getFullName());
	}

}
