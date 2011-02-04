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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import pcgen.cdom.enumeration.CharID;
import pcgen.core.Equipment;
import pcgen.core.character.EquipSet;

/**
 * EquipSetFacet is a Facet that tracks the EquipSets for a Player Character.
 */
public class EquipSetFacet extends AbstractListFacet<EquipSet> implements
		DataFacetChangeListener<Equipment>
{

	public boolean delEquipSet(CharID id, EquipSet eSet)
	{
		Collection<EquipSet> componentSet = getCachedSet(id);
		if (componentSet == null)
		{
			return false;
		}

		boolean found = false;
		final String pid = eSet.getIdPath();

		// first remove this EquipSet
		componentSet.remove(eSet);

		// now find and remove all it's children
		for (Iterator<EquipSet> e = componentSet.iterator(); e.hasNext();)
		{
			final EquipSet es = e.next();
			final String abParentId = es.getParentIdPath()
					+ EquipSet.PATH_SEPARATOR;
			final String abPid = pid + EquipSet.PATH_SEPARATOR;

			if (abParentId.startsWith(abPid))
			{
				e.remove();
				found = true;
			}
		}
		return found;
	}

	public void updateEquipSetItem(CharID id, Equipment oldItem,
			Equipment newItem)
	{
		if (isEmpty(id))
		{
			return;
		}

		final List<EquipSet> tmpList = new ArrayList<EquipSet>();

		// find all oldItem EquipSet's
		for (EquipSet es : getSet(id))
		{
			final Equipment eqI = es.getItem();

			if ((eqI != null) && oldItem.equals(eqI))
			{
				tmpList.add(es);
			}
		}

		for (EquipSet es : tmpList)
		{
			es.setValue(newItem.getName());
			es.setItem(newItem);
		}
	}

	public void delEquipSetItem(CharID id, Equipment eq)
	{
		if (isEmpty(id))
		{
			return;
		}

		final List<EquipSet> tmpList = new ArrayList<EquipSet>();

		// now find and remove equipment from all EquipSet's
		for (EquipSet es : getSet(id))
		{
			final Equipment eqI = es.getItem();

			if ((eqI != null) && eq.equals(eqI))
			{
				tmpList.add(es);
			}
		}

		for (EquipSet es : tmpList)
		{
			delEquipSet(id, es);
		}
	}

	public EquipSet getEquipSetByIdPath(CharID id, String path)
	{
		for (EquipSet eSet : getSet(id))
		{
			if (eSet.getIdPath().equals(path))
			{
				return eSet;
			}
		}

		return null;
	}

	public EquipSet getEquipSetByName(CharID id, String name)
	{
		for (EquipSet eSet : getSet(id))
		{
			if (eSet.getName().equals(name))
			{
				return eSet;
			}
		}

		return null;
	}

	public Float getEquipSetCount(CharID id, String idPath, String name)
	{
		float count = 0;
		for (EquipSet eSet : getSet(id))
		{
			final String esID = eSet.getIdPath() + EquipSet.PATH_SEPARATOR;
			final String abID = idPath + EquipSet.PATH_SEPARATOR;
			if (esID.startsWith(abID))
			{
				if (eSet.getValue().equals(name))
				{
					count += eSet.getQty().floatValue();
				}
			}
		}
		return Float.valueOf(count);
	}

	public Float getEquippedQuantity(CharID id, EquipSet set, Equipment eq)
	{
		final String rPath = set.getIdPath();

		for (EquipSet es : getSet(id))
		{
			String esIdPath = es.getIdPath() + EquipSet.PATH_SEPARATOR;
			String rIdPath = rPath + EquipSet.PATH_SEPARATOR;

			if (!esIdPath.startsWith(rIdPath))
			{
				continue;
			}

			if (eq.getName().equals(es.getValue()))
			{
				return es.getQty();
			}
		}

		return Float.valueOf(0);
	}

	public void dataAdded(DataFacetChangeEvent<Equipment> dfce)
	{
		//Ignore
	}

	public void dataRemoved(DataFacetChangeEvent<Equipment> dfce)
	{
		delEquipSetItem(dfce.getCharID(), dfce.getCDOMObject());
	}
}
