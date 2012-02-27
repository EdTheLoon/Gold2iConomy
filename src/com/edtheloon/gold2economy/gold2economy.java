// Plugin: Gold2iConomy
// Author: EdTheLoon
// Date (last modified): 11/08/11 19:08 by EdTheLoon
// License : GNU GPL v3

package com.edtheloon.gold2economy;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.feildmaster.lib.configuration.PluginWrapper;

public class gold2economy extends PluginWrapper { //To implement feildmaster's config library (turt2live)

	// Permission nodes
	public final static String PERMISSION_IRON = "Gold2Economy.iron";
	public final static String PERMISSION_GOLD = "Gold2Economy.gold";
	public final static String PERMISSION_DIAMOND = "Gold2Economy.diamond";
	public final static String PERMISSION_ADMIN = "Gold2Economy.admin";

	// Config Handler, External APIs and class variables
	public static boolean enabled = false;
	public static PluginManager pm = null;
	public static boolean permissionsEnabled = false;
	//	public static Method usedMethod = null;
	public static VaultSupport vault = null; // turt2live: Start of support for Vault
	public API api = null; // Added by turt2live

	// Minecraft Log
	public static Logger log = Logger.getLogger("Minecraft");

	// Added by turt2live for gold nuggets and such
	public EnhancedConfiguration getConversionChart(){
		EnhancedConfiguration items = new EnhancedConfiguration(new File(getDataFolder(), "items.yml"), this);
		items.loadDefaults(getResource("resources/items.yml"));
		if(items.needsUpdate()){
			items.saveDefaults();
		}
		items.load();
		return items;
	}

	@Override
	public void onDisable(){
		log.info("[" + getDescription().getName() + "] Version " + getDescription().getVersion().toString() + " disabled.");
		enabled = false;
	}

	@Override
	public void onEnable(){
		//Event Handler : Turt2Live
		server s = new server(this);
		s.init();
		//End Event Handler : Turt2Live

		/*
		 * First check to see if Register is installed/enabled. 
		 * It is safe to perform this check at this point in the code because in our plugin.yml we have added a softdepend. 
		 * Meaning that it will only be enabled once Register has been found. 
		 * It will still run however if Register is not found, but will enable last.
		 */

		/*
		 * Turt2Live: Added a Vault check to support Vault users. This has also been added to the softdepend list
		 */
		pm = Bukkit.getServer().getPluginManager(); //Added by turt2live
		vault = new VaultSupport(this);
		Plugin vault_plugin = pm.getPlugin("Vault");
		boolean hasVault = false;
		if(vault_plugin != null){
			if(pm.isPluginEnabled(vault_plugin)){
				hasVault = true;
			}
		}
		//		Plugin plugin = pm.getPlugin("Register");
		//		if(!pm.isPluginEnabled(plugin) && !hasVault){ // Added !hasVault (turt2live)
		//			enabled = false;
		//			// Added Vault to error (turt2live)
		//			log.severe("[" + getDescription().getName() + "] Register or Vault was not found. G2E will not function properly until it is enabled");
		//		}else // Check if register has a method yet and then take note of the method
		// Wrapped in a hasVault IF statement (turt2live)
		if(hasVault){
			boolean success = vault.init();
			// turt2live: Add the required stuff so the plugin doesn't die
			enabled = true;
			//vault.setRegister(false);
			vault.setUsed(true);
			if(!success){
				enabled = false;
				log.info("[" + getDescription().getName() + "] Version " + getDescription().getVersion().toString() + ": Vault found! No method though :(");
			}else{
				log.info("[" + getDescription().getName() + "] Version " + getDescription().getVersion().toString() + " enabled. Using " + vault.method() + " [VAULT]");
			}
		}//else{
			//			vault.setUsed(false); // Added by turt2live
			//			if(Methods.hasMethod()){
			//				usedMethod = Methods.getMethod();
			//			}
			//			vault.setRegister(true);
			//			// Finally, log to console that the plugin has finished initialising and is enabled.
			//			enabled = true;
			//			log.info("[" + getDescription().getName() + "] Version " + getDescription().getVersion().toString() + " enabled. Using " + usedMethod.getName());
		//}
		//vault.setBoth(vault_plugin, plugin); //For the API
		// Moved to below vault check (turt2live)
		// Start configuration (turt2live)
		//Load the defaults, just in case (turt2live)
		getConfig().loadDefaults(getResource("resources/config.yml"));
		/*If the file doesn't exist or the defaults are missing/not there, 
		  save the defaults to the config (turt2live)
		*/
		if(getConfig().needsUpdate()){
			getConfig().saveDefaults();
		}
		// Moved this down to below the Vault check (turt2live)
		// Tell Bukkit that Commands class should handle command execution for this command
		getCommand("gi").setExecutor(new Commands(this, vault)); // Fixed for argument change (turt2live)
		// Start API
		api = new API();
	}
}
