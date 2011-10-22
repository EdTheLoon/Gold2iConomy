package com.edtheloon.gold2economy;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;
//import com.iConomy.iConomy;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Methods;
//import cosine.boseconomy.BOSEconomy;

public class server extends ServerListener {
	private gold2economy plugin;

	public server(gold2economy plugin) {
		this.plugin = plugin;
	}

	public void onPluginDisable(PluginDisableEvent event) {
		
		if (!Methods.hasMethod()) {
			gold2economy.enabled = false;
			gold2economy.log.info("[Gold2Economy] Gold2Economy is disabled until a supported economy is found");
		}
		
		/*// if using iConomy
		if (gold2economy.config.iConomy) {
			if (gold2economy.iConomyPlugin != null) {
				if (event.getPlugin().getDescription().getName().equals("iConomy")) {
					gold2economy.iConomyPlugin = null;
					gold2economy.enabled = false;
					gold2economy.log.info("[Gold2Economy] Un-hooked from iConomy. Gold2Economy will disable until iConomy is enabled.");
				}
			}
		}

		// if using BOSEconomy
		if (gold2economy.config.BOSEconomy) {
			if (gold2economy.BOSEconomyPlugin != null) {
				if (event.getPlugin().getDescription().getName().equals("BOSEconomy")) {
					gold2economy.BOSEconomyPlugin = null;
					gold2economy.enabled = false;
					gold2economy.log.info("[Gold2Economy] Un-hooked from BOSEconomy. Gold2Economy will disable until BOSEconomy is enabled.");
				}
			}
		}
		
		// Permissions
		if (gold2economy.config.usePermissions) {
			if (gold2economy.permissionHandler != null) {
				if (event.getPlugin().getDescription().getName().equals("Permissions")) {
					gold2economy.permissionHandler = null;
					gold2economy.permissionsEnabled = false;
					gold2economy.log.info("[Gold2Economy] Permissions disabled. Falling back to OP only for /gi reload");
				}
			}
		}*/
	}

	public void onPluginEnable(PluginEnableEvent event) {
		
		if (Methods.hasMethod()) {
			Method method = Methods.getMethod();
			gold2economy.enabled = true;
			gold2economy.log.info("[Gold2Economy] Register is using " + method.getName() + " Version " + method.getVersion());
		}
		
		/*// if using iConomy
		if (gold2economy.config.iConomy) {
			if (gold2economy.iConomyPlugin == null) {
				Plugin iConomy = plugin.getServer().getPluginManager().getPlugin("iConomy");

				if (iConomy != null) {
					if (iConomy.isEnabled() && iConomy.getClass().getName().equals("com.iConomy.iConomy")) {
						gold2economy.iConomyPlugin = (iConomy)iConomy;
						gold2economy.enabled = true;
						gold2economy.log.info("[Gold2Economy] Hooked into " + gold2economy.iConomyPlugin.getDescription().getName());
					}
				}
			}
		}

		// if using BOSEconomy
		if (gold2economy.config.BOSEconomy) {
			if (gold2economy.BOSEconomyPlugin == null) {
				Plugin BOSEconomy = plugin.getServer().getPluginManager().getPlugin("BOSEconomy");

				if (BOSEconomy != null) {
					if (BOSEconomy.isEnabled() && BOSEconomy.getClass().getName().equals("cosine.boseconomy.BOSEconomy")) {
						gold2economy.BOSEconomyPlugin = (BOSEconomy) BOSEconomy;
						gold2economy.enabled = true;
						gold2economy.log.info("[Gold2Economy] Hooked into " + gold2economy.BOSEconomyPlugin.getDescription().getName());
					}
				}
			}
		}*/
		
		// if using Permissions
		if (gold2economy.config.usePermissions) {
			if (gold2economy.permissionHandler == null) {
				Plugin PermissionsPlugin = plugin.getServer().getPluginManager().getPlugin("Permissions");
				gold2economy.permissionsEnabled = true;
				gold2economy.permissionHandler = ((Permissions) PermissionsPlugin).getHandler();
				gold2economy.log.info("[Gold2Economy] Hooked into " + PermissionsPlugin.getDescription().getName());
			}
		}
	}

}
