package de.polarwolf.alveran.cornucopia;

import org.bukkit.entity.Player;

public record Blessing(Player player, String destinationGroupName, int blessingDuration) {

}
