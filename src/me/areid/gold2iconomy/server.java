package me.areid.gold2iconomy;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

import com.iConomy.iConomy;
import com.nijiko.permissions.PermissionHandler;

import cosine.boseconomy.BOSEconomy;

public class server extends ServerListener {
	private gold2iconomy plugin;

	public server(gold2iconomy plugin) {
		this.plugin = plugin;
	}

	public void onPluginDisable(PluginDisableEvent event) {
		// if using iConomy
		if (plugin.config.iConomy) {
			if (plugin.iConomyPlugin != null) {
				if (event.getPlugin().getDescription().getName().equals("iConomy")) {
					plugin.iConomyPlugin = null;
					gold2iconomy.enabled = false;
					gold2iconomy.log.info("[Gold2iConomy] Un-hooked from iConomy. Gold2iConomy will disable until iConomy is enabled.");
				}
			}
		}

		// if using BOSEconomy
		if (plugin.config.BOSEconomy) {
			if (plugin.BOSEconomyPlugin != null) {
				if (event.getPlugin().getDescription().getName().equals("BOSEconomy")) {
					plugin.BOSEconomyPlugin = null;
					gold2iconomy.enabled = false;
					gold2iconomy.log.info("[Gold2iConomy] Un-hooked from BOSEconomy. Gold2iConomy will disable until BOSEconomy is enabled.");
				}
			}
		}
		
		// Permissions
		if (plugin.config.usePermissions) {
			if (gold2iconomy.permissionHandler != null) {
				if (event.getPlugin().getDescription().getName().equals("Permissions")) {
					gold2iconomy.permissionHandler = null;
					gold2iconomy.permissionsEnabled = false;
					gold2iconomy.log.info("[Gold2iConomy] Permissions disabled. Falling back to OP only for /gi reload");
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
						gold2iconomy.enabled = true;
						gold2iconomy.log.info("[Gold2iConomy] Hooked into iConomy.");
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
						gold2iconomy.enabled = true;
						gold2iconomy.log.info("[Gold2iConomy] Hooked into BOSEconomy");
					}
				}
			}
		}
		
		if (plugin.config.usePermissions) {
			if (gold2iconomy.permissionHandler == null) {
				Plugin Permissions = plugin.getServer().getPluginManager().getPlugin("Permissions");
				gold2iconomy.permissionsEnabled = true;
				gold2iconomy.permissionHandler = (PermissionHandler) Permissions;
			}
		}
	}

}
