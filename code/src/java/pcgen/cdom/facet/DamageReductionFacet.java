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
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import pcgen.base.lang.StringUtil;
import pcgen.base.util.CaseInsensitiveMap;
import pcgen.base.util.TreeMapToList;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.content.DamageReduction;
import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.ListKey;

/**
 * DamageReductionFacet is a Facet that tracks the DamageReduction objects that
 * have been granted to a Player Character.
 */
public class DamageReductionFacet extends
		AbstractSourcedListFacet<DamageReduction> implements
		DataFacetChangeListener<CDOMObject>
{

	private static final Pattern OR_PATTERN = Pattern.compile(" [oO][rR] ");
	private static final Pattern AND_PATTERN = Pattern.compile(" [aA][nN][dD] ");

	private PrerequisiteFacet prereqFacet = FacetLibrary
			.getFacet(PrerequisiteFacet.class);
	private FormulaResolvingFacet resolveFacet = FacetLibrary
			.getFacet(FormulaResolvingFacet.class);
	private BonusCheckingFacet bonusFacet = FacetLibrary
			.getFacet(BonusCheckingFacet.class);

	/**
	 * Triggered when one of the Facets to which DamageReductionFacet listens
	 * fires a DataFacetChangeEvent to indicate a DamageReduction was added to a
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
		List<DamageReduction> drs = cdo.getListFor(ListKey.DAMAGE_REDUCTION);
		if (drs != null)
		{
			addAll(dfce.getCharID(), drs, cdo);
		}
	}

	/**
	 * Triggered when one of the Facets to which DamageReductionFacet listens
	 * fires a DataFacetChangeEvent to indicate a DamageReduction was removed
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

	private CaseInsensitiveMap<Integer> getDRMap(CharID id,
			Map<DamageReduction, Set<Object>> componentMap)
	{
		CaseInsensitiveMap<Integer> andMap = new CaseInsensitiveMap<Integer>();
		if (componentMap == null || componentMap.isEmpty())
		{
			return andMap;
		}
		CaseInsensitiveMap<Integer> orMap = new CaseInsensitiveMap<Integer>();
		for (Map.Entry<DamageReduction, Set<Object>> me : componentMap
				.entrySet())
		{
			DamageReduction dr = me.getKey();
			for (Object source : me.getValue())
			{
				if (prereqFacet.qualifies(id, dr, source))
				{
					String sourceString = (source instanceof CDOMObject) ? ((CDOMObject) source)
							.getQualifiedKey()
							: "";
					int rawDrValue = resolveFacet.resolve(id,
							dr.getReduction(), sourceString).intValue();
					String bypass = dr.getBypass();
					if (OR_PATTERN.matcher(bypass).find())
					{
						Integer current = orMap.get(bypass);
						if ((current == null)
								|| (current.intValue() < rawDrValue))
						{
							orMap.put(dr.getBypass(), rawDrValue);
						}
					}
					else
					{
						/*
						 * TODO Shouldn't this expansion be done in the DR
						 * token? (since it's static?)
						 */
						String[] splits = AND_PATTERN.split(bypass);
						if (splits.length == 1)
						{
							Integer current = andMap.get(dr.getBypass());
							if ((current == null)
									|| (current.intValue() < rawDrValue))
							{
								andMap.put(dr.getBypass(), rawDrValue);
							}
						}
						else
						{
							for (int j = 0; j < splits.length; j++)
							{
								Integer current = andMap.get(splits[j]);
								if ((current == null)
										|| (current.intValue() < rawDrValue))
								{
									andMap.put(splits[j], rawDrValue);
								}
							}
						}
					}
				}
			}
		}

		// For each 'or'
		// Case 1: A greater or equal DR for any value in the OR
		// e.g. 10/good + 5/magic or good = 10/good
		// Case 2: A smaller DR for any value in the OR
		// e.g. 10/magic or good + 5/good = 10/magic or good; 5/good
		// e.g. 10/magic or good or lawful + 5/good = 10/good; 5/magic or good
		for (Map.Entry<Object, Integer> me : orMap.entrySet())
		{
			String origBypass = me.getKey().toString();
			Integer reduction = me.getValue();
			String[] orValues = OR_PATTERN.split(origBypass);
			boolean shouldAdd = true;
			for (int j = 0; j < orValues.length; j++)
			{
				// See if we already have a value for this type from the 'and'
				// processing.
				Integer andDR = andMap.get(orValues[j]);
				if (andDR != null && andDR >= reduction)
				{
					shouldAdd = false;
					break;
				}
			}
			if (shouldAdd)
			{
				andMap.put(origBypass, reduction);
			}
		}
		return andMap;
	}

	/*
	 * Collections.sort(ret, new DamageReductionComparator(pc)); return
	 * Collections.unmodifiableList(resultList);
	 */
	public String getDRString(CharID id)
	{
		return getDRString(id, getCachedMap(id));
	}

	/*
	 * Weird exposure for TemplateModifier (don't like this)
	 */
	public String getDRString(CharID id,
			Map<DamageReduction, Set<Object>> cachedMap)
	{
		CaseInsensitiveMap<Integer> map = getDRMap(id, cachedMap);
		TreeMapToList<Integer, String> hml = new TreeMapToList<Integer, String>();
		for (Map.Entry<Object, Integer> me : map.entrySet())
		{
			String key = me.getKey().toString();
			int value = me.getValue();
			if (id != null)
			{
				value += (int) bonusFacet.getBonus(id, "DR", key);
			}
			hml.addToListFor(value, key);
		}
		for (Integer reduction : hml.getKeySet())
		{
			if (hml.sizeOfListFor(reduction) > 1)
			{
				Set<String> set = new TreeSet<String>();
				for (String s : hml.getListFor(reduction))
				{
					if (!OR_PATTERN.matcher(s).find())
					{
						hml.removeFromListFor(reduction, s);
						set.add(s);
					}
				}
				hml.addToListFor(reduction, StringUtil.join(set, " and "));
			}
		}

		StringBuilder sb = new StringBuilder();
		boolean needSeparator = false;
		for (Integer reduction : hml.getKeySet())
		{
			Set<String> set = new TreeSet<String>();
			for (String s : hml.getListFor(reduction))
			{
				set.add(reduction + "/" + s);
			}
			if (needSeparator)
			{
				sb.insert(0, "; ");
			}
			needSeparator = true;
			sb.insert(0, StringUtil.join(set, "; "));
		}
		return sb.toString();
	}

	public Integer getDR(CharID id, String key)
	{
		return getNonBonusDR(id, key)
				+ (int) bonusFacet.getBonus(id, "DR", key);
	}

	private int getNonBonusDR(CharID id, String key)
	{
		Integer drValue = getDRMap(id, getCachedMap(id)).get(key);
		return (drValue == null)? 0 : drValue;
	}
}
