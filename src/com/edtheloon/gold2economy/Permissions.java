package com.edtheloon.gold2economy;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.feildmaster.lib.configuration.EnhancedConfiguration;

public class Permissions {

	public static boolean check(CommandSender sender, String perm, EnhancedConfiguration config){ // Added argument: configHandler (turt2live)
		// Create Variables (turt2live)
		boolean usePermissions = config.getBoolean("permissions.Permissions");
		boolean useBukkitPerms = config.getBoolean("permissions.PermissionsBukkit");

		// If using permissions is enabled
		if(usePermissions){

			// If using PermissionsBukkit (aka SuperPerms)
			if(useBukkitPerms){
				if(sender.hasPermission(perm)){
					return true;
				}else if(sender.isOp())
				{
					return true; // If player doesn't have permission node but is op return true
				}
				return false; // just in case no permissions plugin is enabled.
			}

			// If using Permissions by Nijikokun
			else if(usePermissions){
				if(gold2economy.permissionHandler.has((Player) sender, perm) && gold2economy.permissionsEnabled){
					return true;
				}else if(sender.isOp())
				{
					return true; // If player doesn't have permission node but is op return true
				}
				return false; // return false if permissions plugin isn't enabled.
			}

		}else // If user is OP return true
		if(sender.isOp()){
			return true;
		}else{
			return false;
		}

		// TODO: Check PEX specifically (turt2live)

		// Now check Vault (turt2live)
		if(gold2economy.vault.isActive()){
			Player player = Bukkit.getPlayer(sender.getName());
			if(player != null){
				return gold2economy.vault.hasPermission(player, perm);
			}else{
				return gold2economy.vault.hasPermission(sender, perm);
			}
		}

		return false; // Ultimately return false if all checks fail
	}

}