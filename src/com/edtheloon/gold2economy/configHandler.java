package com.edtheloon.gold2economy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.nijikokun.register.payment.Methods;

public class configHandler {

	// Configuration file properties
	private Plugin				plugin;
	private File				configFile		= new File("plugins/Gold2Economy" + File.separator + "config.yml");
	private FileConfiguration	config;

	// Default conversion rates
	public Double				ironRate		= 9.99;
	public Double				goldRate		= 29.99;
	public Double				diamondRate		= 99.99;

	// Default values for whether or not these items can be converted
	public boolean				convertIron		= false;
	public boolean				convertGold		= true;
	public boolean				convertDiamond	= false;

	// Should we use iConomy or BOSEconomy? Default values
	public String				preferred		= "iConomy 6";

	// Should we use permissions? Default false
	public boolean				Permissions		= false;

	// Which permissions plugin should we use?
	public boolean				useBukkitPerms	= true;
	public boolean				usePermissions	= false;

	private VaultSupport		vault;																				// Added by turt2live

	// CONSTRUCTOR
	public configHandler(Plugin _plugin, VaultSupport _vault) { // Added Vault class (turt2live)
		this.plugin = _plugin;
		this.config = _plugin.getConfig();
		this.vault = _vault; // Added by turt2live
		if (checkConfig()) loadConfig(); // Added by turt2live
	}

	// Check to see if config exists, returns true or false
	public boolean checkConfig() {
		if (!configFile.exists()) return false;
		else return true;
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
	// Removed defaults (DEBUG TEST) - turt2live
	public void loadConfig() {

		plugin.reloadConfig();
		// config = plugin.getConfig(); //EdTheLoon
		config = new YamlConfiguration(); // Config fix (not liking this though...) - Turt2Live
		try { // Config fix (not liking this though...) - Turt2Live [TODO: Fix catch blocks to better handle possible errors]
			config.load(configFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

		// Load which items we can convert
		// System.out.println(config.getBoolean("convert.iron")); //DEBUG LINE - Turt2Live
		convertIron = config.getBoolean("convert.iron");
		convertGold = config.getBoolean("convert.gold");
		convertDiamond = config.getBoolean("convert.diamond");

		// Load the conversion rates
		ironRate = config.getDouble("rates.iron");
		goldRate = config.getDouble("rates.gold");
		diamondRate = config.getDouble("rates.diamond");

		// Load whether we shall use permissions or not, and which permissions to use
		Permissions = config.getBoolean("permissions.use");
		usePermissions = config.getBoolean("permissions.Permissions");
		useBukkitPerms = config.getBoolean("permissions.PermissionsBukkit");

		// Load which economy system to use
		preferred = config.getString("preferred");
		// Set preferred if Register was found (if Register is found then enabled will be true.
		if (gold2economy.enabled) if (vault.hasRegister()) Methods.setPreferred(preferred);
	}

	// Added reload (turt2live) - Seemed to work, not ideal though
	public void reload() {
		plugin.reloadConfig();
		loadConfig();
	}
}
