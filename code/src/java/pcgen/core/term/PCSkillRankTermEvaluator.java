/**
 * pcgen.core.term.PCSkillRankTermEvaluator.java
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
 * Created 09-Aug-2008 13:21:32
 *
 * Current Ver: $Revision:$
 * Last Editor: $Author:$
 * Last Edited: $Date:$
 *
 */

package pcgen.core.term;

import pcgen.core.Globals;
import pcgen.core.PlayerCharacter;
import pcgen.core.Skill;
import pcgen.core.spell.Spell;
import pcgen.core.analysis.SkillRankControl;

public class PCSkillRankTermEvaluator
		extends BasePCTermEvaluator implements TermEvaluator
{
	private final String rank;

	public PCSkillRankTermEvaluator(String originalText, String rank)
	{
		this.originalText = originalText;
		this.rank         = rank;
	}

	@Override
	public Float resolve(PlayerCharacter pc)
	{
		return convertToFloat(originalText, evaluate(pc));
	}
	
	@Override
	public String evaluate(PlayerCharacter pc)
	{
		Skill skill = Globals.getContext().ref
				.silentlyGetConstructedCDOMObject(Skill.class, rank);
		if (skill == null || !pc.hasSkill(skill))
		{
			return "0.0";
		}

		return SkillRankControl.getRank(pc, skill).toString();		
	}

	@Override
	public String evaluate(PlayerCharacter pc,  final Spell aSpell) {
		return evaluate(pc);
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
