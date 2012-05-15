package com.edtheloon.gold2economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.feildmaster.lib.configuration.EnhancedConfiguration;

public class API {

	private EnhancedConfiguration config; //Added by turt2live
	private VaultSupport vault; //Added by turt2live
	private gold2economy plugin; //Added by turt2live

	/**
	 * Creates a new instance of the API
	 */
	public API(){
		plugin = (gold2economy) Bukkit.getServer().getPluginManager().getPlugin("Gold2Economy-Vault");
		vault = new VaultSupport(plugin);
		vault.init();
		config = plugin.getConfig();
	}

	/**
	 * Determines if an item can be sold
	 * 
	 * @param itemID the item ID to test
	 * @return true if the item can be sold
	 */
	public boolean canBeConverted(int itemID){
		return Converter.isAllowed(itemID);
	}

	/**
	 * Determines if diamond can be sold
	 * 
	 * @param player the CommandSender selling the diamond
	 * @return true if the CommandSender can sell diamond
	 */
	public boolean canSellDiamond(CommandSender player){
		return vault.hasPermission(player, getPermissionDiamond());
	}

	/**
	 * Determines if gold can be sold
	 * 
	 * @param player the CommandSender selling the gold
	 * @return true if the CommandSender can sell gold
	 */
	public boolean canSellGold(CommandSender player){
		return vault.hasPermission(player, getPermissionGold());
	}

	/**
	 * Determines if iron can be sold
	 * 
	 * @param player the CommandSender selling the iron
	 * @return true if the CommandSener can sell iron
	 */
	public boolean canSellIron(CommandSender player){
		return vault.hasPermission(player, getPermissionIron());
	}

	/**
	 * Converts an item
	 * 
	 * @param sender the command sender converting the item
	 * @param itemID the item ID to be converted
	 * @param amount the amount of item ID to be converted
	 */
	public void convert(CommandSender sender, int itemID, int amount){
		Converter.convertItem(sender, itemID, amount, vault, config);
	}

	/**
	 * Displays the rates table to a CommandSender
	 * 
	 * @param sender the CommandSender to get the rates table
	 */
	public void displayRates(CommandSender sender){
		Functions.displayRates(sender, vault, config);
	}

	/**
	 * Gets the configuration
	 * 
	 * @return the configuration
	 */
	public EnhancedConfiguration getConfig(){
		return config; //Modified by turt2live
	}

	/**
	 * Gets the admin permission
	 * 
	 * @return the admin permission
	 */
	public String getPermissionAdmin(){
		return gold2economy.PERMISSION_ADMIN;
	}

	/**
	 * Gets the diamond permission
	 * 
	 * @return the diamond permission
	 */
	public String getPermissionDiamond(){
		return gold2economy.PERMISSION_DIAMOND;
	}

	/**
	 * Gets the gold permission
	 * 
	 * @return the gold permission
	 */
	public String getPermissionGold(){
		return gold2economy.PERMISSION_GOLD;
	}

	/**
	 * Gets the iron permission
	 * 
	 * @return the iron permission
	 */
	public String getPermissionIron(){
		return gold2economy.PERMISSION_IRON;
	}

	/**
	 * Gets the rate at which an item will sell
	 * 
	 * @param itemID the item ID to test
	 * @return the conversion rate per 1 item ID
	 */
	public double getRate(int itemID){
		return Converter.getRate(itemID);
	}

	/**
	 * Get the Vault class directly
	 * 
	 * @return the Vault class
	 */
	public VaultSupport getVaultSupport(){
		return vault;
	}

	/**
	 * Determines if a CommandSender has admin access
	 * 
	 * @param player the CommandSender to test
	 * @return true if the CommandSender has admin access
	 */
	public boolean isAdmin(CommandSender player){
		return player.hasPermission(getPermissionAdmin());
	}

	/**
	 * Determines if the plugin is 'enabled'
	 * 
	 * @return true if the plugin is enabled
	 */
	public boolean isEnabled(){
		return gold2economy.enabled;
	}

	/**
	 * Determines if Vault is active and has a hook into an economy
	 * 
	 * @return true if Vault is being used and can access an economy
	 */
	public boolean isVaultUsed(){
		return vault.isActive();
	}

	/**
	 * Reloads the configuration
	 */
	public void reloadConfig(){
		config.load();
	}

	/**
	 * Reloads the configuration, alerting a CommandSender
	 * 
	 * @param sender the CommandSender to alert
	 */
	public void reloadConfig(CommandSender sender){
		if(config.load()){
			sender.sendMessage("[" + plugin.getDescription().getName() + "] " + ChatColor.DARK_GREEN + "Configuration Reloaded");
		}else{
			sender.sendMessage("[" + plugin.getDescription().getName() + "] " + ChatColor.DARK_RED + "Configuration failed to reload");
		}
	}
}