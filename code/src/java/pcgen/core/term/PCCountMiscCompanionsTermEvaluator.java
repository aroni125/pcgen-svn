/**
 * pcgen.core.term.PCCountMiscCompanionsTermEvaluator.java
 * Copyright (c) 2008 Andrew Wilson <nuance@users.sourceforge.net>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created 09-Aug-2008 20:05:58
 *
 * Current Ver: $Revision:$
 * Last Editor: $Author:$
 * Last Edited: $Date:$
 *
 */

package pcgen.core.term;

import java.util.Arrays;
import java.util.List;

import pcgen.cdom.enumeration.StringKey;
import pcgen.core.PlayerCharacter;

public class PCCountMiscCompanionsTermEvaluator
		extends BasePCTermEvaluator implements TermEvaluator
{
	public PCCountMiscCompanionsTermEvaluator(String originalText)
	{
		this.originalText = originalText;
	}

	@Override
	public Float resolve(PlayerCharacter pc)
	{
		String CompString = pc.getSafeStringFor(StringKey.MISC_COMPANIONS);
		List<String> companions = Arrays.asList(CompString.split("\r?\n"));
		return (float) companions.size();
	}

	@Override
	public boolean isSourceDependant()
	{
		return false;
	}

	public boolean isStatic()
	{
		return false;
	}
}
