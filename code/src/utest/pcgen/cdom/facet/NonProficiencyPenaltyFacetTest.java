/*
 * Copyright (c) 2009 Tom Parker <thpr@users.sourceforge.net>
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
package pcgen.cdom.facet;

import junit.framework.TestCase;

import org.junit.Test;

import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.IntegerKey;
import pcgen.core.Globals;
import pcgen.core.PCTemplate;

public class NonProficiencyPenaltyFacetTest extends TestCase
{
	/*
	 * NOTE: This is not literal unit testing - it is leveraging the existing
	 * TemplateFacet framework. This class trusts that TemplateFacetTest has
	 * fully vetted TemplateFacet. PLEASE ensure all tests there are working
	 * before investigating tests here.
	 */
	private CharID id;
	private CharID altid;
	private NonProficiencyPenaltyFacet facet;
	private TemplateFacet tfacet = new TemplateFacet();

	@Override
	public void setUp() throws Exception
	{
		facet = new NonProficiencyPenaltyFacet();
		super.setUp();
		facet.setTemplateFacet(tfacet);
		id = new CharID();
		altid = new CharID();
	}

	@Test
	public void testGenderUnsetNull()
	{
		assertEquals(Globals.getGameModeNonProfPenalty(), facet.getPenalty(id));
	}

	@Test
	public void testWithNothingInTemplates()
	{
		tfacet.add(id, new PCTemplate());
		assertEquals(Globals.getGameModeNonProfPenalty(), facet.getPenalty(id));
	}

	@Test
	public void testAvoidPollution()
	{
		PCTemplate pct = new PCTemplate();
		pct.put(IntegerKey.NONPP, -2);
		tfacet.add(id, pct);
		assertEquals(Globals.getGameModeNonProfPenalty(), facet.getPenalty(altid));
	}

	@Test
	public void testGenderLocked()
	{
		PCTemplate pct = new PCTemplate();
		pct.put(IntegerKey.NONPP, -3);
		tfacet.add(id, pct);
		assertEquals(-3, facet.getPenalty(id));
		tfacet.remove(id, pct);
		assertEquals(Globals.getGameModeNonProfPenalty(), facet.getPenalty(id));
	}

	@Test
	public void testMultipleGenderSetSecondDominatesGender()
	{
		PCTemplate pct = new PCTemplate();
		pct.put(IntegerKey.NONPP, -2);
		tfacet.add(id, pct);
		assertEquals(-2, facet.getPenalty(id));
		PCTemplate pct2 = new PCTemplate();
		pct2.put(IntegerKey.NONPP, -3);
		tfacet.add(id, pct2);
		assertEquals(-3, facet.getPenalty(id));
		tfacet.remove(id, pct);
		assertEquals(-3, facet.getPenalty(id));
		tfacet.add(id, pct);
		assertEquals(-2, facet.getPenalty(id));
		tfacet.remove(id, pct);
		assertEquals(-3, facet.getPenalty(id));
		tfacet.remove(id, pct2);
		assertEquals(Globals.getGameModeNonProfPenalty(), facet.getPenalty(id));
	}

}
