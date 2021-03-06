/*
 * Copyright 2007-2010 (C) Thomas Parker <thpr@users.sourceforge.net>
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
 */
package plugin.lsttokens.choose;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import pcgen.cdom.base.BasicChooseInformation;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.ChooseInformation;
import pcgen.cdom.base.ChooseSelectionActor;
import pcgen.cdom.base.Constants;
import pcgen.cdom.base.PersistentChoiceActor;
import pcgen.cdom.choiceset.SimpleChoiceSet;
import pcgen.cdom.enumeration.AssociationListKey;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.core.PlayerCharacter;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import pcgen.rules.persistence.token.ParseResult;

/**
 * New chooser plugin, handles Strings.
 */
public class StringToken implements CDOMSecondaryToken<CDOMObject>,
		PersistentChoiceActor<String>
{

	public String getTokenName()
	{
		return "STRING";
	}

	public String getParentToken()
	{
		return "CHOOSE";
	}

	public ParseResult parseToken(LoadContext context, CDOMObject obj,
		String value)
	{
		if (value == null || value.length() == 0)
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
				+ " must have arguments");
		}
		if (value.indexOf(',') != -1)
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
				+ " arguments may not contain , : " + value);
		}
		if (value.indexOf('[') != -1)
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
				+ " arguments may not contain [] : " + value);
		}
		if (value.charAt(0) == '|')
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
				+ " arguments may not start with | : " + value);
		}
		if (value.charAt(value.length() - 1) == '|')
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
				+ " arguments may not end with | : " + value);
		}
		if (value.indexOf("||") != -1)
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
				+ " arguments uses double separator || : " + value);
		}

		StringTokenizer tok = new StringTokenizer(value, Constants.PIPE);
		Set<String> set = new HashSet<String>();
		while (tok.hasMoreTokens())
		{
			String tokString = tok.nextToken();
			set.add(tokString);
		}
		SimpleChoiceSet<String> scs =
				new SimpleChoiceSet<String>(set, Constants.PIPE);
		BasicChooseInformation<String> tc =
				new BasicChooseInformation<String>(getTokenName(), scs);
		tc.setTitle("Choose an Item");
		tc.setChoiceActor(this);
		context.obj.put(obj, ObjectKey.CHOOSE_INFO, tc);
		return ParseResult.SUCCESS;
	}

	public String[] unparse(LoadContext context, CDOMObject cdo)
	{
		ChooseInformation<?> tc =
				context.getObjectContext()
					.getObject(cdo, ObjectKey.CHOOSE_INFO);
		if (tc == null)
		{
			return null;
		}
		if (!tc.getName().equals(getTokenName()))
		{
			// Don't unparse anything that isn't owned by this SecondaryToken
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(tc.getLSTformat());
		// TODO oops
		// String title = choices.getTitle();
		// if (!title.equals(getDefaultTitle()))
		// {
		// sb.append("|TITLE=");
		// sb.append(title);
		// }
		return new String[]{sb.toString()};
	}

	public Class<CDOMObject> getTokenClass()
	{
		return CDOMObject.class;
	}

	public String decodeChoice(String s)
	{
		return s;
	}

	public String encodeChoice(String choice)
	{
		return choice;
	}

	public void removeChoice(PlayerCharacter pc, CDOMObject owner, String choice)
	{
		pc.removeAssoc(owner, AssociationListKey.CHOOSE_STRING, choice);
		List<ChooseSelectionActor<?>> actors =
				owner.getListFor(ListKey.NEW_CHOOSE_ACTOR);
		if (actors != null)
		{
			for (ChooseSelectionActor ca : actors)
			{
				ca.removeChoice(owner, choice, pc);
			}
		}
	}

	public void restoreChoice(PlayerCharacter pc, CDOMObject owner,
		String choice)
	{
		pc.addAssoc(owner, AssociationListKey.CHOOSE_STRING, choice);
		pc.addAssociation(owner, encodeChoice(choice));
	}

	public boolean allow(String choice, PlayerCharacter pc, boolean allowStack)
	{
		return true;
	}

	public void applyChoice(CDOMObject owner, String choice, PlayerCharacter pc)
	{
		restoreChoice(pc, owner, choice);
		List<ChooseSelectionActor<?>> actors =
				owner.getListFor(ListKey.NEW_CHOOSE_ACTOR);
		if (actors != null)
		{
			for (ChooseSelectionActor ca : actors)
			{
				applyChoice(owner, choice, pc, ca);
			}
		}
	}

	private void applyChoice(CDOMObject owner, String st, PlayerCharacter pc,
		ChooseSelectionActor<String> ca)
	{
		ca.applyChoice(owner, st, pc);
	}

	public List<String> getCurrentlySelected(CDOMObject owner,
		PlayerCharacter pc)
	{
		return pc.getAssocList(owner, AssociationListKey.CHOOSE_STRING);
	}
}
