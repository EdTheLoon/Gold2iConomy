package me.areid.gold2iconomy;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

import com.iConomy.iConomy;

public class server extends ServerListener {
	private gold2iconomy plugin;
	
	public server(gold2iconomy plugin) {
		this.plugin = plugin;
	}
	
	public void onPluginDisable(PluginDisableEvent event) {
		if (plugin.iConomyPlugin != null) {
			if (event.getPlugin().getDescription().getName().equals("iConomy")) {
				plugin.iConomyPlugin = null;
				System.out.println("[Gold2iConomy] Un-hooked from iConomy. Gold2iConomy will disable until iConomy is enabled.");
				gold2iconomy.pm.disablePlugin(plugin);
			}
		}
	}
	
	public void onPluginEnable(PluginEnableEvent event) {
		if (plugin.iConomyPlugin == null) {
			Plugin iConomy = plugin.getServer().getPluginManager().getPlugin("iConomy");
			
			if (iConomy != null) {
				if (iConomy.isEnabled() && iConomy.getClass().getName().equals("com.iConomy.iConomy")) {
					plugin.iConomyPlugin = (iConomy)iConomy;
					System.out.println("[Gold2iConomy] Hooked into iConomy.");
				}
				if (!(plugin.pm.isPluginEnabled("Gold2iConomy"))) {
					plugin.pm.enablePlugin(plugin);
				}
			}
		}
	}

}
