package com.edtheloon.gold2economy;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

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
	public void onPluginDisable(PluginDisableEvent event){
		// Check Vault (turt2live)
		if(event.getPlugin().getDescription().getName() == "Vault"){
			gold2economy.enabled = false;
			gold2economy.log.info("[Gold2Economy] Disabled. G2E will re-enable once Vault is enabled.");
		}
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onPluginEnable(PluginEnableEvent event){
		// Check Vault (turt2live)
		if(gold2economy.enabled == false && event.getPlugin().getDescription().getName() == "Vault"){
			gold2economy.enabled = true;
			gold2economy.log.info("[Gold2Economy] Vault was enabled. G2E now functional.");
		}
	}

}
