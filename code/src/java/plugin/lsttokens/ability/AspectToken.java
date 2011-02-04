/*
 * AspectToken.java
 * Copyright 2008 (C) James Dempsey
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
 * Created on 16/08/2008 18:13:11
 *
 * $Id: $
 */
package plugin.lsttokens.ability;

import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.AspectName;
import pcgen.cdom.enumeration.MapKey;
import pcgen.cdom.helper.Aspect;
import pcgen.core.Ability;
import pcgen.io.EntityEncoder;
import pcgen.rules.context.LoadContext;
import pcgen.rules.context.MapChanges;
import pcgen.rules.persistence.token.AbstractNonEmptyToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.rules.persistence.token.ParseResult;

/**
 * The Class <code>AspectToken</code> parses a generic detail field for
 * abilities. It is a name/value characteristic allowing substitution of values.
 * 
 * <p>
 * Variable substitution is performed by replacing a placeholder indicated by %#
 * with the #th variable in the variable list. For example, the string <br />
 * <code>&quot;This is %1 variable %3 %2&quot;</code> <br />
 * would be replaced with the string &quot;This is a variable substitution
 * string&quot; if the variable list was &quot;a&quot;,&quot;string&quot;,
 * &quot;substitution&quot;.
 * 
 * Last Editor: $Author: $ Last Edited: $Date: $
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision: $
 */
public class AspectToken extends AbstractNonEmptyToken<Ability> implements
		CDOMPrimaryToken<Ability>
{
	/**
	 * @see pcgen.persistence.lst.LstToken#getTokenName()
	 */
	@Override
	public String getTokenName()
	{
		return "ASPECT"; //$NON-NLS-1$
	}

	@Override
	protected ParseResult parseNonEmptyToken(LoadContext context, Ability ability,
		String value)
	{
		int pipeLoc = value.indexOf(Constants.PIPE);
		if (pipeLoc == -1)
		{
			return new ParseResult.Fail(getTokenName()
					+ " expecting '|', format is: "
					+ "AspectName|Aspect value|Variable|... was: " + value);
		}
		String key = value.substring(0, pipeLoc);
		if (key.length() == 0)
		{
			return new ParseResult.Fail(getTokenName()
					+ " expecting non-empty type, "
					+ "format is: AspectName|Aspect value|Variable|... was: "
					+ value);
		}
		String val = value.substring(pipeLoc + 1);
		if (val.length() == 0)
		{
			return new ParseResult.Fail(getTokenName()
					+ " expecting non-empty value, "
					+ "format is: AspectName|Aspect value|Variable|... was: "
					+ value);
		}
		if (val.startsWith(Constants.PIPE))
		{
			return new ParseResult.Fail(getTokenName()
					+ " expecting non-empty value, "
					+ "format is: AspectName|Aspect value|Variable|... was: "
					+ value);
		}
		Aspect a = parseAspect(key, val);
		context.getObjectContext().put(ability, MapKey.ASPECT, a.getKey(), a);

		return ParseResult.SUCCESS;
	}

	/**
	 * Parses the ASPECT tag into a Aspect object.
	 * 
	 * @param aspectDef
	 *            The LST tag
	 * @return A <tt>Aspect</tt> object
	 */
	public Aspect parseAspect(final String name, final String aspectDef)
	{
		final StringTokenizer tok = new StringTokenizer(aspectDef,
				Constants.PIPE);

		final Aspect aspect = new Aspect(name, EntityEncoder.decode(tok
				.nextToken()));

		while (tok.hasMoreTokens())
		{
			final String token = tok.nextToken();
			aspect.addVariable(token);
		}

		return aspect;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pcgen.rules.persistence.token.CDOMPrimaryParserToken#unparse(pcgen.rules.context.LoadContext,
	 *      java.lang.Object)
	 */
	public String[] unparse(LoadContext context, Ability ability)
	{
		MapChanges<AspectName, Aspect> changes = context.getObjectContext()
				.getMapChanges(ability, MapKey.ASPECT);
		if (changes == null || changes.isEmpty())
		{
			return null;
		}
		Set<String> set = new TreeSet<String>();
		Set<AspectName> keys = changes.getAdded().keySet();
		for (AspectName an : keys)
		{
			Aspect q = changes.getAdded().get(an);
			set.add(new StringBuilder().append(q.getName()).append(
					Constants.PIPE).append(q.getPCCText()).toString());
		}
		return set.toArray(new String[set.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pcgen.rules.persistence.token.CDOMToken#getTokenClass()
	 */
	public Class<Ability> getTokenClass()
	{
		return Ability.class;
	}

}
