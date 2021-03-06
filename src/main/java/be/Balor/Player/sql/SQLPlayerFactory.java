/*This file is part of AdminCmd.

    AdminCmd is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    AdminCmd is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with AdminCmd.  If not, see <http://www.gnu.org/licenses/>.*/
package be.Balor.Player.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import lib.SQL.PatPeter.SQLibrary.Database;

import org.bukkit.entity.Player;

import be.Balor.Player.ACPlayer;
import be.Balor.Player.EmptyPlayer;
import be.Balor.Player.IPlayerFactory;
import be.Balor.Tools.Debug.ACLogger;
import be.Balor.Tools.Debug.DebugLog;

import com.google.common.base.Joiner;
import com.google.common.collect.MapMaker;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class SQLPlayerFactory implements IPlayerFactory {
	private static PreparedStatement insertPlayer, doubleCheckPlayer;
	private final Map<String, Long> playersID = new MapMaker().concurrencyLevel(6).makeMap();
	private final Object doubleCheckLock = new Object();
	private final Object insertPlayerLock = new Object();

	/**
 * 
 */
	public SQLPlayerFactory() {
		initPrepStmt();
		final ResultSet rs = Database.DATABASE.query("SELECT `name`,`id` FROM `ac_players`");

		try {
			while (rs.next()) {
				playersID.put(rs.getString("name"), rs.getLong("id"));
			}
			rs.close();
		} catch (final SQLException e) {
			ACLogger.severe("Problem when getting players from the DB", e);
		}
		DebugLog.INSTANCE.info("Players found : " + Joiner.on(", ").join(playersID.keySet()));

	}

	/**
	 * 
	 */
	public static synchronized void initPrepStmt() {
		insertPlayer = Database.DATABASE.prepare("INSERT INTO `ac_players` (`name`) VALUES (?);");
		doubleCheckPlayer = Database.DATABASE.prepare("SELECT `id` FROM `ac_players` WHERE `name` = ?");
	}

	private Long getPlayerID(final String playername) {
		Long id = playersID.get(playername);
		if (id != null) {
			return id;
		}

		synchronized (doubleCheckLock) {
			try {
				id = doubleCheckPlayer(playername);
			} catch (final CommunicationsException e) {
				initPrepStmt();
				try {
					id = doubleCheckPlayer(playername);
				} catch (final SQLException e1) {
				}
			} catch (final SQLException e) {
			}
		}

		return id;
	}

	/**
	 * @param playername
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	private Long doubleCheckPlayer(final String playername) throws SQLException {
		ResultSet rs = null;
		Long id;
		try {
			doubleCheckPlayer.clearParameters();
			doubleCheckPlayer.setString(1, playername);
			doubleCheckPlayer.execute();
			rs = doubleCheckPlayer.getResultSet();
			if (rs == null) {
				return null;
			}
			if (!rs.next()) {
				return null;
			}
			id = rs.getLong(1);
			if (id != null) {
				playersID.put(playername, id);
			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (final SQLException e) {

			}
		}
		return id;
	}

	/*
	 * (Non javadoc)
	 * 
	 * @see be.Balor.Player.IPlayerFactory#createPlayer(java.lang.String)
	 */
	@Override
	public ACPlayer createPlayer(final String playername) {
		final Long id = getPlayerID(playername);
		if (id == null) {
			return new EmptyPlayer(playername);
		} else {
			return new SQLPlayer(playername, id);
		}

	}

	/*
	 * (Non javadoc)
	 * 
	 * @see
	 * be.Balor.Player.IPlayerFactory#createPlayer(org.bukkit.entity.Player)
	 */
	@Override
	public ACPlayer createPlayer(final Player player) {
		final Long id = getPlayerID(player.getName());
		if (id == null) {
			return new EmptyPlayer(player);
		} else {
			return new SQLPlayer(player, id);
		}

	}

	/*
	 * (Non javadoc)
	 * 
	 * @see be.Balor.Player.IPlayerFactory#getExistingPlayers()
	 */
	@Override
	public Set<String> getExistingPlayers() {
		return Collections.unmodifiableSet(playersID.keySet());
	}

	/*
	 * (Non javadoc)
	 * 
	 * @see be.Balor.Player.IPlayerFactory#addExistingPlayer(java.lang.String)
	 */
	@Override
	public void addExistingPlayer(final String player) {
		if (!playersID.containsKey(player)) {
			try {
				insertPlayer(player);
			} catch (final CommunicationsException e) {
				initPrepStmt();
				try {
					insertPlayer(player);
				} catch (final SQLException e1) {
					ACLogger.severe("Problem when adding player to the DB", e1);
				}
			} catch (final SQLException e) {
				ACLogger.severe("Problem when adding player to the DB", e);
			}
		}

	}

	/**
	 * @param player
	 * @throws SQLException
	 */
	private void insertPlayer(final String player) throws SQLException {
		synchronized (insertPlayerLock) {
			ResultSet rs = null;
			insertPlayer.clearParameters();
			insertPlayer.setString(1, player);
			insertPlayer.executeUpdate();

			rs = insertPlayer.getGeneratedKeys();
			if (rs.next()) {
				playersID.put(player, rs.getLong(1));
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

}
