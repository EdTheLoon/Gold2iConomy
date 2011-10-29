package com.edtheloon.gold2economy;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;
//import com.iConomy.iConomy;
import com.nijikokun.bukkit.Permissions.Permissions;
//import cosine.boseconomy.BOSEconomy;

public class server extends ServerListener {
	private gold2economy plugin;

	public server(gold2economy plugin) {
		this.plugin = plugin;
	}

	public void onPluginDisable(PluginDisableEvent event) {
		// There's nothing here!
		// If register was disabled then 'disable' our plugin.
		if(event.getPlugin().getDescription().getName() == "Register") {
			gold2economy.enabled = false;
			gold2economy.log.info("[Gold2Economy] Disabled. G2E will re-enable once Register is enabled.");
		}
	}

	public void onPluginEnable(PluginEnableEvent event) {
		
		// if using Permissions
		if (gold2economy.config.usePermissions) {
			if (gold2economy.permissionHandler == null) {
				Plugin PermissionsPlugin = plugin.getServer().getPluginManager().getPlugin("Permissions");
				gold2economy.permissionsEnabled = true;
				gold2economy.permissionHandler = ((Permissions) PermissionsPlugin).getHandler();
				gold2economy.log.info("[Gold2Economy] Hooked into " + PermissionsPlugin.getDescription().getName());
			}
		}
		
		// If plugin is disabled and enabled plugin is Register
		if (gold2economy.enabled == false && event.getPlugin().getDescription().getName() == "Register")
		{
			gold2economy.enabled = true;
			gold2economy.log.info("[Gold2Economy] Register was enabled. G2E now functional.");
		}
	}

}
