package com.edtheloon.gold2economy;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

// Class written by Turt2Live: mc.turt2live.com/plugins
public class VaultSupport {

	private boolean	vault	= false;
	private Plugin	plugin	= null;
	private String	method	= "Unknown";

	private Economy	economy	= null;

	public VaultSupport(Plugin p) {
		plugin = p;
	}

	public void setUsed(boolean b) {
		vault = b;
	}

	public boolean isActive() {
		return vault;
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
		return vault;
	}

	public void deposit(String player, double amount) {
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
