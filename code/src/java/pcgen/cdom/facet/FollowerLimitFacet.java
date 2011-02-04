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

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pcgen.base.util.WrappedMapSet;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.helper.FollowerLimit;
import pcgen.cdom.list.CompanionList;

/**
 * FollowerLimitFacet is a Facet that tracks the Follower Limits that have been
 * set for a Player Character.
 */
public class FollowerLimitFacet implements DataFacetChangeListener<CDOMObject>
{
	private FormulaResolvingFacet resolveFacet = FacetLibrary
			.getFacet(FormulaResolvingFacet.class);
	private BonusCheckingFacet bonusFacet = FacetLibrary
			.getFacet(BonusCheckingFacet.class);

	/**
	 * Triggered when one of the Facets to which FollowerOptionFacet listens
	 * fires a DataFacetChangeEvent to indicate a FollowerOption was added to a
	 * Player Character.
	 * 
	 * @param dfce
	 *            The DataFacetChangeEvent containing the information about the
	 *            change
	 * 
	 * @see pcgen.cdom.facet.DataFacetChangeListener#dataAdded(pcgen.cdom.facet.DataFacetChangeEvent)
	 */
	public void dataAdded(DataFacetChangeEvent<CDOMObject> dfce)
	{
		CDOMObject cdo = dfce.getCDOMObject();
		List<FollowerLimit> followers = cdo.getListFor(ListKey.FOLLOWERS);
		if (followers != null)
		{
			addAll(dfce.getCharID(), followers, cdo);
		}
	}

	/**
	 * Triggered when one of the Facets to which FollowerOptionFacet listens
	 * fires a DataFacetChangeEvent to indicate a FollowerOption was removed
	 * from a Player Character.
	 * 
	 * @param dfce
	 *            The DataFacetChangeEvent containing the information about the
	 *            change
	 * 
	 * @see pcgen.cdom.facet.DataFacetChangeListener#dataRemoved(pcgen.cdom.facet.DataFacetChangeEvent)
	 */
	public void dataRemoved(DataFacetChangeEvent<CDOMObject> dfce)
	{
		removeAll(dfce.getCharID(), dfce.getCDOMObject());
	}

	private void addAll(CharID id, List<FollowerLimit> list, CDOMObject cdo)
	{
		for (FollowerLimit fo : list)
		{
			add(id, fo, cdo);
		}
	}

	private void add(CharID id, FollowerLimit fo, CDOMObject cdo)
	{
		if (fo == null)
		{
			throw new IllegalArgumentException("Object to add may not be null");
		}
		CompanionList cl = fo.getCompanionList().resolvesTo();
		Map<FollowerLimit, Set<CDOMObject>> foMap = getConstructingCachedMap(
				id, cl);
		Set<CDOMObject> set = foMap.get(fo);
		if (set == null)
		{
			set = new WrappedMapSet<CDOMObject>(IdentityHashMap.class);
			foMap.put(fo, set);
		}
		set.add(cdo);
	}

	private void removeAll(CharID id, CDOMObject source)
	{
		Map<CompanionList, Map<FollowerLimit, Set<CDOMObject>>> componentMap = getCachedMap(id);
		if (componentMap != null)
		{
			for (Iterator<Map<FollowerLimit, Set<CDOMObject>>> it = componentMap
					.values().iterator(); it.hasNext();)
			{
				Map<FollowerLimit, Set<CDOMObject>> foMap = it.next();
				for (Iterator<Set<CDOMObject>> it2 = foMap.values().iterator(); it2
						.hasNext();)
				{
					Set<CDOMObject> set = it2.next();
					if (set.remove(source) && set.isEmpty())
					{
						it2.remove();
					}
				}
				if (foMap.isEmpty())
				{
					it.remove();
				}
			}
		}
	}

	/**
	 * Returns the type-safe Map for this FollowerLimitFacet and the given
	 * CharID. May return null if no information has been set in this
	 * FollowerLimitFacet for the given CharID.
	 * 
	 * Note that this method SHOULD NOT be public. The Map is owned by
	 * AbstractSourcedListFacet, and since it can be modified, a reference to
	 * that object should not be exposed to any object other than
	 * AbstractSourcedListFacet.
	 * 
	 * @param id
	 *            The CharID for which the Set should be returned
	 * @return The Set for the Player Character represented by the given CharID;
	 *         null if no information has been set in this FollowerLimitFacet
	 *         for the Player Character.
	 */
	private Map<CompanionList, Map<FollowerLimit, Set<CDOMObject>>> getCachedMap(
			CharID id)
	{
		return (Map<CompanionList, Map<FollowerLimit, Set<CDOMObject>>>) FacetCache
				.get(id, getClass());
	}

	/**
	 * Returns a type-safe Map for this FollowerLimitFacet and the given CharID.
	 * Will return a new, empty Map if no information has been set in this
	 * AbstractSourcedListFacet for the given CharID. Will not return null.
	 * 
	 * Note that this method SHOULD NOT be public. The Map object is owned by
	 * AbstractSourcedListFacet, and since it can be modified, a reference to
	 * that object should not be exposed to any object other than
	 * FollowerLimitFacet.
	 * 
	 * @param id
	 *            The CharID for which the Map should be returned
	 * @return The Map for the Player Character represented by the given CharID.
	 */
	private Map<FollowerLimit, Set<CDOMObject>> getConstructingCachedMap(
			CharID id, CompanionList cl)
	{
		Map<CompanionList, Map<FollowerLimit, Set<CDOMObject>>> componentMap = getCachedMap(id);
		if (componentMap == null)
		{
			componentMap = new HashMap<CompanionList, Map<FollowerLimit, Set<CDOMObject>>>();
			FacetCache.set(id, getClass(), componentMap);
		}
		Map<FollowerLimit, Set<CDOMObject>> foMap = componentMap.get(cl);
		if (foMap == null)
		{
			foMap = new IdentityHashMap<FollowerLimit, Set<CDOMObject>>();
			componentMap.put(cl, foMap);
		}
		return foMap;
	}

	public int getMaxFollowers(CharID id, CompanionList cl)
	{
		Map<CompanionList, Map<FollowerLimit, Set<CDOMObject>>> componentMap = getCachedMap(id);
		if (componentMap == null)
		{
			return -1;
		}
		Map<FollowerLimit, Set<CDOMObject>> foMap = componentMap.get(cl);
		if (foMap == null)
		{
			return -1;
		}
		int ret = -1;
		for (Map.Entry<FollowerLimit, Set<CDOMObject>> me : foMap.entrySet())
		{
			FollowerLimit fl = me.getKey();
			Set<CDOMObject> set = me.getValue();
			for (CDOMObject source : set)
			{
				int val = resolveFacet.resolve(id, fl.getValue(),
						source.getQualifiedKey()).intValue();
				ret = Math.max(ret, val);
			}
		}
		if (ret != -1)
		{
			ret += bonusFacet.getBonus(id, "FOLLOWERS", cl.getKeyName()
					.toUpperCase());
		}
		return ret;
	}

}
