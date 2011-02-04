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

import java.util.IdentityHashMap;
import java.util.Set;

import pcgen.base.util.WrappedMapSet;
import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.reference.CDOMSingleRef;
import pcgen.core.Equipment;
import pcgen.core.WeaponProf;

/**
 * WeaponProfFacet is a Facet that tracks the WeaponProfs that have been granted
 * to a Player Character.
 */
public class WeaponProfFacet extends AbstractSourcedListFacet<WeaponProf>
		implements DataFacetChangeListener<WeaponProf>
{

	private final AutoWeaponProfFacet awpFacet = FacetLibrary
			.getFacet(AutoWeaponProfFacet.class);
	private final HasDeityWeaponProfFacet hdwpFacet = FacetLibrary
			.getFacet(HasDeityWeaponProfFacet.class);
	private final DeityWeaponProfFacet deityWeaponProfFacet = FacetLibrary
			.getFacet(DeityWeaponProfFacet.class);

	/**
	 * Triggered when one of the Facets to which WeaponProfFacet listens fires a
	 * DataFacetChangeEvent to indicate a WeaponProf was added to a Player
	 * Character.
	 * 
	 * @param dfce
	 *            The DataFacetChangeEvent containing the information about the
	 *            change
	 * 
	 * @see pcgen.cdom.facet.DataFacetChangeListener#dataAdded(pcgen.cdom.facet.DataFacetChangeEvent)
	 */
	public void dataAdded(DataFacetChangeEvent<WeaponProf> dfce)
	{
		add(dfce.getCharID(), dfce.getCDOMObject(), dfce.getSource());
	}

	/**
	 * Triggered when one of the Facets to which WeaponProfFacet listens fires a
	 * DataFacetChangeEvent to indicate a WeaponProf was removed from a Player
	 * Character.
	 * 
	 * @param dfce
	 *            The DataFacetChangeEvent containing the information about the
	 *            change
	 * 
	 * @see pcgen.cdom.facet.DataFacetChangeListener#dataRemoved(pcgen.cdom.facet.DataFacetChangeEvent)
	 */
	public void dataRemoved(DataFacetChangeEvent<WeaponProf> dfce)
	{
		remove(dfce.getCharID(), dfce.getCDOMObject(), dfce.getSource());
	}

	public Set<WeaponProf> getProfs(CharID id)
	{
		final Set<WeaponProf> ret = new WrappedMapSet<WeaponProf>(IdentityHashMap.class);
		ret.addAll(getSet(id));
		ret.addAll(awpFacet.getWeaponProfs(id));
		if (hdwpFacet.hasDeityWeaponProf(id))
		{
			ret.addAll(deityWeaponProfFacet.getSet(id));
		}
		return ret;
	}

	public boolean containsProf(CharID id, WeaponProf wp)
	{
		return contains(id, wp) || awpFacet.getWeaponProfs(id).contains(wp)
				|| hdwpFacet.hasDeityWeaponProf(id)
				&& deityWeaponProfFacet.getSet(id).contains(wp);
	}

	public boolean isProficientWithWeapon(CharID id, Equipment eq)
	{
		if (eq.isNatural())
		{
			return true;
		}

		CDOMSingleRef<WeaponProf> ref = eq.get(ObjectKey.WEAPON_PROF);
		if (ref == null)
		{
			return false;
		}

		return containsProf(id, ref.resolvesTo());
	}

}
