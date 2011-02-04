/*
 * Copyright (c) Thomas Parker, 2009.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */
package pcgen.cdom.facet;

import pcgen.cdom.enumeration.CharID;

/**
 * WeightFacet is a Facet that tracks the Player Character's weight. Note that
 * this weight is the actual character weight, not the character plus the
 * character's equipment.
 */
public class WeightFacet
{

	private final Class<?> thisClass = getClass();

	/**
	 * Sets the weight of the Player Character represented by the given CharID
	 * to the given value.
	 * 
	 * @param id
	 *            The CharID representing the Player Character for which the
	 *            weight will be set
	 * @param weight
	 *            The weight to set for the Player Character represented by the
	 *            given CharID
	 */
	public void setWeight(CharID id, int weight)
	{
		FacetCache.set(id, thisClass, weight);
	}

	/**
	 * Removes the weight for the Player Character represented by the given
	 * CharID.
	 * 
	 * @param id
	 *            The CharID representing the Player Character for which the
	 *            weight will be removed
	 */
	public void removeWeight(CharID id)
	{
		FacetCache.remove(id, thisClass);
	}

	/**
	 * Returns the weight for the Player Character represented by the given
	 * CharID
	 * 
	 * @param id
	 *            The CharID of the Player Character for which the weight will
	 *            be returned
	 * @return The weight of the Player Character represented by the given
	 *         CharID
	 */
	public int getWeight(CharID id)
	{
		Integer weight = (Integer) FacetCache.get(id, thisClass);
		return weight == null ? 0 : weight;
	}
}
