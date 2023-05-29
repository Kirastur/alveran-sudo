package de.polarwolf.alveran.integration.luckperms;

import static net.luckperms.api.model.data.DataMutateResult.FAIL_ALREADY_HAS;

import java.time.Duration;
import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.polarwolf.alveran.cornucopia.Blessing;
import de.polarwolf.alveran.exception.AlveranException;
import de.polarwolf.alveran.notifications.NotificationManager;
import de.polarwolf.alveran.text.Message;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;

public class LuckPermsHook {

	protected LuckPermsListener luckPermsListener = null;

	public LuckPermsHook(Plugin plugin, NotificationManager notificationManager) {
		luckPermsListener = new LuckPermsListener(plugin, notificationManager);
		luckPermsListener.enableListener();
	}

	public void disable() {
		if (luckPermsListener != null) {
			luckPermsListener.disableListener();
			luckPermsListener = null;
		}
	}

	public boolean isDisabled() {
		return luckPermsListener == null;
	}

	public void validateGroup(String groupName) throws AlveranException {
		LuckPerms lpAPI = LuckPermsProvider.get();
		Group destinationGroup = lpAPI.getGroupManager().getGroup(groupName);
		if (destinationGroup == null) {
			throw new AlveranException(null, Message.LUCKPERMS_GROUP_NOT_FOUND, groupName);
		}
	}

	protected boolean isUserBlessed(User user, String blessedGroupName) {
		Collection<Group> inheritedGroups = user.getInheritedGroups(user.getQueryOptions());
		for (Group myGroup : inheritedGroups) {
			if (myGroup.getName().equalsIgnoreCase(blessedGroupName)) {
				return true;
			}
		}
		return false;
	}

	public boolean isPlayerBlessed(Player player, String blessedGroupName) {
		LuckPerms lpAPI = LuckPermsProvider.get();
		User user = lpAPI.getPlayerAdapter(Player.class).getUser(player);
		return isUserBlessed(user, blessedGroupName);
	}

	public boolean blessSinglePlayer(Blessing blessing) throws AlveranException {
		Player player = blessing.player();
		String destinationGroupName = blessing.destinationGroupName();
		int blessingDuration = blessing.blessingDuration();

		// Get the LuckPerm user-object from player
		LuckPerms lpAPI = LuckPermsProvider.get();
		User user = lpAPI.getPlayerAdapter(Player.class).getUser(player);

		// Check if player already has group membership
		Boolean isAlreadyBlessed = isUserBlessed(user, destinationGroupName);

		// OK, we have checked all, now let's execute blessing
		InheritanceNode node = InheritanceNode.builder(destinationGroupName).expiry(Duration.ofHours(blessingDuration))
				.build();
		DataMutateResult result = user.data().add(node);
		if (!result.wasSuccessful() && !result.equals(FAIL_ALREADY_HAS)) {
			throw new AlveranException(player.getName(), Message.LUCKPERM_BLESS_ERROR, result.toString());
		}

		// Save new settings to user
		lpAPI.getUserManager().saveUser(user);

		// Return the previous blessed state to the caller
		return !isAlreadyBlessed;
	}

	public boolean unblessSinglePlayer(Blessing blessing) throws AlveranException {
		Player player = blessing.player();
		String destinationGroupName = blessing.destinationGroupName();
		int blessingDuration = blessing.blessingDuration();

		// get the LuckPerm user-object from player
		LuckPerms lpAPI = LuckPermsProvider.get();
		User user = lpAPI.getPlayerAdapter(Player.class).getUser(player);

		// Check if user has the blessing
		if (!isUserBlessed(user, destinationGroupName)) {
			return false;
		}

		// OK, we have checked all, now let's remove blessing
		InheritanceNode node = InheritanceNode.builder(destinationGroupName).expiry(Duration.ofHours(blessingDuration))
				.build();
		DataMutateResult result = user.data().remove(node);
		if (!result.wasSuccessful()) {
			throw new AlveranException(player.getName(), Message.LUCKPERM_UNBLESS_ERROR, result.toString());
		}

		// Save new settings to user
		lpAPI.getUserManager().saveUser(user);

		// Give success state to the caller
		return true;
	}

}
