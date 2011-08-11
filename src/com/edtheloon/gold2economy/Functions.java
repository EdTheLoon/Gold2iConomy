package com.edtheloon.gold2economy;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Functions {

	// Reload configuration
	public static boolean giReload(CommandSender sender) {
		gold2economy.config.loadConfig();
		sender.sendMessage(ChatColor.GREEN + "[Gold2Economy] " + ChatColor.WHITE + "Reloaded. Rate is " + gold2economy.config.cRate.toString());
		return true;
	}
}
