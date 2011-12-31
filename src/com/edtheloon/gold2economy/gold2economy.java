// Plugin: Gold2iConomy
// Author: EdTheLoon
// Date (last modified): 11/08/11 19:08 by EdTheLoon
// License : GNU GPL v3

package com.edtheloon.gold2economy;

import java.util.logging.Logger;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Methods;

public class gold2economy extends JavaPlugin {

	// Permission nodes
	public final static String PERMISSION_IRON = "Gold2Economy.iron";
	public final static String PERMISSION_GOLD = "Gold2Economy.gold";
	public final static String PERMISSION_DIAMOND = "Gold2Economy.diamond";
	public final static String PERMISSION_ADMIN = "Gold2Economy.admin";

	// Config Handler, External APIs and class variables
	public static configHandler config;
	public static PermissionHandler permissionHandler = null;
	public static boolean enabled = false;
	public static PluginManager pm = null;
	public static boolean permissionsEnabled = false;
	public static Method usedMethod = null;
	public static VaultSupport		vault				= null;							// turt2live: Start of support

	// Minecraft Log
	public static Logger log = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() { 

		// Create a new instance of configHandler
		config = new configHandler(this);

		// Check to see if configuration exists. If it exists then load it; if not then create it
		if (config.checkConfig()) config.loadConfig();
		else config.createConfig();

		// Register plugin enable and disable events
		pm = getServer().getPluginManager();
		pm.registerEvent(Type.PLUGIN_ENABLE, new server(this), Priority.Monitor, this);
		pm.registerEvent(Type.PLUGIN_DISABLE, new server(this), Priority.Monitor, this);

		// Tell Bukkit that Commands class should handle command execution for this command
		getCommand("gi").setExecutor(new Commands());

		/* First check to see if Register is installed/enabled. It is safe to perform this check at this
		 * point in the code because in our plugin.yml we have added a softdepend. Meaning that
		 * it will only be enabled once Register has been found. It will still run however if Register is
		 * not found, but will enable last.
		 */

		/*
		 * Turt2Live: Added a Vault check to support Vault users. This has also been added to the softdepend list
		 */
		vault = new VaultSupport(this);
		Plugin vault_plugin = pm.getPlugin("Vault");
		boolean hasVault = false;
		if (vault_plugin != null)
			if (pm.isPluginEnabled(vault_plugin)) hasVault = true;
		Plugin plugin = pm.getPlugin("Register");
		if (!pm.isPluginEnabled(plugin) && !hasVault) { // Added !hasVault (turt2live)
			enabled = false;
			// Added Vault to error (turt2live)
			log.severe("[Gold2Economy] Register or Vault was not found. G2E will not function properly until it is enabled");
		} else // Check if register has a method yet and then take note of the method
			//Wrapped in a hasVault IF statement (turt2live)
			if(hasVault){
				boolean success = vault.init();
				// turt2live: Add the required stuff so the plugin doesn't die
				enabled = true;
				if(!success){
					enabled = false;
					log.info("[Gold2Economy] Version "+getDescription().getVersion().toString()+": Vault found! No method though :(");
				} else log.info("[Gold2Economy] Version "+getDescription().getVersion().toString()+" enabled. Using "+vault.method()+" [VAULT]");
			}else{
				vault.setUsed(false); //Added by turt2live
				if (Methods.hasMethod()) usedMethod = Methods.getMethod();

				// Finally, log to console that the plugin has finished initialising and is enabled.
				enabled = true;
				log.info("[Gold2Economy] Version " + getDescription().getVersion().toString() + " enabled. Using " + usedMethod.getName());
			}

	}

	@Override
	public void onDisable() { 
		log.info("[Gold2Economy] Version " + getDescription().getVersion().toString() + " disabled.");
		enabled = false;
	}
}
