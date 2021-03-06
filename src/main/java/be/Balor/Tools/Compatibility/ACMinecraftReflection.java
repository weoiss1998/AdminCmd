/************************************************************************
 * This file is part of AdminCmd.									
 *																		
 * AdminCmd is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by	
 * the Free Software Foundation, either version 3 of the License, or		
 * (at your option) any later version.									
 *																		
 * AdminCmd is distributed in the hope that it will be useful,	
 * but WITHOUT ANY WARRANTY; without even the implied warranty of		
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the			
 * GNU General Public License for more details.							
 *																		
 * You should have received a copy of the GNU General Public License
 * along with AdminCmd.  If not, see <http://www.gnu.org/licenses/>.
 ************************************************************************/
package be.Balor.Tools.Compatibility;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import be.Balor.Tools.Compatibility.Reflect.FieldUtils;

/**
 * @author Antoine
 * 
 */
public class ACMinecraftReflection extends MinecraftReflection {

	/**
	 * 
	 */
	public ACMinecraftReflection() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Retrieve the NMS PlayerInventory class.
	 * 
	 * @return The PlayerInventory class.
	 */
	public static Class<?> getPlayerInventoryClass() {
		return getMinecraftClass("PlayerInventory");
	}

	/**
	 * Retrieve the NMS IInventory class.
	 * 
	 * @return The IInventory class.
	 */
	public static Class<?> getIInventoryClass() {
		return getMinecraftClass("IInventory");
	}

	/**
	 * Retrieve the NMS Packet201PlayerInfo class.
	 * 
	 * @return The Packet201PlayerInfo class.
	 */
	public static Class<?> getPacket201PlayerInfoClass() {
		return getMinecraftClass("Packet201PlayerInfo");
	}

	/**
	 * Retrieve the NMS Packet29DestroyEntity class.
	 * 
	 * @return The Packet29DestroyEntity class.
	 */
	public static Class<?> getPacket29DestroyEntityClass() {
		return getMinecraftClass("Packet29DestroyEntity");
	}

	/**
	 * Retrieve the NMS Packet20NamedEntitySpawn class.
	 * 
	 * @return The Packet20NamedEntitySpawn class.
	 */
	public static Class<?> getPacket20NamedEntitySpawnClass() {
		return getMinecraftClass("Packet20NamedEntitySpawn");
	}

	/**
	 * Retrieve the NMS EntityHuman class.
	 * 
	 * @return The EntityHuman class.
	 */
	public static Class<?> getEntityHumanClass() {
		return getMinecraftClass("EntityHuman");
	}

	/**
	 * Retrieve the CraftBukkit CraftHumanEntity class.
	 * 
	 * @return The CraftHumanEntity class.
	 */
	public static Class<?> getCraftHumanEntityClass() {
		return getCraftBukkitClass("entity.CraftHumanEntity");
	}

	/**
	 * Get the NMS inventory of the given player
	 * 
	 * @param p
	 *            - player
	 * @return {@link PlayerInventory}
	 */
	public static Object getInventory(final Player p) {
		return FieldUtils.getField(ACMinecraftReflection.getHandle(p),
				"inventory");
	}

	/**
	 * Get the NMS Server
	 * 
	 * @return instance of MinecraftServer
	 */
	public static Object getMinecraftServer() {
		final Server server = Bukkit.getServer();
		try {
			return server.getClass().getMethod("getServer").invoke(server);
		} catch (final Exception e) {
			throw new RuntimeException("Can't get server  from " + server, e);
		}
	}

	/**
	 * Dynamically retrieve the name of the current NetServerHandler.
	 * 
	 * @return Name of the NetServerHandler class.
	 */
	public static String getNetServerHandlerName() {
		return getNetServerHandlerClass().getSimpleName();
	}

	/**
	 * Determine if the given object is a NMS object of the given class
	 * 
	 * @param obj
	 *            - the given object.
	 * @param clazz
	 *            - name of the class to test
	 * @return TRUE if it is, FALSE otherwise.
	 */
	public static boolean instanceOfNMS(final Object obj, final String clazz) {
		return getMinecraftClass(clazz).isAssignableFrom(obj.getClass());
	}

