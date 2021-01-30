package de.polarwolf.alveran.api;

import java.time.Duration;
import java.util.Collection;
import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.polarwolf.alveran.config.AlveranConfig;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;

import static net.luckperms.api.model.data.DataMutateResult.*;

public class AlveranAPI {

	protected final Plugin plugin;
	protected final AlveranConfig alveranConfig;

    public AlveranAPI(Plugin plugin, AlveranConfig alveranConfig) {
    	this.plugin=plugin;
    	this.alveranConfig=alveranConfig;
    }
    
    protected boolean isDebug() {
    	return alveranConfig.getDebug();
    }
    
    protected void printWarning(String warningText) {
		plugin.getLogger().warning(warningText);
    }
    
    protected void printInfo(String infoText) {
    	plugin.getLogger().info(infoText);
    }
    
    protected void printDebug (String debugText) {
    	if (isDebug()) {
    		printInfo("Debug: "+debugText);
    	}
    }
    
    protected void printNotFound (String errorText, String errorParameter) {
		printWarning(errorText+" \""+errorParameter+"\" not found.");    	
    }
    
    protected boolean groupExists (String groupName) {
    	LuckPerms lpAPI = LuckPermsProvider.get();
    	Group destinationGroup = lpAPI.getGroupManager().getGroup(groupName);
    	if(destinationGroup==null) {
    		printNotFound("LuckPerms group", groupName);
    		return false;
    	}
    	return true;
	}

    protected boolean isPlayerAuthorized(Player player, ProtectedRegion region, String neededPermissionName) {

    	// Check if player is inside Region
    	Location location = player.getLocation();
    	BlockVector3 bloc = BlockVector3.at(location.getX(), location.getY(), location.getZ());
    	if (!region.contains (bloc)) {
   			printDebug("Player is not in region");
    		return false;
    	}
	
    	// Check if player has acolyte permission
    	if (!player.hasPermission(neededPermissionName)) {
   			printDebug("Player doesn't have the permission");
    		return false;
    	}
    	return true;
    }
    
    protected boolean isUserBlessed(User user, String blessedGroupName) { 
    	Collection<Group> inheritedGroups = user.getInheritedGroups(user.getQueryOptions());
    	return inheritedGroups.stream().anyMatch(g ->g.getName().equals(blessedGroupName));
	}
    
    protected boolean blessSinglePlayer(Player player, String destinationGroupName, int blessingDuration) {
    	
    	// get the LuckPerm user-object from player
		LuckPerms lpAPI = LuckPermsProvider.get();
    	User user = lpAPI.getPlayerAdapter(Player.class).getUser(player);

		// Check if player already has group membership
		Boolean isAlreadyBlessed = isUserBlessed (user, destinationGroupName); 
		
		// OK, we have checked all, now let's execute blessing
		printDebug("Player will get blessing");
    	InheritanceNode node = InheritanceNode.builder(destinationGroupName).expiry(Duration.ofHours(blessingDuration)).build();
    	DataMutateResult result = user.data().add(node); 
		if (!(result.wasSuccessful()||result.equals(FAIL_ALREADY_HAS))) {
			printWarning("Something went wrong while blessing "+player.getName());
			printWarning("LuckPerms says: "+result.toString());
			return false;
		}
		
		// Save new settings to user
		lpAPI.getUserManager().saveUser(user);
		
		// Print the result to the player and console
		if (isAlreadyBlessed) {
			printDebug("Player has been blessed again by Alveran for "+blessingDuration+" hours");
		} else {
			printDebug("Player has been blessed by Alveran for "+blessingDuration+" hours");
		}
		return true;
	}

    protected boolean unblessSinglePlayer(Player player, String destinationGroupName, int blessingDuration) {

    	// get the LuckPerm user-object from player
		LuckPerms lpAPI = LuckPermsProvider.get();
    	User user = lpAPI.getPlayerAdapter(Player.class).getUser(player);

		if (!isUserBlessed(user, destinationGroupName)) {
			// Player is not in group alveran ==> nothing to do
			printDebug("Player "+player.getName()+" dosn't have the blessing - nothing to do");
			return true;
		}

		// OK, we have checked all, now let's remove blessing
    	InheritanceNode node = InheritanceNode.builder(destinationGroupName).expiry(Duration.ofHours(blessingDuration)).build();
		DataMutateResult result = user.data().remove(node);
		if (!result.wasSuccessful()) {
			printWarning("Something went wrong while remove blessing from "+player.getName());
			printWarning("LuckPerms says: "+result.toString());
			return false;
		}

		// Save new settings to user
		lpAPI.getUserManager().saveUser(user);

		// Print the result to the player and console
		printDebug("Player "+player.getName()+" has lost his blessing");
		return true;
	}
    	
    public boolean performBlessing(@Nonnull World world) {
    	
		printDebug("Perform blessing");
		
    	// Get values from config
		String regionName = alveranConfig.getRegion();
		String neededPermissionName = alveranConfig.getPermission();
		String destinationGroupName = alveranConfig.getDestinationGroup();
		Integer blessingDuration = alveranConfig.getDuration();

		// Get WorldGuard region
		ProtectedRegion region = WorldGuard
				 				.getInstance()
				 				.getPlatform()
				 				.getRegionContainer()
				 				.get(BukkitAdapter.adapt(world))
				 				.getRegion (regionName);
		if (region==null) {
			printNotFound("WorldGuard Region", regionName);
			return false;
		}
		
		// Check Destination Group
		if (!groupExists(destinationGroupName)) {
			return false;
		}

		// Loop for all players
		printDebug("Checking all players");
		for (Player player : world.getPlayers()) {

			printDebug("Checking player "+player.getName());
			
			// Check if player is able to get blessed and bless them
			if (isPlayerAuthorized(player, region, neededPermissionName)) {
				blessSinglePlayer(player, destinationGroupName, blessingDuration);
			}
		}
		printDebug("Blessing finished");
		return true;
    }

	public boolean unblessPlayer(Player player) {
		
		printDebug("Perform unblessing");

		// Get values from config
		String destinationGroupName = alveranConfig.getDestinationGroup();
		Integer blessingDuration = alveranConfig.getDuration();
		
		// Check Destination Group
		if (!groupExists(destinationGroupName)) {
			return false;
		}
		
		return unblessSinglePlayer(player, destinationGroupName, blessingDuration);
	}

    public boolean isPlayerBlessed(Player player) { 

    	// Get Group Name
    	String blessedGroupName = alveranConfig.getDestinationGroup();

    	// Get the LuckPerm user-object from player
		LuckPerms lpAPI = LuckPermsProvider.get();
    	User user = lpAPI.getPlayerAdapter(Player.class).getUser(player);

    	Collection<Group> inheritedGroups = user.getInheritedGroups(user.getQueryOptions());
    	return inheritedGroups.stream().anyMatch(g ->g.getName().equals(blessedGroupName));
	}

}

