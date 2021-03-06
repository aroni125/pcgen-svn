/*
 * SkillModifierTest.java
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
 * Created on 27/09/2008 21:04:10
 *
 * $Id: $
 */
package pcgen.core.analysis;

import org.junit.Test;

import pcgen.AbstractCharacterTestCase;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.SkillArmorCheck;
import pcgen.cdom.enumeration.StringKey;
import pcgen.core.Ability;
import pcgen.core.AbilityCategory;
import pcgen.core.Globals;
import pcgen.core.PCClass;
import pcgen.core.PlayerCharacter;
import pcgen.core.Race;
import pcgen.core.Skill;
import pcgen.core.bonus.Bonus;
import pcgen.core.bonus.BonusObj;
import pcgen.rules.context.LoadContext;
import pcgen.util.TestHelper;

/**
 * The Class <code>SkillModifierTest</code> is responsible for checking that the 
 * SkillModifier class is operating correctly.
 * 
 * Last Editor: $Author: $
 * Last Edited: $Date:  $
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision:  $
 */
public class SkillModifierTest extends AbstractCharacterTestCase
{

	PCClass pcClass;
	Race emptyRace = new Race();
	boolean firstTime = true;
	Ability skillFocus = new Ability();
	Ability persuasive = new Ability();
	Skill bluff;

	/* (non-Javadoc)
	 * @see pcgen.AbstractCharacterTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		LoadContext context = Globals.getContext();

		if (firstTime)
		{
			firstTime = false;

			pcClass = new PCClass();

			TestHelper.makeSkill("Bluff", "Charisma", cha, true,
				SkillArmorCheck.NONE);
			TestHelper.makeSkill("Listen", "Wisdom", wis, true,
				SkillArmorCheck.NONE);

			skillFocus =
					TestHelper.makeAbility("Skill Focus", AbilityCategory.FEAT, "General");
			BonusObj aBonus = Bonus.newBonus(context, "SKILL|LIST|3");
			
			if (aBonus != null)
			{
				skillFocus.addToListFor(ListKey.BONUS, aBonus);
			}
			skillFocus.put(ObjectKey.MULTIPLE_ALLOWED, true);
			skillFocus.put(StringKey.CHOICE_STRING, "SKILLSNAMED|TYPE.Strength|TYPE.Dexterity|TYPE.Constitution|TYPE.Intelligence|TYPE.Wisdom|TYPE.Charisma");

			persuasive =
					TestHelper.makeAbility("Persuasive", AbilityCategory.FEAT, "General");
			aBonus = Bonus.newBonus(context, "SKILL|KEY_Bluff,KEY_Listen|2");
			
			if (aBonus != null)
			{
				persuasive.addToListFor(ListKey.BONUS, aBonus);
			}
			persuasive.put(ObjectKey.MULTIPLE_ALLOWED, false);

		}

		final PlayerCharacter character = getCharacter();
		character.incrementClassLevel(1, pcClass);
	}

	/* (non-Javadoc)
	 * @see pcgen.AbstractCharacterTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception
	{
		pcClass = null;
		super.tearDown();
	}

	/**
	 * Test getModifierExplanation for both lists and multiple 
	 * bonus feats.
	 */
	@Test
	public void testGetModifierExplanation()
	{
		bluff =
				Globals.getContext().ref.silentlyGetConstructedCDOMObject(
					Skill.class, "KEY_bluff");
		PlayerCharacter pc = getCharacter();
		setPCStat(pc, cha, 10);

		assertEquals("Initial state", "", SkillModifier.getModifierExplanation(
			bluff, pc, false));

		Ability sf = pc.addAbilityNeedCheck(AbilityCategory.FEAT, skillFocus);
		pc.addAssociation(sf, "KEY_Bluff");
		pc.calcActiveBonuses();
		assertEquals("Bonus after skill focus", "+3[Skill Focus]",
			SkillModifier.getModifierExplanation(bluff, pc, false));

		pc.addAbilityNeedCheck(AbilityCategory.FEAT, persuasive);
		assertEquals("Bonus after persuasive",
			"+2[Persuasive] +3[Skill Focus]", SkillModifier
				.getModifierExplanation(bluff, pc, false));
	}

}
