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
package be.Balor.OpenInv;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.PlayerInventory;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class ACPlayerInventory extends PlayerInventory {

	public final ItemStack[] extra = new ItemStack[5];

	/**
	 * @param entityhuman
	 */
	public ACPlayerInventory(final EntityHuman entityhuman) {
		super(entityhuman);
		this.armor = entityhuman.inventory.armor;
		this.items = entityhuman.inventory.items;
	}

	@Override
	public ItemStack[] getContents() {
		final ItemStack[] C = new ItemStack[getSize()];
		System.arraycopy(items, 0, C, 0, items.length);
		System.arraycopy(extra, 0, C, items.length, extra.length);
		System.arraycopy(armor, 0, C, items.length + extra.length, armor.length);
		return C;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.server.PlayerInventory#getSize()
	 */
	@Override
	public int getSize() {
		return super.getSize() + 5;
	}

	@Override
	public boolean a(final EntityHuman entityhuman) {
		return this.player.dead ? false : true;
	}

	@Override
	public String getName() {
		if (player.name.length() > 16) {
			return player.name.substring(0, 16);
		}
		return player.name;
	}

	private int getReversedItemSlotNum(final int i) {
		if (i >= 32) {
			return i - 32;
		} else {
			return i + 9;
		}
	}

	private int getReversedArmorSlotNum(final int i) {
		if (i == 0) {
			return 3;
		}
		if (i == 1) {
			return 2;
		}
		if (i == 2) {
			return 1;
		}
		if (i == 3) {
			return 0;
		} else {
			return i;
		}
	}

	@Override
	public ItemStack getItem(int i) {
		ItemStack[] is = this.items;

		if (i >= is.length) {
			i -= is.length;
			is = this.extra;
		} else {
			i = getReversedItemSlotNum(i);
		}

		if (i >= is.length) {
			i -= is.length;
			is = this.armor;
		}
		if (is == this.armor) {
			i = getReversedArmorSlotNum(i);
		}

		return is[i];
	}

	@Override
	public ItemStack splitStack(int i, final int j) {
		ItemStack[] is = this.items;

		if (i >= is.length) {
			i -= is.length;
			is = this.extra;
		} else {
			i = getReversedItemSlotNum(i);
		}

		if (i >= is.length) {
			i -= is.length;
			is = this.armor;
		}
		if (is == this.armor) {
			i = getReversedArmorSlotNum(i);
		}

		if (is[i] != null) {
			ItemStack itemstack;

			if (is[i].count <= j) {
				itemstack = is[i];
				is[i] = null;
				return itemstack;
			} else {
				itemstack = is[i].a(j);
				if (is[i].count == 0) {
					is[i] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack splitWithoutUpdate(int i) {
		ItemStack[] is = this.items;

		if (i >= is.length) {
			i -= is.length;
			is = this.extra;
		} else {
			i = getReversedItemSlotNum(i);
		}

		if (i >= is.length) {
			i -= is.length;
			is = this.armor;
		}
		if (is == this.armor) {
			i = getReversedArmorSlotNum(i);
		}

		if (is[i] != null) {
			final ItemStack itemstack = is[i];

			is[i] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setItem(int i, final ItemStack itemstack) {
		ItemStack[] is = this.items;

		if (i >= is.length) {
			i -= is.length;
			is = this.extra;
		} else {
			i = getReversedItemSlotNum(i);
		}

		if (i >= is.length) {
			i -= is.length;
			is = this.armor;
		}
		if (is == this.armor) {
			i = getReversedArmorSlotNum(i);
		}
		is[i] = itemstack;
	}
}