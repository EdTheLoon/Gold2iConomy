package com.edtheloon.gold2economy;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Permissions {

	public static boolean check(CommandSender sender, String perm, configHandler config){ // Added argument: configHandler (turt2live)

		// If using permissions is enabled
		if(config.usePermissions){

			// If using PermissionsBukkit (aka SuperPerms)
			if(config.useBukkitPerms){
				if(sender.hasPermission(perm)){
					return true;
				}else if(sender.isOp())
				{
					return true; // If player doesn't have permission node but is op return true
				}
				return false; // just in case no permissions plugin is enabled.
			}

			// If using Permissions by Nijikokun
			else if(config.usePermissions){
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
		return false; // Ultimately return false if all checks fail
	}

}