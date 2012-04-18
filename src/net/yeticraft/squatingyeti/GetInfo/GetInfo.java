package net.yeticraft.squatingyeti.GetInfo;

import java.util.logging.Logger;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.permission.Permission;
import net.yeticraft.squatingyeti.GetZone.GetZone;
import net.yeticraft.squatingyeti.YetiHome.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.text.SimpleDateFormat;

public class GetInfo extends JavaPlugin {

	public HomeManager homes;
	public GetZone zones;
	public Logger log = Logger.getLogger("Minecraft");
	private static Permission permissions = null;
	private static boolean usingVault = false;
	private static boolean usingPermissions = false;

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command only works in-game");
			return true;
		}
		
		Player player = (Player) sender;
		Date firstPlayed;
		Date lastPlayed;
		if (args.length == 0 ) {
			sender.sendMessage(ChatColor.YELLOW + "You must specify a player");
			return true;
		}
		if (args.length >= 2) {
			sender.sendMessage(ChatColor.YELLOW + "Too many arguments. Try /GetInfo [name]");
			return true;
		} 
		if (label.equalsIgnoreCase("getinfo") && args.length == 1) {
			Player target = findOnlinePlayer(args[0]);
			
            if (target != null) {
            	//target = findOnlinePlayer(args[0]);
                Player sendPlayer = (Player) sender;
                log.info("Sender: " +player);
                String owner = target.getName();
                log.info(owner);
                float exp = target.getExp();
                Location loc = target.getLocation();
                log.info("loc: " +loc);
                int x = target.getLocation().getBlockX();
                int y = target.getLocation().getBlockY();
                int z = target.getLocation().getBlockZ();
                int health = target.getHealth();
                short playDays;
                //String zone = zones.currentZone(loc, owner);
                firstPlayed = new Date (target.getFirstPlayed());
				lastPlayed = new Date(target.getLastPlayed());
				playDays = (short)((System.currentTimeMillis() - target.getFirstPlayed()) / 86400000);
					player.sendMessage(ChatColor.GREEN + owner);
					player.sendMessage(ChatColor.YELLOW + "================================");
					player.sendMessage("First Played " +(new SimpleDateFormat()).format(firstPlayed));
					player.sendMessage("Last Played " + (new SimpleDateFormat()).format(lastPlayed));
					player.sendMessage("Location: x:" + x + " y:" + y + " z:" + z);
					player.sendMessage("Zone: " +  zones.getZoneOther(player, target));
					player.sendMessage("Days Played: " + playDays);
					player.sendMessage("Health: " + health);
					player.sendMessage("Experience: " + exp);
					sendPlayer.performCommand("listhomes" + owner);
					return true;
				}
            

		else if (target == null) {
				OfflinePlayer oPlayer = getServer().getOfflinePlayer(args[0]);
			if (!oPlayer.hasPlayedBefore()){
					player.sendMessage(ChatColor.RED + args[0] + " has not played on Yeticraft before");
					return true;
			}		
					String offPlayer = oPlayer.getName();
					firstPlayed = new Date (oPlayer.getFirstPlayed());
					lastPlayed = new Date(oPlayer.getLastPlayed());
					short playDays;
					playDays = (short)((System.currentTimeMillis() - oPlayer.getFirstPlayed()) / 86400000);
					player.sendMessage(ChatColor.GREEN + offPlayer);
					player.sendMessage(ChatColor.YELLOW + "================================");
					player.sendMessage(ChatColor.GREEN + offPlayer + ChatColor.RED + " is not online");
					player.sendMessage("First Played " +(new SimpleDateFormat()).format(firstPlayed));
					player.sendMessage("Last Played " + (new SimpleDateFormat()).format(lastPlayed));
					player.sendMessage("Days Played: " + playDays);
					return true;
			}
		} return false;
	}	




	/*public static getOffline(OfflinePlayer offPlayer, String[] args) {
		offPlayer = Bukkit.getOfflinePlayer(args[0]);
		String offTarget = null;
		offTarget = offPlayer.getName();
		if ()
	} */
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
	
	public Player findOnlinePlayer(String name) {
		if (name != null) {
			Player p = getServer().getPlayer(name);
			if (p != null) return p;
			//try to find by partial name
			Boolean found = false;
			int nameLength = name.length();
			for (Player p1: getServer().getOnlinePlayers()) {
				if (p1.getName().length() > nameLength) {
					if (p1.getName().substring(0, (nameLength - 1)).equalsIgnoreCase(name)) {
						//return null if more than 1 player matches
						if (found) return null;
						found = true;
						p = p1;
					}
				}	
			} return p;
		} return null;
	}
}