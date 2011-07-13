package me.areid.gold2iconomy;

import java.util.HashMap;
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

	//static String pluginDir = "plugins/Gold2iConomy";
	//static File config = new File(pluginDir + File.separator + "config.yml");
	//static Properties prop = new Properties();
	public final String PERMISSION_USE = "Gold2iConomy.use";
	public final String PERMISSION_ADMIN = "Gold2iConomy.admin";

	public configHandler config = new configHandler();
	public iConomy iConomyPlugin = null;
	public static PermissionHandler permissionHandler;
	public boolean usePermissions = false;	

	Logger log = Logger.getLogger("Minecraft");

	public void onEnable() { 
		if (config.checkConfig()) {
			config.loadConfig();
		} else {
			config.createConfig();
		}
		log.info("[Gold2iConomy] Plugin enabled.");
		getServer().getPluginManager().registerEvent(Type.PLUGIN_ENABLE, new server(this), Priority.Monitor, this);
		getServer().getPluginManager().registerEvent(Type.PLUGIN_DISABLE, new server(this), Priority.Monitor, this);
		setupPermissions();
	}

	public void onDisable() { 
		log.info("[Gold2iConomy] Plugin disabled.");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("gi")) {
			if (args.length == 0) {
				sender.sendMessage("Conversion rate: 1 gold ingot = " + iConomy.format(config.cRate));
				return true;
			}

			if (args[0].equalsIgnoreCase("reload")) {
				if (usePermissions && permissionHandler.has((Player)sender, PERMISSION_ADMIN) | sender.isOp()) {
					giReload(sender);
					return true;
				}
			}

			if (args[0].equalsIgnoreCase("all")) {
				Player player = (Player)sender;
				PlayerInventory pi = player.getInventory();
				
				Integer i;
				ItemStack items[] = pi.getContents();
				Integer ingots = 0;
				
				for (i = 0; i < items.length; i++)
				{
					if (items[i].getTypeId() == 266) {
						ingots = ingots + items[i].getAmount();
					}
				}
				convertGold(sender, ingots);
				return true;
			}

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
				} catch (NumberFormatException e) {
					log.info("[Gold2iConomy] ERROR: " + e.toString());
					return false;
				}
			}
		}
		return false;
	}

	public boolean convertGold(CommandSender sender, Integer ingots)
	{
		//Integer ingots = 0;
		/*try {
			if (args[0].isEmpty())
			{
				sender.sendMessage("[Gold2iConomy] You did not enter an amount");
				return true;
			}
			ingots = new Integer(args[0]);
		} catch (NumberFormatException e) {
			sender.sendMessage("[Gold2iConomy] You did not enter an amount");
			return false;
		}*/

		Double conversion = config.cRate * ingots;
		Player player = (Player)sender;
		PlayerInventory pi = player.getInventory();

		if (pi.contains(266, ingots))
		{				
			Holdings balance = iConomy.getAccount(player.getName()).getHoldings();
			balance.add(conversion);
			HashMap<Integer,ItemStack> difference = pi.removeItem(new ItemStack(266, ingots));
			sender.sendMessage("You converted " + Integer.toString(ingots) + " ingots into " + iConomy.format(conversion));
			sender.sendMessage("You now have " + iConomy.format(player.getName()));
			difference.clear();
			return true;
		} else {
			sender.sendMessage("You do not have " + Integer.toString(ingots) + " gold ingots!");
			return true;
		}
	}

	public boolean giReload(CommandSender sender) {
		config.loadConfig();
		sender.sendMessage("[Gold2iConomy] Reloaded. Rate is " + config.cRate.toString());
		return true;
	}

	private void setupPermissions() {
		if (permissionHandler != null) {
			return;
		}

		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

		if (permissionsPlugin == null) {
			log.info("[Gold2iConomy] Permissions not detected. Everyone can use /gi. Only ops can use /gi-reload");
			usePermissions = false;
			return;
		}

		permissionHandler = ((Permissions) permissionsPlugin).getHandler();
		usePermissions = true;
		log.info("[Gold2iConomy] Using "+((Permissions)permissionsPlugin).getDescription().getFullName());
	}

}
