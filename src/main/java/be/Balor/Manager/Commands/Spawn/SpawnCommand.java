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
package be.Balor.Manager.Commands.Spawn;

import be.Balor.Manager.Commands.CoreCommand;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public abstract class SpawnCommand extends CoreCommand {
	/**
 * 
 */
	public SpawnCommand() {
		super();
		this.permParent = plugin.getPermissionLinker().getPermParent(
				"admincmd.spawn.*");
	}

	/**
	 * @param string
	 * @param string2
	 */
	public SpawnCommand(final String cmd, final String permNode) {
		super(cmd, permNode);
		this.permParent = plugin.getPermissionLinker().getPermParent(
				"admincmd.spawn.*");
	}
}
