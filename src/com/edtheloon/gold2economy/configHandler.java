package com.edtheloon.gold2economy;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.nijikokun.register.payment.Methods;

public class configHandler extends EnhancedConfiguration { //Added by turt2live (extends)

	// Configuration file properties
	private Plugin plugin;
	//Removed config values (turt2live)

	// Default conversion rates
	public Double ironRate = 9.99;
	public Double goldRate = 29.99;
	public Double diamondRate = 99.99;

	// Default values for whether or not these items can be converted
	public boolean convertIron = false;
	public boolean convertGold = true;
	public boolean convertDiamond = false;

	// Should we use iConomy or BOSEconomy? Default values
	public String preferred = "iConomy 6";

	// Should we use permissions? Default false
	public boolean Permissions = false;

	// Which permissions plugin should we use?
	public boolean useBukkitPerms = true;
	public boolean usePermissions = false;

	private VaultSupport vault; // Added by turt2live

	// CONSTRUCTOR
	// Removed config global variables (turt2live)
	public configHandler(Plugin _plugin, VaultSupport _vault){ // Added Vault class (turt2live)
		super(_plugin); //Added by turt2live
		this.plugin = _plugin;
		this.vault = _vault; // Added by turt2live
		if(updateConfig()){ //Auto-load the config
			createConfig(); // Added by turt2live
		}else{
			loadConfig(); //Added by turt2live (auto-load config)
		}
	}

	// Create the configuration file and insert default values
	// Changed to private (turt2live)
	// Rewritten by turt2live (comments also by turt2live)
	private void createConfig(){
		//Load the defaults, just in case
		loadDefaults(plugin.getResource("resources/config.yml"));
		//If the file doesn't exist or the defaults are missing/not there, 
		//save the defaults to the config
		if(updateConfig()){ //More of a failsafe than a check (see loadConfig())
			saveDefaults();
		}
		//Reload again (also inits the file)
		loadConfig();
	}

	// Load configuration file
	// Removed defaults (DEBUG TEST) - turt2live
	// Changed all calls to plugin.getConfig() - turt2live
	public void loadConfig(){
		if(updateConfig()){
			createConfig();
			return; //So we don't have an infinite loop of loading (see last line line createConfig(), turt2live)
		}
		load();

		// Load which items we can convert
		// System.out.println(config.getBoolean("convert.iron")); //DEBUG LINE - Turt2Live
		convertIron = plugin.getConfig().getBoolean("convert.iron");
		convertGold = plugin.getConfig().getBoolean("convert.gold");
		convertDiamond = plugin.getConfig().getBoolean("convert.diamond");

		// Load the conversion rates
		ironRate = plugin.getConfig().getDouble("rates.iron");
		goldRate = plugin.getConfig().getDouble("rates.gold");
		diamondRate = plugin.getConfig().getDouble("rates.diamond");

		// Load whether we shall use permissions or not, and which permissions to use
		Permissions = plugin.getConfig().getBoolean("permissions.use");
		usePermissions = plugin.getConfig().getBoolean("permissions.Permissions");
		useBukkitPerms = plugin.getConfig().getBoolean("permissions.PermissionsBukkit");

		// Load which economy system to use
		preferred = plugin.getConfig().getString("preferred");
		// Set preferred if Register was found (if Register is found then enabled will be true.
		if(gold2economy.enabled){
			if(vault.hasRegister()){
				Methods.setPreferred(preferred);
			}
		}
	}

	// Added reload (turt2live) - Seemed to work, not ideal though
	public void reload(CommandSender sender){ //Removed by turt2live
		if(sender != null){
			sender.sendMessage("[" + plugin.getDescription().getName() + "] " + ChatColor.DARK_GREEN + "Configuration reloaded.");
		}
		loadConfig();
	}

	// Check to see if the config is in the correct format & is present
	// RETURNS: True if a config change is needed, false otherwise
	// Rewritten to determine if the config needs to be created
	private boolean updateConfig(){ //Changed to private & renamed: checkConfig -> updateConfig(turt2live)
		return (!fileExists() || !checkDefaults()) ? true : false; //Changed by turt2live
	}
}
