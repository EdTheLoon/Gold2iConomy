package com.edtheloon.gold2economy;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

// Class written by Turt2Live: mc.turt2live.com/plugins
public class VaultSupport {

	private boolean	vault		= false;
	private boolean	register	= false;
	private boolean	both		= false;		//Used by the API only (for now?)
	private Plugin	plugin		= null;
	private String	method		= "Unknown";

	private Economy	economy		= null;

	public VaultSupport(Plugin p) {
		plugin = p;
	}

	public void setUsed(boolean b) {
		vault = b;
	}

	public void setRegister(boolean b) {
		register = b;
	}

	public void setBoth(boolean b) {
		both = b;
	}

	public void setBoth(Plugin vault, Plugin register) {
		both = ((vault != null && register != null) ? true : false);
	}

	public boolean hasBoth() {
		return both;
	}

	public boolean isActive() {
		return vault;
	}

	public boolean hasRegister() {
		return register;
	}

	public String method() {
		return method;
	}

	public boolean init() {
		RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
			vault = true;
			method = economy.getName();
		} else {
			vault = false;
			economy = null;
		}
		// System.out.println(economy); //DEBUG (turt2live)
		return vault;
	}

	public void deposit(String player, double amount) {
		// System.out.println(player + " | " + amount); //DEBUG (turt2live)
		economy.depositPlayer(player, amount);
	}

	public void withdraw(String player, double amount) {
		economy.withdrawPlayer(player, amount);
	}

	public double balance(String player) {
		return economy.getBalance(player);
	}

	public String format(double amount) {
		return economy.format(amount);
	}
}
