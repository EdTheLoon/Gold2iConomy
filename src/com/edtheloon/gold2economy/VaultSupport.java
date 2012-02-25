package com.edtheloon.gold2economy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

// Class written by Turt2Live: mc.turt2live.com/plugins
public class VaultSupport {

	private boolean vault = false;
	//private boolean register = false;
	//private boolean both = false; //Used by the API only (for now?)
	private gold2economy plugin = null;
	private String method = "Unknown";

	private Economy economy = null;
	private Permission permissions = null;

	public VaultSupport(gold2economy p){
		plugin = p;
	}

	public double balance(String player){
		return economy.getBalance(player);
	}

	public void deposit(String player, double amount){
		// System.out.println(player + " | " + amount); //DEBUG (turt2live)
		economy.depositPlayer(player, amount);
	}

	public String format(double amount){
		return economy.format(amount);
	}

	public String getPermissions(){
		return permissions.getName();
	}

	public gold2economy getPlugin(){
		return plugin;
	}

	//	public boolean hasBoth(){
	//		return both;
	//	}

	public boolean hasPermission(CommandSender player, String permission){
		if(Bukkit.getPlayer(player.getName()) != null){
			return hasPermission(Bukkit.getPlayer(player.getName()), permission);
		}
		return permissions.has(player, permission);
	}

	public boolean hasPermission(Player player, String permission){
		return permissions.has(player.getLocation().getWorld(), player.getName(), permission);
	}

	//	public boolean hasRegister(){
	//		return register;
	//	}

	public boolean init(){
		RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if(economyProvider != null){
			economy = economyProvider.getProvider();
			vault = true;
			method = economy.getName();
		}else{
			vault = false;
			economy = null;
		}
		// System.out.println(economy); //DEBUG (turt2live)
		RegisteredServiceProvider<Permission> permissionsProvider = plugin.getServer().getServicesManager().getRegistration(Permission.class);
		if(permissionsProvider != null && vault){ // We only want to use perms if the economy check passed
			permissions = permissionsProvider.getProvider();
		}else{
			vault = false;
		}
		// System.out.println(permissions); //DEBUG (turt2live)
		return vault;
	}

	public boolean isActive(){
		return vault;
	}

	public String method(){
		return method;
	}

	//	public void setBoth(boolean b){
	//		both = b;
	//	}
	//
	//	public void setBoth(Plugin vault, Plugin register){
	//		both = ((vault != null && register != null) ? true : false);
	//	}
	//
	//	public void setRegister(boolean b){
	//		register = b;
	//	}

	public void setUsed(boolean b){
		vault = b;
	}

	public void withdraw(String player, double amount){
		economy.withdrawPlayer(player, amount);
	}
}