	/**
	 * Determine if a given object can be found within the package
	 * net.minecraft.server.
	 * 
	 * @param obj
	 *            - the object to test.
	 * @return TRUE if it can, FALSE otherwise.
	 */
	public static boolean isMinecraftObject(@Nonnull final Object obj) {
		if (obj == null) {
			throw new IllegalArgumentException(
					"Cannot determine the type of a null object.");
		}

		// Doesn't matter if we don't check for the version here
		return obj.getClass().getName().startsWith(MINECRAFT_PREFIX_PACKAGE);
	}

	/**
	 * Determine if a given object is found in net.minecraft.server, and has the
	 * given name.
	 * 
	 * @param obj
	 *            - the object to test.
	 * @param className
	 *            - the class name to test.
	 * @return TRUE if it can, FALSE otherwise.
	 */
	public static boolean isMinecraftObject(@Nonnull final Object obj,
			final String className) {
		if (obj == null) {
			throw new IllegalArgumentException(
					"Cannot determine the type of a null object.");
		}

		final String javaName = obj.getClass().getName();
		return javaName.startsWith(MINECRAFT_PREFIX_PACKAGE)
				&& javaName.endsWith(className);
	}

	/**
	 * Get the Handeling Object from a Bukkit Object
	 * 
	 * @param bukkitObject
	 *            - bukkit object
	 * @return an Handler on success.
	 * @throws RuntimeException
	 *             If we were unable to retrieve the entity.
	 */
	public static Object getHandle(final @Nonnull Object bukkitObject) {
		if (bukkitObject == null) {
			return null;
		}

		// We will have to do this dynamically, unfortunately
		try {
			return bukkitObject.getClass().getMethod("getHandle")
					.invoke(bukkitObject);
		} catch (final Exception e) {
			try {
				return FieldUtils.getField(bukkitObject, "handle");
			} catch (final Exception e1) {
				throw new RuntimeException("Cannot get Handle from "
						+ bukkitObject, e1);
			}
		}
	}

	/**
	 * Get the NetServerHandler of the wanted player
	 * 
	 * @param player
	 *            - CraftPlayer
	 * @return the networkManager
	 * @throws RuntimeException
	 *             If we were unable to retrieve the entity.
	 */
	public static Object getNetServerHandler(final Object player) {
		try {
			final String fieldName = getNetServerHandlerName();
			return FieldUtils.getField(
					player,
					Character.toLowerCase(fieldName.charAt(0))
							+ (fieldName.length() > 1 ? fieldName.substring(1)
									: ""));
		} catch (final Exception e) {
			throw new RuntimeException("Cannot get NetServerHandler from "
					+ player, e);
		}
	}

	/**
	 * Teleport the wanted player to the given location
	 * 
	 * @param player
	 *            - Bukkit player
	 * @param toLocation
	 *            - Location
	 * @throws RuntimeException
	 *             If we were unable to retrieve the entity.
	 */
	public static void teleportPlayer(final Player player,
			final Location toLocation) {
		final Object networkManager = getNetServerHandler(getHandle(player));
		try {
			networkManager.getClass().getMethod("teleport", Location.class)
					.invoke(networkManager, toLocation);
		} catch (final Exception e) {
			throw new RuntimeException("Can't teleport the player " + player
					+ " to " + toLocation, e);
		}
	}

	public static Class<?> getNMSWorldClass() {
		return getMinecraftClass("World");
	}

	public static Class<?> getItemInWorldManagerClass() {
		return getMinecraftClass("ItemInWorldManager", "PlayerInteractManager");
	}

	@SuppressWarnings("unchecked")
	public static <T extends Entity> T getBukkitEntityCasted(
			final Object nmsObject) {
		return (T) MinecraftReflection.getBukkitEntity(nmsObject);
	}

}
