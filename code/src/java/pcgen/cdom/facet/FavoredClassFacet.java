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

import java.util.List;
import java.util.Set;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.PCClass;

/**
 * FavoredClassFacet is a Facet that tracks the Favored Classes that have been
 * granted to a Player Character.
 */
public class FavoredClassFacet extends AbstractSourcedListFacet<PCClass>
		implements DataFacetChangeListener<CDOMObject>
{

	private HasAnyFavoredClassFacet hasAnyFavoredClassFacet;

	private ClassFacet classFacet;

	private RaceFacet raceFacet;

	private TemplateFacet templateFacet;
	
	private ConditionalTemplateFacet conditionalTemplateFacet;

	/**
	 * Triggered when one of the Facets to which FavoredClassFacet listens fires
	 * a DataFacetChangeEvent to indicate a CDOMObject was added to a Player
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
		List<CDOMReference<? extends PCClass>> list = cdo
				.getListFor(ListKey.FAVORED_CLASS);
		if (list != null)
		{
			for (CDOMReference<? extends PCClass> ref : list)
			{
				addAll(dfce.getCharID(), ref.getContainedObjects(), cdo);
			}
		}
	}

	/**
	 * Triggered when one of the Facets to which FavoredClassFacet listens fires
	 * a DataFacetChangeEvent to indicate a CDOMObject was removed from a Player
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

	public int getFavoredClassLevel(CharID id)
	{
		Set<PCClass> aList = getSet(id);
		int level = 0;
		int max = 0;

		boolean isAny = hasAnyFavoredClassFacet.contains(id, Boolean.TRUE);
		for (PCClass cl : aList)
		{
			for (PCClass pcClass : classFacet.getClassSet(id))
			{
				if (isAny)
				{
					max = Math.max(max, classFacet.getLevel(id, pcClass));
				}
				if (cl.getKeyName().equals(pcClass.getKeyName()))
				{
					level += classFacet.getLevel(id, pcClass);
					break;
				}
			}
		}
		return Math.max(level, max);
	}

	public void setHasAnyFavoredClassFacet(HasAnyFavoredClassFacet hasAnyFavoredClassFacet)
	{
		this.hasAnyFavoredClassFacet = hasAnyFavoredClassFacet;
	}

	public void setClassFacet(ClassFacet classFacet)
	{
		this.classFacet = classFacet;
	}
	
	public void setRaceFacet(RaceFacet raceFacet)
	{
		this.raceFacet = raceFacet;
	}

	public void setTemplateFacet(TemplateFacet templateFacet)
	{
		this.templateFacet = templateFacet;
	}

	public void setConditionalTemplateFacet(
		ConditionalTemplateFacet conditionalTemplateFacet)
	{
		this.conditionalTemplateFacet = conditionalTemplateFacet;
	}

	public void init()
	{
		raceFacet.addDataFacetChangeListener(this);
		templateFacet.addDataFacetChangeListener(this);
		conditionalTemplateFacet.addDataFacetChangeListener(this);
	}
}
