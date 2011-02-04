/*
 * Copyright (c) Thomas Parker, 2010.
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

import java.util.List;

import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.Deity;
import pcgen.core.PCClass;

public class LegalDeityFacet
{
	private ClassFacet classFacet = FacetLibrary.getFacet(ClassFacet.class);

	private PrerequisiteFacet prereqFacet = FacetLibrary
			.getFacet(PrerequisiteFacet.class);

	/*
	 * Note this facet makes no sense to turn into a "push" facet that is a
	 * listener to classes. The reason it makes no sense is that no token
	 * defaults to ANY, and loading the default ANY reference into a cache and
	 * running contains against it would probably be slower than just testing
	 * each Class every time.
	 */
	public boolean allows(CharID id, Deity aDeity)
	{
		if (aDeity == null)
		{
			return false;
		}
		boolean result;
		if (classFacet.isEmpty(id))
		{
			result = true;
		}
		else
		{
			result = false;
			CLASS: for (PCClass aClass : classFacet.getClassSet(id))
			{
				List<CDOMReference<Deity>> deityList = aClass
						.getListFor(ListKey.DEITY);
				if (deityList == null)
				{
					result = true;
					break;
				}
				else
				{
					for (CDOMReference<Deity> deity : deityList)
					{
						if (deity.contains(aDeity))
						{
							result = true;
							break CLASS;
						}
					}
				}
			}
		}

		return result && prereqFacet.qualifies(id, aDeity, aDeity);
	}

}
