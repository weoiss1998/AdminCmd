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
package be.Balor.Tools.Egg.Exceptions;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class DontHaveThePermissionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8920472001709861953L;

	/**
	 * 
	 */
	public DontHaveThePermissionException(final String message) {
		super(message);
	}

}
