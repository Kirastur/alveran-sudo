package de.polarwolf.alveran.integration.worldguard;

import org.bukkit.World;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public record WorldGuardRegion(ProtectedRegion region, World world) {

}
