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
import pcgen.cdom.enumeration.Gender;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.core.PCTemplate;

public class GenderFacetTest extends TestCase
{
	/*
	 * NOTE: This is not literal unit testing - it is leveraging the existing
	 * TemplateFacet framework. This class trusts that TemplateFacetTest has
	 * fully vetted TemplateFacet. PLEASE ensure all tests there are working
	 * before investigating tests here.
	 */
	private CharID id;
	private CharID altid;
	private GenderFacet facet;
	private TemplateFacet tfacet = new TemplateFacet();

	@Override
	public void setUp() throws Exception
	{
		facet = new GenderFacet();
		super.setUp();
		facet.setTemplateFacet(tfacet);
		id = new CharID();
		altid = new CharID();
	}

	@Test
	public void testGenderUnsetNull()
	{
		assertEquals(Gender.getDefaultValue(), facet.getGender(id));
	}

	@Test
	public void testWithNothingInTemplates()
	{
		tfacet.add(id, new PCTemplate());
		assertEquals(Gender.getDefaultValue(), facet.getGender(id));
	}

	@Test
	public void testAvoidPollution()
	{
		PCTemplate pct = new PCTemplate();
		pct.put(ObjectKey.GENDER_LOCK, Gender.Neuter);
		tfacet.add(id, pct);
		assertEquals(Gender.getDefaultValue(), facet.getGender(altid));
	}

	@Test
	public void testGenderSet()
	{
		assertTrue(facet.canSetGender(id));
		facet.setGender(id, Gender.Female);
		assertTrue(facet.canSetGender(id));
		assertEquals(Gender.Female, facet.getGender(id));
		facet.removeGender(id);
		assertTrue(facet.canSetGender(id));
		assertEquals(Gender.getDefaultValue(), facet.getGender(id));
	}
	
	@Test
	public void testGenderLocked()
	{
		assertTrue(facet.canSetGender(id));
		PCTemplate pct = new PCTemplate();
		pct.put(ObjectKey.GENDER_LOCK, Gender.Female);
		tfacet.add(id, pct);
		assertFalse(facet.canSetGender(id));
		assertEquals(Gender.Female, facet.getGender(id));
		tfacet.remove(id, pct);
		assertTrue(facet.canSetGender(id));
		assertEquals(Gender.getDefaultValue(), facet.getGender(id));
	}

	@Test
	public void testGenderSetLockDominates()
	{
		facet.setGender(id, Gender.Female);
		assertEquals(Gender.Female, facet.getGender(id));
		PCTemplate pct = new PCTemplate();
		pct.put(ObjectKey.GENDER_LOCK, Gender.Neuter);
		tfacet.add(id, pct);
		assertEquals(Gender.Neuter, facet.getGender(id));
		tfacet.remove(id, pct);
		assertEquals(Gender.Female, facet.getGender(id));
	}

	@Test
	public void testMultipleGenderSetSecondDominatesGender()
	{
		PCTemplate pct = new PCTemplate();
		pct.put(ObjectKey.GENDER_LOCK, Gender.Neuter);
		tfacet.add(id, pct);
		assertEquals(Gender.Neuter, facet.getGender(id));
		PCTemplate pct2 = new PCTemplate();
		pct2.put(ObjectKey.GENDER_LOCK, Gender.Female);
		tfacet.add(id, pct2);
		assertEquals(Gender.Female, facet.getGender(id));
		tfacet.remove(id, pct);
		assertEquals(Gender.Female, facet.getGender(id));
		tfacet.add(id, pct);
		assertEquals(Gender.Neuter, facet.getGender(id));
		tfacet.remove(id, pct);
		assertEquals(Gender.Female, facet.getGender(id));
		tfacet.remove(id, pct2);
		assertEquals(Gender.getDefaultValue(), facet.getGender(id));
	}

}
