package com.edtheloon.gold2economy;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import com.nijikokun.bukkit.Permissions.Permissions;
import com.nijikokun.register.payment.Methods;

// import cosine.boseconomy.BOSEconomy;

public class server implements Listener { //Fixed for new event system - Turt2Live
	private gold2economy plugin;

	public server(gold2economy plugin){
		this.plugin = plugin;
	}

	//Method added by Turt2Live
	public void init(){
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler (priority = EventPriority.MONITOR)
	//Fixed for new event system - Turt2Live
	public void onPluginDisable(PluginDisableEvent event){
		// There's nothing here!
		// If register was disabled then 'disable' our plugin.
		if(event.getPlugin().getDescription().getName() == "Register"){
			gold2economy.enabled = false;
			gold2economy.log.info("[Gold2Economy] Disabled. G2E will re-enable once Register/Vault is enabled."); // Added a /Vault to message (turt2live)
		}
		// Repeat for Vault (turt2live)
		if(event.getPlugin().getDescription().getName() == "Vault"){
			gold2economy.enabled = false;
			gold2economy.log.info("[Gold2Economy] Disabled. G2E will re-enable once Register/Vault is enabled.");
		}
	}

	@EventHandler (priority = EventPriority.MONITOR)
	//Fixed for new event system - Turt2Live
	public void onPluginEnable(PluginEnableEvent event){

		// if using Permissions
		if(plugin.config.usePermissions){
			if(gold2economy.permissionHandler == null){
				Plugin PermissionsPlugin = plugin.getServer().getPluginManager().getPlugin("Permissions");
				gold2economy.permissionsEnabled = true;
				gold2economy.permissionHandler = ((Permissions) PermissionsPlugin).getHandler();
				gold2economy.log.info("[Gold2Economy] Hooked into " + PermissionsPlugin.getDescription().getName());
			}
		}

		// If plugin is disabled and enabled plugin is Register
		if(gold2economy.enabled == false && event.getPlugin().getDescription().getName() == "Register")
		{
			Methods.setPreferred(plugin.config.preferred); // Fixed for argument change (turt2live)
			gold2economy.enabled = true;
			gold2economy.log.info("[Gold2Economy] Register was enabled. G2E now functional.");
		}
		// Repeat for Vault (turt2live)
		if(gold2economy.enabled == false && event.getPlugin().getDescription().getName() == "Vault")
		{
			Methods.setPreferred(plugin.config.preferred); // Fixed for argument change (turt2live)
			gold2economy.enabled = true;
			gold2economy.log.info("[Gold2Economy] Vault was enabled. G2E now functional.");
		}
	}

}
