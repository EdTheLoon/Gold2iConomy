package com.edtheloon.gold2economy;

import java.io.File;

import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.Configuration;

public class configHandler {
	
	// Configuration file properties
	private Plugin plugin;
	private File configFile = new File("plugins/Gold2Economy" + File.separator + "config.yml");
	private Configuration config;
	
	// Default conversion rates
	public Double ironRate = 9.99;
	public Double goldRate = 29.99;
	public Double diamondRate = 99.99;
	
	// Default values for whether or not these items can be converted
	public boolean convertIron = false;
	public boolean convertGold = true;
	public boolean convertDiamond = false;
	
	// Should we use iConomy or BOSEconomy? Default values
	public boolean iConomy = true;
	public boolean BOSEconomy = false;
	
	// Should we use permissions? Default false
	public boolean Permissions = false;
	
	// Which permissions plugin should we use?
	public boolean useBukkitPerms = true;
	public boolean usePermissions = false;
	
	// CONSTRUCTOR 
	public configHandler(Plugin _plugin) {
		this.plugin = _plugin;
		this.config = new Configuration(configFile);
	}
	
	// Check to see if config exists, returns true or false
	public boolean checkConfig() {
		if (!configFile.exists())
		{
			return false;
		} else {
			return true;
		}
	}
	
	// Create the configuration file and insert default values
	public void createConfig() {
		
		config.setProperty("convert.iron", convertIron);
		config.setProperty("convert.gold", convertGold);
		config.setProperty("convert.diamond", convertDiamond);
		
		config.setProperty("rates.iron", ironRate);
		config.setProperty("rates.gold", goldRate);
		config.setProperty("rates.diamond", diamondRate);
		
		config.setProperty("permissions.use", Permissions);
		config.setProperty("permissions.Permissions", usePermissions);
		config.setProperty("permissions.PermissionsBukkit", useBukkitPerms);
		
		config.setProperty("economy.iConomy", iConomy);
		config.setProperty("economy.BOSEconomy", BOSEconomy);
		config.setHeader("# Gold2Economy Version " + plugin.getDescription().getVersion());
		config.save();
		
		// Now that Config is created we can load the values
		loadConfig();
	}
	
	// Load configuration file
	public void loadConfig() {
		config.load();			
		
		// Load which items we can convert
		convertIron = config.getBoolean("convert.iron", convertIron);
		convertGold = config.getBoolean("convert.gold", convertGold);
		convertDiamond = config.getBoolean("convert.diamond", convertDiamond);
		
		// Load the conversion rates
		ironRate = config.getDouble("rates.iron", ironRate);
		goldRate = config.getDouble("rates.gold", goldRate);
		diamondRate = config.getDouble("rates.diamond", diamondRate);
		
		// Load whether we shall use permissions or not, and which permissions to use
		Permissions = config.getBoolean("permissions.use", false);
		usePermissions = config.getBoolean("permissions.Permissions", false);
		useBukkitPerms = config.getBoolean("permissions.PermissionsBukkit", true);
		
		// Load which economy system to use
		iConomy = config.getBoolean("economy.iConomy", true);
		BOSEconomy = config.getBoolean("economy.BOSEconomy", false);
	}
}
