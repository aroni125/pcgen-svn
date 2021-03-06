/**
 * pcgen.core.term.PCSPellBaseStatScoreEvaluator.java
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
 * Created 10-Aug-2008 00:42:42
 *
 * Current Ver: $Revision:$
 * Last Editor: $Author:$
 * Last Edited: $Date:$
 *
 */

package pcgen.core.term;

import pcgen.cdom.enumeration.ObjectKey;
import pcgen.core.PCClass;
import pcgen.core.PCStat;
import pcgen.core.PlayerCharacter;
import pcgen.core.analysis.StatAnalysis;

public class PCSPellBaseStatScoreEvaluatorTermEvaluator 
		extends BasePCTermEvaluator implements TermEvaluator
{
	private final String classKey;

	public PCSPellBaseStatScoreEvaluatorTermEvaluator(String originalText, String classKey)
	{
		this.originalText = originalText;
		this.classKey     = classKey;
	}

	@Override
	public Float resolve(PlayerCharacter pc)
	{
		final PCClass aClass = pc.getClassKeyed(classKey);

		if (aClass == null)
		{
			return 0f;
		}
		
		PCStat ss = aClass.get(ObjectKey.SPELL_STAT);

		if (ss == null)
		{
			return 10f;
		}

		return (float) StatAnalysis.getStatModFor(pc, ss);
	}

	@Override
	public boolean isSourceDependant()
	{
		return true;
	}

	public boolean isStatic()
	{
		return false;
	}
}
