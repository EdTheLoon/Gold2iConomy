package com.edtheloon.gold2economy;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;
import com.iConomy.iConomy;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import cosine.boseconomy.BOSEconomy;

public class server extends ServerListener {
	private gold2economy plugin;

	public server(gold2economy plugin) {
		this.plugin = plugin;
	}

	public void onPluginDisable(PluginDisableEvent event) {
		// if using iConomy
		if (plugin.config.iConomy) {
			if (plugin.iConomyPlugin != null) {
				if (event.getPlugin().getDescription().getName().equals("iConomy")) {
					plugin.iConomyPlugin = null;
					gold2economy.enabled = false;
					gold2economy.log.info("[Gold2Economy] Un-hooked from iConomy. Gold2Economy will disable until iConomy is enabled.");
				}
			}
		}

		// if using BOSEconomy
		if (plugin.config.BOSEconomy) {
			if (plugin.BOSEconomyPlugin != null) {
				if (event.getPlugin().getDescription().getName().equals("BOSEconomy")) {
					plugin.BOSEconomyPlugin = null;
					gold2economy.enabled = false;
					gold2economy.log.info("[Gold2Economy] Un-hooked from BOSEconomy. Gold2Economy will disable until BOSEconomy is enabled.");
				}
			}
		}
		
		// Permissions
		if (plugin.config.usePermissions) {
			if (gold2economy.permissionHandler != null) {
				if (event.getPlugin().getDescription().getName().equals("Permissions")) {
					gold2economy.permissionHandler = null;
					gold2economy.permissionsEnabled = false;
					gold2economy.log.info("[Gold2Economy] Permissions disabled. Falling back to OP only for /gi reload");
				}
			}
		}
	}

	public void onPluginEnable(PluginEnableEvent event) {
		// if using iConomy
		if (plugin.config.iConomy) {
			if (plugin.iConomyPlugin == null) {
				Plugin iConomy = plugin.getServer().getPluginManager().getPlugin("iConomy");

				if (iConomy != null) {
					if (iConomy.isEnabled() && iConomy.getClass().getName().equals("com.iConomy.iConomy")) {
						plugin.iConomyPlugin = (iConomy)iConomy;
						gold2economy.enabled = true;
						gold2economy.log.info("[Gold2Economy] Hooked into iConomy.");
					}
				}
			}
		}

		// if using BOSEconomy
		if (plugin.config.BOSEconomy) {
			if (plugin.BOSEconomyPlugin == null) {
				Plugin BOSEconomy = plugin.getServer().getPluginManager().getPlugin("BOSEconomy");

				if (BOSEconomy != null) {
					if (BOSEconomy.isEnabled() && BOSEconomy.getClass().getName().equals("cosine.boseconomy.BOSEconomy")) {
						plugin.BOSEconomyPlugin = (BOSEconomy) BOSEconomy;
						gold2economy.enabled = true;
						gold2economy.log.info("[Gold2Economy] Hooked into BOSEconomy");
					}
				}
			}
		}
		
		// if using Permissions
		if (plugin.config.usePermissions) {
			if (gold2economy.permissionHandler == null) {
				Plugin PermissionsPlugin = plugin.getServer().getPluginManager().getPlugin("Permissions");
				gold2economy.permissionsEnabled = true;
				gold2economy.permissionHandler = ((Permissions) PermissionsPlugin).getHandler();
			}
		}
	}

}
