/*
 * LoadInfo.java
 * Copyright (c) Thomas Parker, 2010.
 * Copyright 2002 (C) James Dempsey
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on August 16, 2002, 10:00 PM AEST (+10:00)
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 *
 */
package pcgen.core.system;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import pcgen.cdom.base.Loadable;
import pcgen.cdom.reference.CDOMSingleRef;
import pcgen.core.SizeAdjustment;

/**
 * <code>LoadInfo</code> describes the data associated with a loads and
 * encumbrance
 * 
 * @author Stefan Radermacher <zaister@users.sourceforge.net>
 * @version $Revision$
 */
public class LoadInfo implements Loadable
{
	private URI sourceURI;
	private String loadInfoName;

	private Map<CDOMSingleRef<SizeAdjustment>, BigDecimal> rawSizeMultiplierMap = new HashMap<CDOMSingleRef<SizeAdjustment>, BigDecimal>();
	private Map<SizeAdjustment, BigDecimal> sizeMultiplierMap = new HashMap<SizeAdjustment, BigDecimal>();

	private SortedMap<Integer, BigDecimal> strengthLoadMap = new TreeMap<Integer, BigDecimal>();
	private int minStrenghScoreWithLoad = 0;
	private int maxStrengthScoreWithLoad = 0;

	private BigDecimal loadScoreMultiplier = BigDecimal.ZERO;
	private int loadMultStep = 10;

	private Map<String, LoadInfo.LoadMapEntry> loadMultiplierMap = new HashMap<String, LoadInfo.LoadMapEntry>();
	private String modifyFormula;

	public URI getSourceURI()
	{
		return sourceURI;
	}

	public void setSourceURI(URI source)
	{
		sourceURI = source;
	}

	/**
	 * Set the load score multiplier
	 * 
	 * @param value
	 */
	public void setLoadScoreMultiplier(BigDecimal multiplier)
	{
		loadScoreMultiplier = multiplier;
	}

	public BigDecimal getLoadScoreMultiplier()
	{
		return loadScoreMultiplier;
	}

	/**
	 * Add a load score/value pair
	 * 
	 * @param score
	 * @param load
	 */
	public void addLoadScoreValue(int score, BigDecimal load)
	{
		strengthLoadMap.put(score, load);
		if (score > maxStrengthScoreWithLoad)
		{
			maxStrengthScoreWithLoad = score;
		}
		if (score < minStrenghScoreWithLoad)
		{
			minStrenghScoreWithLoad = score;
		}
	}

	/**
	 * Get the value for a load score
	 * 
	 * @param score
	 * @return the value for a load score
	 */
	public BigDecimal getLoadScoreValue(int score)
	{
		if (score < minStrenghScoreWithLoad)
		{
			return BigDecimal.ZERO;
		}
		else if (score > maxStrengthScoreWithLoad)
		{
			if (getLoadMultiplierCount() == 1)
			{
				// TODO Isn't this a bug??
				return getLoadScoreValue(minStrenghScoreWithLoad);
			}
			return loadScoreMultiplier.multiply(getLoadScoreValue(score
					- loadMultStep));
		}
		else
		{
			BigDecimal loadScore = strengthLoadMap.get(score);
			if (loadScore == null)
			{
				SortedMap<Integer, BigDecimal> headMap = strengthLoadMap
						.headMap(score);
				/*
				 * Assume headMap is populated, since minScore is tested, above -
				 * thpr Mar 14, 2007
				 */
				return strengthLoadMap.get(headMap.lastKey());
			}
			return loadScore;
		}
	}

	/**
	 * Add a size adjustment
	 * 
	 * @param size
	 * @param multiplier
	 */
	public void addSizeAdjustment(CDOMSingleRef<SizeAdjustment> size,
			BigDecimal multiplier)
	{
		rawSizeMultiplierMap.put(size, multiplier);
	}

