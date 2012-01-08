package com.edtheloon.gold2economy;

import org.bukkit.command.CommandSender;

import com.nijikokun.register.payment.Method;

public class API {

	// Returns the method used
	public Method getMethod() {
		return gold2economy.usedMethod;
	}

	// Returns whether the plugin is 'enabled' or not
	public boolean isEnabled() {
		return gold2economy.enabled;
	}

	// Returns the configHandler being used
	public configHandler getConfig() {
		return gold2economy.config;
	}

	// Return the permission string for iron
	public String getPermissionIron() {
		return gold2economy.PERMISSION_IRON;
	}

	// Return the permission string for gold
	public String getPermissionGold() {
		return gold2economy.PERMISSION_GOLD;
	}

	// Return the permission string for diamond
	public String getPermissionDiamond() {
		return gold2economy.PERMISSION_DIAMOND;
	}

	// Return the permission string for admin
	public String getPermissionAdmin() {
		return gold2economy.PERMISSION_ADMIN;
	}

	public void reloadConfig() {
		Functions.giReload(null);
	}

	public void reloadConfig(CommandSender sender) {
		Functions.giReload(sender);
	}

	public void displayRates(CommandSender sender) {
		Functions.displayRates(sender);
	}

	@Deprecated
	// Because I need to fix the VaultSupport class passing (turt2live)
	public void convert(CommandSender sender, int itemID, int amount) {
		// Converter.convertItem(sender, itemID, amount); //Because of deprecation (turt2live)
	}

}