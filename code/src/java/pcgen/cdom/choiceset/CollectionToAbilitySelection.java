/*
 * Copyright 2010 (C) Tom Parker <thpr@users.sourceforge.net>
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package pcgen.cdom.choiceset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.Converter;
import pcgen.cdom.base.PrimitiveChoiceSet;
import pcgen.cdom.base.PrimitiveCollection;
import pcgen.cdom.base.PrimitiveFilter;
import pcgen.cdom.enumeration.GroupingState;
import pcgen.cdom.enumeration.Nature;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.helper.AbilitySelection;
import pcgen.core.Ability;
import pcgen.core.AbilityCategory;
import pcgen.core.PlayerCharacter;
import pcgen.core.chooser.ChooserUtilities;

public class CollectionToAbilitySelection implements
		PrimitiveChoiceSet<AbilitySelection>
{
	private final PrimitiveCollection<Ability> collection;

	public CollectionToAbilitySelection(PrimitiveCollection<Ability> coll)
	{
		if (coll == null)
		{
			throw new IllegalArgumentException(
					"PrimitiveCollection must not be null");
		}
		collection = coll;
	}

	public Class<? super AbilitySelection> getChoiceClass()
	{
		return AbilitySelection.class;
	}

	public GroupingState getGroupingState()
	{
		return collection.getGroupingState();
	}

	public String getLSTformat(boolean useAny)
	{
		return collection.getLSTformat(useAny);
	}

	public Collection<AbilitySelection> getSet(PlayerCharacter pc)
	{
		return collection.getCollection(pc, new ExpandingConverter(pc));
	}

	/**
	 * Returns the consistent-with-equals hashCode for this
	 * CollectionToAbilitySelection
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return collection.hashCode();
	}

	/**
	 * Returns true if this CollectionToAbilitySelection is equal to the given
	 * Object. Equality is defined as being another CollectionToAbilitySelection
	 * object with equal underlying contents.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof CollectionToAbilitySelection)
				&& ((CollectionToAbilitySelection) obj).collection
						.equals(collection);
	}

	public static class ExpandingConverter implements
			Converter<Ability, AbilitySelection>
	{

		private final PlayerCharacter character;

		public ExpandingConverter(PlayerCharacter pc)
		{
			character = pc;
		}

		public Collection<AbilitySelection> convert(CDOMReference<Ability> ref)
		{
			Set<AbilitySelection> returnSet = new HashSet<AbilitySelection>();
			for (Ability a : ref.getContainedObjects())
			{
				processAbility(ref, returnSet, a);
			}
			return returnSet;
		}

		private void processAbility(CDOMReference<Ability> ref,
				Set<AbilitySelection> returnSet, Ability a)
		{
			if (a.getSafe(ObjectKey.MULTIPLE_ALLOWED))
			{
				returnSet.addAll(addMultiplySelectableAbility(character, a, ref
						.getChoice()));
			}
			else
			{
				returnSet.add(new AbilitySelection(a, Nature.NORMAL));
			}
		}

		public Collection<AbilitySelection> convert(CDOMReference<Ability> ref,
				PrimitiveFilter<Ability> lim)
		{
			Set<AbilitySelection> returnSet = new HashSet<AbilitySelection>();
			for (Ability a : ref.getContainedObjects())
			{
				if (lim.allow(character, a))
				{
					processAbility(ref, returnSet, a);
				}
			}
			return returnSet;
		}

		private Collection<AbilitySelection> addMultiplySelectableAbility(
				final PlayerCharacter aPC, Ability ability, String subName)
		{
			// If already have taken the feat, use it so we can remove
			// any choices already selected
			final Ability pcFeat = aPC.getFeatNamed(ability.getKeyName());

			Ability pcability = ability;
			if (pcFeat != null)
			{
				pcability = pcFeat;
			}

			boolean isPattern = false;
			String nameRoot = null;
			if (subName != null)
			{
				final int percIdx = subName.indexOf('%');

				if (percIdx > -1)
				{
					isPattern = true;
					nameRoot = subName.substring(0, percIdx);
				}
				else if (subName.length() != 0)
				{
					nameRoot = subName;
				}
			}

			final List<String> availableList = new ArrayList<String>();
			final List<?> tempAvailList = new ArrayList<Object>();
			final List<?> tempSelList = new ArrayList<Object>();
			ChooserUtilities.modChoices(pcability, tempAvailList, tempSelList,
					false, aPC, true, AbilityCategory.FEAT);
			// Mod choices may have sent us back weaponprofs, abilities or
			// strings,
			// so we have to do a conversion here
			for (Object o : tempAvailList)
			{
				String choice = o.toString();
				if ("NOCHOICE".equals(choice))
				{
					availableList.add("");
				}
				else
				{
					availableList.add(choice);
				}
			}

			// Remove any that don't match

			if (nameRoot != null && nameRoot.length() != 0)
			{
				for (int n = availableList.size() - 1; n >= 0; --n)
				{
					final String aString = availableList.get(n);

					if (!aString.startsWith(nameRoot))
					{
						availableList.remove(n);
					}
				}

				// Example: ADD:FEAT(Skill Focus(Craft (Basketweaving))) If you
				// have no ranks in Craft (Basketweaving), the available list
				// will
				// be empty
				//
				// Make sure that the specified feat is available, even though
				// it
				// does not meet the prerequisite

				if (isPattern && !availableList.isEmpty())
				{
					availableList.add(nameRoot);
				}
			}

			List<AbilitySelection> returnList = new ArrayList<AbilitySelection>(
					availableList.size());
			for (String s : availableList)
			{
				returnList
						.add(new AbilitySelection(pcability, Nature.NORMAL, s));
			}
			return returnList;
		}

	}
}
