package com.edtheloon.gold2economy;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import com.nijikokun.register.payment.Methods;

public class configHandler {
	
	// Configuration file properties
	private Plugin plugin;
	private File configFile = new File("plugins/Gold2Economy" + File.separator + "config.yml");
	private FileConfiguration config;
	
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
	
	// CONSTRUCTOR 
	public configHandler(Plugin _plugin) {
		this.plugin = _plugin;
		this.config = _plugin.getConfig();
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
		
		config.set("convert.iron", convertIron);
		config.set("convert.gold", convertGold);
		config.set("convert.diamond", convertDiamond);
		
		config.set("rates.iron", ironRate);
		config.set("rates.gold", goldRate);
		config.set("rates.diamond", diamondRate);
		
		config.set("permissions.use", Permissions);
		config.set("permissions.Permissions", usePermissions);
		config.set("permissions.PermissionsBukkit", useBukkitPerms);
		
		config.set("preferred", preferred);
		plugin.saveConfig();
		
		// Now that Config is created we can load the values
		loadConfig();
	}
	
	// Load configuration file
	public void loadConfig() {
		
		plugin.reloadConfig();
		config = plugin.getConfig();
		
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
		preferred = config.getString("preferred");
		Methods.setPreferred(preferred);
	}
}
