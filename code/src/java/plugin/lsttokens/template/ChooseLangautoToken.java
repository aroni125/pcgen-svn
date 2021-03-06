/*
 * Copyright 2007 (C) Thomas Parker <thpr@users.sourceforge.net>
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
package plugin.lsttokens.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import pcgen.base.formula.Formula;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.ChoiceSet;
import pcgen.cdom.base.ConcretePersistentTransitionChoice;
import pcgen.cdom.base.Constants;
import pcgen.cdom.base.FormulaFactory;
import pcgen.cdom.base.PersistentChoiceActor;
import pcgen.cdom.base.PersistentTransitionChoice;
import pcgen.cdom.base.SelectableSet;
import pcgen.cdom.choiceset.ReferenceChoiceSet;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.core.Globals;
import pcgen.core.Language;
import pcgen.core.PCTemplate;
import pcgen.core.PlayerCharacter;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.TokenUtilities;
import pcgen.rules.persistence.token.AbstractTokenWithSeparator;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import pcgen.rules.persistence.token.DeferredToken;
import pcgen.rules.persistence.token.ParseResult;

public class ChooseLangautoToken extends AbstractTokenWithSeparator<PCTemplate> implements
		CDOMSecondaryToken<PCTemplate>, PersistentChoiceActor<Language>,
		DeferredToken<PCTemplate>
{

	private static final Class<Language> LANGUAGE_CLASS = Language.class;

	public String getParentToken()
	{
		return "CHOOSE";
	}

	private String getFullName()
	{
		return getParentToken() + ":" + getTokenName();
	}

	@Override
	public String getTokenName()
	{
		return "LANGAUTO";
	}

	@Override
	protected char separator()
	{
		return '|';
	}

	@Override
	protected ParseResult parseTokenWithSeparator(LoadContext context, PCTemplate template, String value)
	{
		List<CDOMReference<Language>> refs =
				new ArrayList<CDOMReference<Language>>();
		StringTokenizer tok = new StringTokenizer(value, Constants.PIPE);
		while (tok.hasMoreTokens())
		{
			String tokText = tok.nextToken();
			CDOMReference<Language> lang = TokenUtilities.getReference(context,
					LANGUAGE_CLASS, tokText);
			if (lang == null)
			{
				return new ParseResult.Fail("  Error was encountered while parsing "
					+ getFullName() + ": " + value
					+ " had an invalid reference: " + tokText);
			}
			refs.add(lang);
		}

		ReferenceChoiceSet<Language> rcs =
				new ReferenceChoiceSet<Language>(refs);
		if (!rcs.getGroupingState().isValid())
		{
			return new ParseResult.Fail("Non-sensical " + getFullName()
				+ ": Contains ANY and a specific reference: " + value);
		}
		ChoiceSet<Language> cs = new ChoiceSet<Language>(getTokenName(), rcs);
		cs.setTitle("Pick a Language");
		PersistentTransitionChoice<Language> tc =
				new ConcretePersistentTransitionChoice<Language>(cs, FormulaFactory.ONE);
		context.getObjectContext().put(template, ObjectKey.CHOOSE_LANGAUTO, tc);
		tc.setChoiceActor(this);
		return ParseResult.SUCCESS;
	}

	public String[] unparse(LoadContext context, PCTemplate template)
	{
		PersistentTransitionChoice<Language> container = context
				.getObjectContext().getObject(template,
						ObjectKey.CHOOSE_LANGAUTO);
		if (container == null)
		{
			return null;
		}
		SelectableSet<?> cs = container.getChoices();
		Formula f = container.getCount();
		if (f == null)
		{
			context.addWriteMessage("Unable to find " + getFullName()
					+ " Count");
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb
				.append(cs.getLSTformat().replaceAll(Constants.COMMA,
						Constants.PIPE));
		return new String[] { sb.toString() };
	}

	public Class<PCTemplate> getTokenClass()
	{
		return PCTemplate.class;
	}

	public void applyChoice(CDOMObject owner, Language choice,
		PlayerCharacter pc)
	{
		pc.addFreeLanguage(choice, owner);
	}

	public boolean allow(Language choice, PlayerCharacter pc, boolean allowStack)
	{
		return !pc.hasLanguage(choice);
	}

	public Language decodeChoice(String s)
	{
		return Globals.getContext().ref.silentlyGetConstructedCDOMObject(
			LANGUAGE_CLASS, s);
	}

	public String encodeChoice(Language choice)
	{
		return choice.getKeyName();
	}

	public void restoreChoice(PlayerCharacter pc, CDOMObject owner,
		Language choice)
	{
		pc.addFreeLanguage(choice, owner);
	}

	public Class<PCTemplate> getDeferredTokenClass()
	{
		return PCTemplate.class;
	}

	/*
	 * This is deferred into ListKey.ADD to ensure that ADD:.CLEAR doesn't
	 * impact CHOOSE:LANGAUTO. It is hoped that CHOOSE:LANGAUTO can be
	 * refactored into an ADD token in order to avoid this contortion
	 */
	public boolean process(LoadContext context, PCTemplate template)
	{
		PersistentTransitionChoice<Language> langauto = template
				.get(ObjectKey.CHOOSE_LANGAUTO);
		if (langauto != null)
		{
			template.addToListFor(ListKey.ADD, langauto);
		}
		return true;
	}

	public void removeChoice(PlayerCharacter pc, CDOMObject owner,
			Language choice)
	{
		pc.removeFreeLanguage(choice, owner);
	}

	public List<Language> getCurrentlySelected(CDOMObject owner,
			PlayerCharacter pc)
	{
		return Collections.emptyList();
	}
}
