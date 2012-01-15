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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.helper.StatLock;
import pcgen.core.PCStat;

/**
 * StatLockFacet is a Facet that tracks the Stats that have been locked on a
 * Player Character.
 */
public class StatLockFacet extends AbstractSourcedListFacet<StatLock> implements
		DataFacetChangeListener<CDOMObject>
{
	private FormulaResolvingFacet resolveFacet = FacetLibrary
			.getFacet(FormulaResolvingFacet.class);

	/**
	 * Triggered when one of the Facets to which StatLockFacet listens fires a
	 * DataFacetChangeEvent to indicate a CDOMObject was added to a Player
	 * Character.
	 * 
	 * @param dfce
	 *            The DataFacetChangeEvent containing the information about the
	 *            change
	 * 
	 * @see pcgen.cdom.facet.DataFacetChangeListener#dataAdded(pcgen.cdom.facet.DataFacetChangeEvent)
	 */
	@Override
	public void dataAdded(DataFacetChangeEvent<CDOMObject> dfce)
	{
		CDOMObject cdo = dfce.getCDOMObject();
		List<StatLock> locks = cdo.getListFor(ListKey.STAT_LOCKS);
		if (locks != null)
		{
			addAll(dfce.getCharID(), locks, cdo);
		}
	}

	/**
	 * Triggered when one of the Facets to which StatLockFacet listens fires a
	 * DataFacetChangeEvent to indicate a CDOMObject was removed from a Player
	 * Character.
	 * 
	 * @param dfce
	 *            The DataFacetChangeEvent containing the information about the
	 *            change
	 * 
	 * @see pcgen.cdom.facet.DataFacetChangeListener#dataRemoved(pcgen.cdom.facet.DataFacetChangeEvent)
	 */
	@Override
	public void dataRemoved(DataFacetChangeEvent<CDOMObject> dfce)
	{
		removeAll(dfce.getCharID(), dfce.getCDOMObject());
	}

	public Number getLockedStat(CharID id, PCStat stat)
	{
		Number max = Double.NEGATIVE_INFINITY;
		boolean hit = false;

		Map<StatLock, Set<Object>> componentMap = getCachedMap(id);
		if (componentMap != null)
		{
			for (Iterator<Map.Entry<StatLock, Set<Object>>> it = componentMap
					.entrySet().iterator(); it.hasNext();)
			{
				Entry<StatLock, Set<Object>> me = it.next();
				Set<Object> set = me.getValue();
				StatLock lock = me.getKey();
				if (lock.getLockedStat().equals(stat))
				{
					for (Object source : set)
					{
						String sourceString = (source instanceof CDOMObject) ? ((CDOMObject) source)
								.getQualifiedKey()
								: "";
						Number val = resolveFacet.resolve(id, lock
								.getLockValue(), sourceString);
						if (val.doubleValue() > max.doubleValue())
						{
							hit = true;
							max = val;
						}
					}
				}
			}
		}
		return hit ? max : null;
	}
}
