package de.polarwolf.alveran.integration.worldguard;

import org.bukkit.World;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.polarwolf.alveran.exception.AlveranException;
import de.polarwolf.alveran.text.Message;

public class WorldGuardHook {

	public boolean hasRegion(World world, String regionName) {
		ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer()
				.get(BukkitAdapter.adapt(world)).getRegion(regionName);
		return (region != null);
	}

	public WorldGuardRegion getRegion(World world, String regionName) throws AlveranException {
		ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer()
				.get(BukkitAdapter.adapt(world)).getRegion(regionName);
		if (region == null) {
			throw new AlveranException(world.getName(), Message.WORLDGUARD_REGION_NOT_FOUND, regionName);
		}
		return new WorldGuardRegion(region, world);
	}

	public boolean isInside(Vector vector, WorldGuardRegion region) {
		BlockVector3 bloc = BlockVector3.at(vector.getX(), vector.getY(), vector.getZ());
		return region.region().contains(bloc);
	}

}
