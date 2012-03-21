package net.yeticraft.squatingyeti.WhoIs;

import java.util.logging.Logger;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class WhoIs extends JavaPlugin {
	
	Logger log = Logger.getLogger("Minecraft");
	private static Permission permissions = null;
	private static boolean usingVault = false;
	private static boolean usingPermissions = false;
	
	public void onDisable() {
		log.info("WhoIs has been disabled");
	}
	
	public void onEnable() {
		log.info("WhoIs has been enabled");
		initializeVault();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command only works in-game");
			return true;
		}
		
		Player player = (Player) sender;
		String targetName = args[0].toLowerCase();
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
			log.info("[WhoIs] found [Vault] and searching for economy plugin.");
			usingVault = true;
			if (setupPermissions()) {
				log.info("[WhoIs] found [" + permissions.getName() + "] plugin. Prices enabled.");
			}
				else if (!setupPermissions()) {
					log.info("[SEVERE][WhoIs] permissions plugin not found.");
					usingPermissions = false;
				}
		}else {
			log.info("[SEVERE][WhoIs] could not find [Vault]!");
			usingVault = false;
		}
	}
}