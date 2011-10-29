package com.edtheloon.gold2economy;

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

}