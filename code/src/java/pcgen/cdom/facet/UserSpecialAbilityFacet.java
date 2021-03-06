/*
 * Copyright (c) Thomas Parker, 2012.
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

import java.util.ArrayList;
import java.util.List;

import pcgen.cdom.base.QualifiedActor;
import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.helper.SAProcessor;
import pcgen.core.SpecialAbility;

public class UserSpecialAbilityFacet extends
		AbstractQualifiedListFacet<SpecialAbility>
{

	private final PlayerCharacterTrackingFacet trackingFacet = FacetLibrary
		.getFacet(PlayerCharacterTrackingFacet.class);

	public List<SpecialAbility> getResolved(CharID id, Object source)
	{
		List<SpecialAbility> returnList = new ArrayList<SpecialAbility>();
		SAProcessor proc = new SAProcessor(trackingFacet.getPC(id));
		for (SpecialAbility sa : getQualifiedSet(id, source))
		{
			returnList.add(proc.act(sa, source));
		}
		return returnList;
	}

	public <T> List<T> getAllResolved(CharID id, QualifiedActor<SpecialAbility, T> qa)
	{
		return actOnQualifiedSet(id, qa);
	}


}
