package de.polarwolf.alveran.api;

import java.time.Duration;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.polarwolf.alveran.main.Main;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;

public class AlveranAPI {

	private final Main main;

    public AlveranAPI(Main main) {
    	this.main = main;
    }

    public boolean PerformBlessing() {
    	
		if (main.getAlveranConfig().getDebug()) {
			main.getLogger().info("Debug: Perform blessing");
		}
		
    	// Get values from config
    	String strWorld = main.getAlveranConfig().getWorld();
		String strRegion = main.getAlveranConfig().getRegion();
		String strPermission = main.getAlveranConfig().getPermission();
		String strGroup = main.getAlveranConfig().getDestinationGroup();
		Integer intDuration = main.getAlveranConfig().getDuration();

		// Get minecraft world 
		World world = main.getServer().getWorld(strWorld);
		if(world==null) {
			main.getLogger().warning("World \""+strWorld+"\" not found.");
			return false;
		}
			
		// Get WorldGuard region
		ProtectedRegion region = WorldGuard
				 				.getInstance()
				 				.getPlatform()
				 				.getRegionContainer()
				 				.get(BukkitAdapter.adapt(world))
				 				.getRegion (strRegion);
		if(region==null) {
			main.getLogger().warning("WorldGuard Region \""+strRegion+"\" not found.");
			return false;
		}
		
		// Get LuckPerms Provider
		LuckPerms lpapi = LuckPermsProvider.get();
		if(lpapi==null) {
			main.getLogger().warning("LuckPerms provider not found.");
			return false;
		}

		// Get Destination Group
		Group destinationGroup = lpapi.getGroupManager().getGroup(strGroup);
		if(destinationGroup==null) {
			main.getLogger().warning("LuckPerms group \""+strGroup+"\" not found.");
			return false;
		}
		
		// Loop for all players
		BlockVector3 bloc = null;
		Location location = null;
		User user = null;
		InheritanceNode node = null;
		Collection<Group> inheritedGroups  = null;
		Boolean IsAlreadyBlessed = false;
		DataMutateResult result = null;
		
		if (main.getAlveranConfig().getDebug()) {
			main.getLogger().info("Debug: Checking all players");
		}

		for(Player player : world.getPlayers()) {
			IsAlreadyBlessed = false;

			if (main.getAlveranConfig().getDebug()) {
				main.getLogger().info("Debug: Checking player "+player.getName());
			}

			// Check if player is inside Region
			location = player.getLocation();
			bloc = BlockVector3.at(location.getX(), location.getY(), location.getZ());
			if (!region.contains (bloc)) {
				if (main.getAlveranConfig().getDebug()) {
					main.getLogger().info("Debug: Player is not in region");
				}
				continue;
			}
			
			// Check if player has acolyte permission
			if (!player.hasPermission(strPermission)) {
				if (main.getAlveranConfig().getDebug()) {
					main.getLogger().info("Debug: Player doesn't have the permission");
				}
				continue;
			}
			
			// OK, the player is ready to receive blessing
			// first get the LuckPerms user object
			user = lpapi.getPlayerAdapter(Player.class).getUser(player);

			// Check if player already has group membership
			inheritedGroups = user.getInheritedGroups(user.getQueryOptions());
			if (inheritedGroups.stream().anyMatch(g ->g.getName().equals(strGroup))) {				
				IsAlreadyBlessed = true;
			}
			
			// OK, we have checked all, now let's execute blessing
			if (main.getAlveranConfig().getDebug()) {
				main.getLogger().info("Debug: Player will get blessing");
			}
			node = InheritanceNode.builder(strGroup).expiry(Duration.ofHours(intDuration)).build();
			result = user.data().add(node); 
			if (!(result.wasSuccessful()|result.toString().equals("FAIL_ALREADY_HAS"))) {
				main.getLogger().warning("Something went wrong while blessing "+player.getName());
				main.getLogger().warning("LuckPerms says: "+result.toString());
				return false;
			}
			lpapi.getUserManager().saveUser(user);
			
			// Print the result to the player and console
			if (IsAlreadyBlessed) {
				if (main.getAlveranConfig().getDebug()) {
					main.getLogger().info("Debug: Player has been blessed again by Alveran for "+intDuration+" hours");
				}
			} else {
				if (main.getAlveranConfig().getDebug()) {
					main.getLogger().info("Debug: Player has been blessed by Alveran for "+intDuration+" hours");
				}
			}
		}
		if (main.getAlveranConfig().getDebug()) {
			main.getLogger().info("Debug: Blessing finished");
		}
		return true;
    }

	public boolean UnblessPlayer(Player player) {

		if (main.getAlveranConfig().getDebug()) {
			main.getLogger().info("Debug: Perform unblessing");
		}
    	// Get values from config
		String strGroup = main.getAlveranConfig().getDestinationGroup();
		Integer intDuration = main.getAlveranConfig().getDuration();

		// Get LuckPerms Provider
		LuckPerms lpapi = LuckPermsProvider.get();
		if(lpapi==null) {
			main.getLogger().warning("LuckPerms provider not found.");
			return false;
		}

		// Get Destination Group
		Group destinationGroup = lpapi.getGroupManager().getGroup(strGroup);
		if(destinationGroup==null) {
			main.getLogger().warning("LuckPerms group \""+strGroup+"\" not found.");
			return false;
		}
		
		// Get LuckPerms user from player
		User user = lpapi.getPlayerAdapter(Player.class).getUser(player);
		if(user==null) {
			main.getLogger().warning("LuckPerms user \""+player.getName()+"\" not found.");
			return false;
		}

		// Check if player has group membership
		Collection<Group> inheritedGroups = user.getInheritedGroups(user.getQueryOptions());
		if (!inheritedGroups.stream().anyMatch(g ->g.getName().equals(strGroup))) {
			// Player is not in group alveran ==> nothing to do
			if (main.getAlveranConfig().getDebug()) {
				main.getLogger().info("Debug: Player "+player.getName()+" dosn't have the blessing - nothing to do");
			}
			return true;
		}
		
		// OK, we have checked all, now let's remove the blessing
		InheritanceNode node = InheritanceNode.builder(strGroup).expiry(Duration.ofHours(intDuration)).build();
		DataMutateResult result = user.data().remove(node);
		if (!result.wasSuccessful()) {
			main.getLogger().warning("Something went wrong while remove blessing from "+player.getName());
			main.getLogger().warning("LuckPerms says: "+result.toString());
			return false;
		}
		lpapi.getUserManager().saveUser(user);

		// Print the result to the player and console
		if (main.getAlveranConfig().getDebug()) {
			main.getLogger().info("Debug: Player "+player.getName()+" has lost his blessing");
		}
		return true;
	}
}

