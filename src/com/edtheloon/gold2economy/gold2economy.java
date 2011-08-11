// Plugin: Gold2iConomy
// Author: EdTheLoon
// Date (last modified): 11/08/11 19:08 by EdTheLoon
// License : GNU GPL v3

package com.edtheloon.gold2economy;

import java.util.logging.Logger;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.iConomy.*;
import com.nijiko.permissions.PermissionHandler;
import cosine.boseconomy.BOSEconomy;

public class gold2economy extends JavaPlugin {

	// Permission nodes
	public final static String PERMISSION_USE = "Gold2Economy.use";
	public final static String PERMISSION_ADMIN = "Gold2Economy.admin";

	// Config Handler, External APIs and class variables
	public static configHandler config;
	public static iConomy iConomyPlugin = null;
	public static BOSEconomy BOSEconomyPlugin = null;
	public static PermissionHandler permissionHandler;
	public static boolean enabled = false;
	public static PluginManager pm = null;
	public static boolean permissionsEnabled = false;

	// Minecraft Log
	public static Logger log = Logger.getLogger("Minecraft");

	public void onEnable() { 
		
		// Create a new instance of configHandler
		config = new configHandler(this);

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
		
		// Tell Bukkit that Commands class should handle command execution for this command
		getCommand("gi").setExecutor(new Commands());
		
		// Finally, log to console that the plugin has finished initialising and is enabled.
		log.info("[Gold2Economy] Version " + this.getDescription().getVersion().toString() + " enabled.");
		
	}

	public void onDisable() { 
		log.info("[Gold2Economy] Version " + this.getDescription().getVersion().toString() + " disabled.");
	}
}
