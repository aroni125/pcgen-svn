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
package pcgen.cdom.base;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pcgen.base.lang.StringUtil;
import pcgen.cdom.enumeration.AssociationListKey;
import pcgen.cdom.enumeration.GroupingState;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.PlayerCharacter;
import pcgen.core.chooser.ChoiceManagerList;
import pcgen.core.chooser.UserInputManager;

public class UserChooseInformation implements ChooseInformation<String>,
		PersistentChoiceActor<String>
{

	public static final String UCI_NAME = "User Input";
	
	/**
	 * The title (presented to the user) of this ChoiceSet
	 */
	private String title = null;

	public Class<String> getChoiceClass()
	{
		return String.class;
	}

	public ChoiceManagerList getChoiceManager(CDOMObject owner, int cost)
	{
		return new UserInputManager(owner, this, cost);
	}

	public GroupingState getGroupingState()
	{
		return GroupingState.ALLOWS_NONE;
	}

	public String getLSTformat()
	{
		return "*USERINPUT";
	}

	public String getName()
	{
		return UCI_NAME;
	}

	public Collection<String> getSet(PlayerCharacter pc)
	{
		return Collections.singletonList("USERINPUT");
	}

	public String getTitle()
	{
		return title == null ? "Provide User Input" : title;
	}

	public CharSequence getDisplay(PlayerCharacter pc, CDOMObject owner)
	{
		return StringUtil.joinToStringBuffer(pc.getExpandedAssociations(owner),
			", ");
	}

	public void restoreChoice(PlayerCharacter pc, CDOMObject owner,
		String choice)
	{
		pc.addAssoc(owner, AssociationListKey.CHOOSE_NOCHOICE, choice);
		pc.addAssociation(owner, choice);
	}

	public List<String> getCurrentlySelected(CDOMObject owner,
		PlayerCharacter pc)
	{
		return pc.getAssocList(owner, AssociationListKey.CHOOSE_NOCHOICE);
	}

	public void applyChoice(CDOMObject owner, String choice, PlayerCharacter pc)
	{
		restoreChoice(pc, owner, choice);
		List<ChooseSelectionActor<?>> actors =
				owner.getListFor(ListKey.NEW_CHOOSE_ACTOR);
		if (actors != null)
		{
			for (ChooseSelectionActor csa : actors)
			{
				applyChoice(owner, pc, choice, csa);
			}
		}
	}

	private void applyChoice(CDOMObject owner, PlayerCharacter pc, String choice,
		ChooseSelectionActor<String> csa)
	{
		csa.applyChoice(owner, choice, pc);
	}

	public void removeChoice(PlayerCharacter pc, CDOMObject owner, String choice)
	{
		pc.removeAssoc(owner, AssociationListKey.CHOOSE_NOCHOICE, choice);
		pc.removeAssociation(owner, choice);
		List<ChooseSelectionActor<?>> actors =
				owner.getListFor(ListKey.NEW_CHOOSE_ACTOR);
		if (actors != null)
		{
			for (ChooseSelectionActor csa : actors)
			{
				csa.removeChoice(owner, choice, pc);
			}
		}
	}

	public PersistentChoiceActor<String> getChoiceActor()
	{
		return this;
	}

	public void setChoiceActor(ChoiceActor<String> actor)
	{
		// ignore
	}

	public boolean allow(String choice, PlayerCharacter pc, boolean allowStack)
	{
		return true;
	}

	public String decodeChoice(String choice)
	{
		return choice;
	}

	public String encodeChoice(String choice)
	{
		return choice;
	}

	public void setTitle(String chooseTitle)
	{
		title = chooseTitle;
	}

}
