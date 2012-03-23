package net.yeticraft.squatingyeti.GetInfo;

import java.util.logging.Logger;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class GetInfo extends JavaPlugin {
	
	Logger log = Logger.getLogger("Minecraft");
	private static Permission permissions = null;
	private static boolean usingVault = false;
	private static boolean usingPermissions = false;

	public boolean onCommand(CommandSender sender, Command command, String label, Entity[] args){
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command only works in-game");
			return true;
		}
		
		Player player = (Player) sender;
		Entity oPlayer = args[0];
		String targetName = ((Player) oPlayer).getName();
		if (args.length == 0 ) {
			sender.sendMessage(ChatColor.YELLOW + "You must specify a player");
			return true;
		}
		if (args.length >= 2) {
			sender.sendMessage(ChatColor.YELLOW + "Too many arguments. Try /GetInfo [name]");
			return true;
		}
		if (label.equalsIgnoreCase("getinfo")) {
			player.sendMessage("We are working to here " + oPlayer);
			return true;
		}
		else {
			player.sendMessage("Something went wrong");
		}
		return false;
	}
	
	
	public void onDisable() {
		log.info("GetInfo has been disabled");
	}
	
	public void onEnable() {
		log.info("GetInfo has been enabled");
		initializeVault();
	}
	public static Permission getPermissions() {
		return permissions;
	}
	
	public static boolean isUsingVault() {
		return usingVault;
	}
	
	public static boolean isUsingPermissions() {
		return usingPermissions;
	}
	
	private boolean setupPermissions() {
	if (usingVault) {
		RegisteredServiceProvider<Permission> permissionsProvider = getServer().getServicesManager().getRegistration(
				net.milkbowl.vault.permission.Permission.class);
		if (permissionsProvider != null) {
			permissions = permissionsProvider.getProvider();
		}
		usingPermissions = true;
		return (permissions != null);
	}
	usingPermissions = false;
	return false;
	}
	
	private void initializeVault() {
		Plugin x = this.getServer().getPluginManager().getPlugin("Vault");
		if (x != null && x instanceof Vault) {
			log.info("[GetInfo] found [Vault] and searching for permissions plugin.");
			usingVault = true;
			if (setupPermissions()) {
				log.info("[GetInfo] found [" + permissions.getName() + "] plugin. Permissions enabled.");
			}
				else if (!setupPermissions()) {
					log.severe("[GetInfo] permissions plugin not found.");
					usingPermissions = false;
				}
		}else {
			log.severe("[GetInfo] could not find [Vault]!");
			usingVault = false;
		}
	}
}