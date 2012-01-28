package com.edtheloon.gold2economy;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.nijikokun.register.payment.Method;

public class API {

	private configHandler config; //Added by turt2live
	private VaultSupport vault; //Added by turt2live
	private Plugin plugin; //Added by turt2live

	//Added constructor
	public API(){
		plugin = Bukkit.getServer().getPluginManager().getPlugin("Gold2Economy-Vault");
		vault = new VaultSupport(plugin);
		vault.init();
		config = new configHandler(plugin, vault);
		config.loadConfig();
	}

	//Returns TRUE if the CommandSender can sell diamond
	public boolean canSellDiamond(CommandSender player){
		return player.hasPermission(getPermissionDiamond());
	}

	//Returns TRUE if the CommandSender can sell gold
	public boolean canSellGold(CommandSender player){
		return player.hasPermission(getPermissionGold());
	}

	//Returns TRUE if the CommandSender can sell iron
	public boolean canSellIron(CommandSender player){
		return player.hasPermission(getPermissionIron());
	}

	//Converts an item for a user (turt2live)
	public void convert(CommandSender sender, int itemID, int amount){
		Converter.convertItem(sender, itemID, amount, vault, config);
	}

	//Displays the rates to the CommandSender (turt2live)
	public void displayRates(CommandSender sender){
		Functions.displayRates(sender, vault, config);
	}

	// Returns the configHandler being used
	public configHandler getConfig(){
		return config; //Modified by turt2live
	}

	// Returns the method used
	public Method getMethod(){
		return gold2economy.usedMethod;
	}

	// Return the permission string for admin
	public String getPermissionAdmin(){
		return gold2economy.PERMISSION_ADMIN;
	}

	// Return the permission string for diamond
	public String getPermissionDiamond(){
		return gold2economy.PERMISSION_DIAMOND;
	}

	// Return the permission string for gold
	public String getPermissionGold(){
		return gold2economy.PERMISSION_GOLD;
	}

	// Return the permission string for iron
	public String getPermissionIron(){
		return gold2economy.PERMISSION_IRON;
	}

	//METHODS ADDED BY TURT2LIVE

	//get the VaultSupport class directly
	public VaultSupport getVaultSupport(){
		return vault;
	}

	//Returns TRUE if the CommandSender has admin access
	public boolean isAdmin(CommandSender player){
		return player.hasPermission(getPermissionAdmin());
	}

	// Returns whether the plugin is 'enabled' or not
	public boolean isEnabled(){
		return gold2economy.enabled;
	}

	//Returns TRUE if Register is being used as the economy hook
	public boolean isRegisterUsed(){
		return vault.hasRegister();
	}

	//Returns TRUE if both Vault and Register COULD be used
	public boolean isVaultAndRegisterUsed(){
		return vault.hasBoth();
	}

	//Returns TRUE if Vault is being used as the economy hook
	public boolean isVaultUsed(){
		return vault.isActive();
	}

	//Reloads the configuration (turt2live)
	public void reloadConfig(){
		//Removed deprecated Functions.giReload() call (turt2live)
		config.reload(null); //Turt2Live: Passing null into CommandSender 
								//doesn't break anything, just means no display 
								//message is sent
	}

	//Reloads the configuration from a command sender (turt2live)
	public void reloadConfig(CommandSender sender){
		//Removed deprecated Functions.giReload() call (turt2live)
		config.reload(null);
	}
}