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
import pcgen.cdom.enumeration.IntegerKey;
import pcgen.core.PCTemplate;
import pcgen.core.Race;

/**
 * ReachFacet is a Facet that calculates the Reach for a Player Character.
 */
public class ReachFacet
{
	private TemplateFacet templateFacet = FacetLibrary
			.getFacet(TemplateFacet.class);
	private RaceFacet raceFacet = FacetLibrary.getFacet(RaceFacet.class);
	private BonusCheckingFacet bonusFacet = FacetLibrary
			.getFacet(BonusCheckingFacet.class);

	/**
	 * Returns the Reach for a Player Character represented by the given CharID
	 * 
	 * @param id
	 *            The CharID representing the Player Character for which the
	 *            Reach should be returned.
	 * @return The Reach for the Player Character represented by the given
	 *         CharID
	 */
	public int getReach(CharID id)
	{
		final Race aRace = raceFacet.get(id);
		int reach = 0;
		if (aRace != null)
		{
			reach = aRace.getSafe(IntegerKey.REACH);
		}

		// Scan templates for any overrides
		for (PCTemplate template : templateFacet.getSet(id))
		{
			Integer r = template.get(IntegerKey.REACH);
			if (r != null)
			{
				reach = r;
			}
		}
		reach += (int) bonusFacet.getBonus(id, "COMBAT", "REACH");
		return reach;
	}

}