	public void resolveSizeAdjustmentMap()
	{
		for (Map.Entry<CDOMSingleRef<SizeAdjustment>, BigDecimal> me : rawSizeMultiplierMap
				.entrySet())
		{
			sizeMultiplierMap.put(me.getKey().resolvesTo(), me.getValue());
		}
	}

	/**
	 * Get the size adjustment
	 * 
	 * @param size
	 * @return the size adjustment
	 */
	public BigDecimal getSizeAdjustment(SizeAdjustment size)
	{
		if (sizeMultiplierMap.containsKey(size))
		{
			return sizeMultiplierMap.get(size);
		}
		return null;
	}

	/**
	 * Add load multiplier
	 * @param encumbranceType
	 * @param value
	 * @param formula
	 * @param checkPenalty
	 */
	public void addLoadMultiplier(String encumbranceType, Float value,
			String formula, Integer checkPenalty)
	{
		LoadMapEntry newEntry = new LoadMapEntry(value, formula, checkPenalty);
		loadMultiplierMap.put(encumbranceType, newEntry);
	}

	/**
	 * Get the load multiplier
	 * @param encumbranceType
	 * @return load multiplier
	 */
	public Float getLoadMultiplier(String encumbranceType)
	{
		if (loadMultiplierMap.containsKey(encumbranceType))
		{
			return loadMultiplierMap.get(encumbranceType).getMuliplier();
		}
		return null;
	}

	/**
	 * Get the load move formula
	 * @param encumbranceType
	 * @return the load move formula
	 */
	public String getLoadMoveFormula(String encumbranceType)
	{
		if (loadMultiplierMap.containsKey(encumbranceType))
		{
			return loadMultiplierMap.get(encumbranceType).getFormula();
		}
		return "";
	}

	/**
	 * Get the load check penalty
	 * @param encumbranceType
	 * @return the load check penalty
	 */
	public int getLoadCheckPenalty(String encumbranceType)
	{
		if (loadMultiplierMap.containsKey(encumbranceType))
		{
			return loadMultiplierMap.get(encumbranceType).getCheckPenalty();
		}
		return 0;
	}

	/**
	 * Set the load modifier formula
	 * @param argFormula
	 */
	public void setLoadModifierFormula(final String argFormula)
	{
		modifyFormula = argFormula;
	}

	/**
	 * Get the load modifier formula
	 * @return the load modifier formula
	 */
	public String getLoadModifierFormula()
	{
		return modifyFormula;
	}

	/**
	 * Get the load multiplier count
	 * @return the load multiplier count
	 */
	public int getLoadMultiplierCount()
	{
		return loadMultiplierMap.size();
	}

	private static class LoadMapEntry
	{
		private Float multiplier;
		private String moveFormula;
		private Integer checkPenalty;

		/**
		 * Constructor
		 * @param argMultiplier
		 * @param argFormula
		 * @param argPenalty
		 */
		public LoadMapEntry(Float argMultiplier, String argFormula,
				Integer argPenalty)
		{
			multiplier = argMultiplier;
			moveFormula = argFormula;
			checkPenalty = argPenalty;
		}

		/**
		 * Get multiplier
		 * @return multiplier
		 */
		public Float getMuliplier()
		{
			return multiplier;
		}

		/**
		 * Get the formula
		 * @return formula
		 */
		public String getFormula()
		{
			return moveFormula;
		}

		/**
		 * Get the check penalty
		 * @return the check penalty
		 */
		public int getCheckPenalty()
		{
			return checkPenalty.intValue();
		}
	}
	
	public void setLoadMultStep(int step)
	{
		loadMultStep = step;
	}

	public String getDisplayName()
	{
		return loadInfoName;
	}

	public String getKeyName()
	{
		return getDisplayName();
	}

	public String getLSTformat()
	{
		return getDisplayName();
	}

	public boolean isInternal()
	{
		return false;
	}

	public boolean isType(String type)
	{
		return false;
	}

	public void setName(String name)
	{
		loadInfoName = name;
	}

}
