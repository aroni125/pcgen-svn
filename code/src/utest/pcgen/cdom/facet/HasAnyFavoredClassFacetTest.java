/*
 * Copyright (c) 2010 Tom Parker <thpr@users.sourceforge.net>
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

import org.junit.Test;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.testsupport.AbstractExtractingFacetTest;
import pcgen.core.PCStat;
import pcgen.core.PCTemplate;
import pcgen.core.Race;

public class HasAnyFavoredClassFacetTest extends
		AbstractExtractingFacetTest<CDOMObject, Boolean>
{
	private HasAnyFavoredClassFacet facet = new HasAnyFavoredClassFacet();
	private Boolean[] target;
	private CDOMObject[] source;

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		CDOMObject cdo1 = new PCTemplate();
		cdo1.setName("Templ");
		CDOMObject cdo2 = new Race();
		cdo2.setName("Race");
		PCStat pcs1 = new PCStat();
		pcs1.setName("Stat1");
		PCStat pcs2 = new PCStat();
		pcs2.setName("Stat2");
		Boolean st1 = Boolean.TRUE;
		Boolean st2 = Boolean.FALSE;
		cdo1.put(ObjectKey.ANY_FAVORED_CLASS, st1);
		cdo2.put(ObjectKey.ANY_FAVORED_CLASS, st2);
		source = new CDOMObject[]{cdo1, cdo2};
		target = new Boolean[]{st1, st2};
	}

	@Override
	protected CDOMObject getContainingObject(int i)
	{
		return source[i];
	}

	@Override
	protected DataFacetChangeListener<CDOMObject> getListener()
	{
		return facet;
	}

	@Override
	protected Boolean getTargetObject(int i)
	{
		return target[i];
	}

	@Override
	protected AbstractSourcedListFacet<Boolean> getFacet()
	{
		return facet;
	}

	@Override
	protected Boolean getObject()
	{
		return Boolean.TRUE;
	}

	@Override
	protected Boolean getAltObject()
	{
		return Boolean.FALSE;
	}

	@Test
	public void testTypeAddAllSecondSource()
	{
		// TODO Override due to Boolean only having 2 values
	}

	@Test
	public void testTypeRemoveAllList()
	{
		// TODO Override due to Boolean only having 2 values
	}

	@Test
	public void testTypeRemoveAllSource()
	{
		// TODO Override due to Boolean only having 2 values
	}

	@Test
	public void testTypeGetSetSource()
	{
		// TODO Override due to Boolean only having 2 values
	}

	@Test
	public void testTypeContainsFrom()
	{
		// TODO Override due to Boolean only having 2 values
	}
}
