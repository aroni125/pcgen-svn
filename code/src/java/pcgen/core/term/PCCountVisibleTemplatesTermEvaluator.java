/**
 * pcgen.core.term.PCCountVisibleTemplatesTermEvaluator.java
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
 * Created 09-Aug-2008 20:39:59
 *
 * Current Ver: $Revision:$
 * Last Editor: $Author:$
 * Last Edited: $Date:$
 *
 */

package pcgen.core.term;

import pcgen.core.PlayerCharacter;
import pcgen.core.PCTemplate;
import pcgen.util.enumeration.Visibility;
import pcgen.cdom.enumeration.ObjectKey;

public class PCCountVisibleTemplatesTermEvaluator 
		extends BasePCTermEvaluator implements TermEvaluator
{
	public PCCountVisibleTemplatesTermEvaluator(String originalText)
	{
		this.originalText = originalText;
	}

	@Override
	public Float resolve(PlayerCharacter pc)
	{
		Float count = 0f;

		for ( PCTemplate template : pc.getTemplateSet() )
		{
			final Visibility vis = template.getSafe(ObjectKey.VISIBILITY);

			if ((vis == Visibility.DEFAULT)
			|| (vis == Visibility.OUTPUT_ONLY))
			{
				count++;
			}
		}

		return count;
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
