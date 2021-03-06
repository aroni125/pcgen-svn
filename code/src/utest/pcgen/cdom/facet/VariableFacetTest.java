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

import junit.framework.TestCase;

import org.junit.Test;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.FormulaFactory;
import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.VariableKey;
import pcgen.core.PCTemplate;

public class VariableFacetTest extends TestCase
{

	private VariableFacet facet = new VariableFacet();
	private CharID id;
	private CharID altid;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		id = new CharID();
		altid = new CharID();
	}

	@Test
	public void testAddEmptyObject()
	{
		Object source = new Object();
		CDOMObject t1 = new PCTemplate();
		DataFacetChangeEvent<CDOMObject> dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t1, source,
					DataFacetChangeEvent.DATA_ADDED);
		getFacet().dataAdded(dfce);
		VariableKey vk = VariableKey.getConstant("Var1");
		assertFalse(getFacet().contains(id, vk));
	}

	@Test
	public void testAddSimple()
	{
		Object source = new Object();
		CDOMObject t1 = new PCTemplate();
		VariableKey vk = VariableKey.getConstant("Var1");
		t1.put(vk, FormulaFactory.getFormulaFor(2));
		DataFacetChangeEvent<CDOMObject> dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t1, source,
					DataFacetChangeEvent.DATA_ADDED);
		getFacet().dataAdded(dfce);
		assertTrue(getFacet().contains(id, vk));
		assertFalse(getFacet().contains(altid, vk));
		assertFalse(getFacet().contains(id, VariableKey.getConstant("Var2")));
	}

	@Test
	public void testAddSameTwice()
	{
		Object source = new Object();
		CDOMObject t1 = new PCTemplate();
		CDOMObject t2 = new PCTemplate();
		VariableKey vk = VariableKey.getConstant("Var1");
		t1.put(vk, FormulaFactory.getFormulaFor(2));
		t2.put(vk, FormulaFactory.getFormulaFor(4));
		DataFacetChangeEvent<CDOMObject> dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t1, source,
					DataFacetChangeEvent.DATA_ADDED);
		getFacet().dataAdded(dfce);
		assertTrue(getFacet().contains(id, vk));
		assertFalse(getFacet().contains(altid, vk));
		assertFalse(getFacet().contains(id, VariableKey.getConstant("Var2")));
		dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t2, source,
					DataFacetChangeEvent.DATA_ADDED);
		getFacet().dataAdded(dfce);
		assertTrue(getFacet().contains(id, vk));
		assertFalse(getFacet().contains(altid, vk));
		assertFalse(getFacet().contains(id, VariableKey.getConstant("Var2")));
	}

	@Test
	public void testAddSameTwo()
	{
		Object source = new Object();
		CDOMObject t1 = new PCTemplate();
		VariableKey vk1 = VariableKey.getConstant("Var1");
		VariableKey vk2 = VariableKey.getConstant("Var2");
		t1.put(vk1, FormulaFactory.getFormulaFor(2));
		t1.put(vk2, FormulaFactory.getFormulaFor(4));
		DataFacetChangeEvent<CDOMObject> dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t1, source,
					DataFacetChangeEvent.DATA_ADDED);
		getFacet().dataAdded(dfce);
		assertTrue(getFacet().contains(id, vk1));
		assertTrue(getFacet().contains(id, vk2));
		assertFalse(getFacet().contains(altid, vk1));
		assertFalse(getFacet().contains(id, VariableKey.getConstant("Var3")));
	}

	@Test
	public void testAddRemove()
	{
		Object source = new Object();
		CDOMObject t1 = new PCTemplate();
		VariableKey vk1 = VariableKey.getConstant("Var1");
		VariableKey vk2 = VariableKey.getConstant("Var2");
		t1.put(vk1, FormulaFactory.getFormulaFor(2));
		t1.put(vk2, FormulaFactory.getFormulaFor(4));
		DataFacetChangeEvent<CDOMObject> dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t1, source,
					DataFacetChangeEvent.DATA_ADDED);
		getFacet().dataAdded(dfce);
		assertTrue(getFacet().contains(id, vk1));
		assertTrue(getFacet().contains(id, vk2));
		assertFalse(getFacet().contains(altid, vk1));
		assertFalse(getFacet().contains(id, VariableKey.getConstant("Var3")));
		dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t1, source,
					DataFacetChangeEvent.DATA_REMOVED);
		getFacet().dataRemoved(dfce);
		assertFalse(getFacet().contains(id, vk1));
		assertFalse(getFacet().contains(id, vk2));
		assertFalse(getFacet().contains(altid, vk1));
		assertFalse(getFacet().contains(id, VariableKey.getConstant("Var3")));
	}

	@Test
	public void testUselessRemove()
	{
		Object source = new Object();
		CDOMObject t1 = new PCTemplate();
		VariableKey vk1 = VariableKey.getConstant("Var1");
		DataFacetChangeEvent<CDOMObject> dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t1, source,
					DataFacetChangeEvent.DATA_REMOVED);
		getFacet().dataRemoved(dfce);
		assertFalse(getFacet().contains(id, vk1));
	}

	@Test
	public void testAddRemove1First()
	{
		Object source = new Object();
		CDOMObject t1 = new PCTemplate();
		CDOMObject t2 = new PCTemplate();
		VariableKey vk = VariableKey.getConstant("Var1");
		t1.put(vk, FormulaFactory.getFormulaFor(2));
		t2.put(vk, FormulaFactory.getFormulaFor(4));
		DataFacetChangeEvent<CDOMObject> dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t1, source,
					DataFacetChangeEvent.DATA_ADDED);
		getFacet().dataAdded(dfce);
		assertTrue(getFacet().contains(id, vk));
		assertFalse(getFacet().contains(altid, vk));
		assertFalse(getFacet().contains(id, VariableKey.getConstant("Var2")));
		dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t2, source,
					DataFacetChangeEvent.DATA_ADDED);
		getFacet().dataAdded(dfce);
		assertTrue(getFacet().contains(id, vk));
		assertFalse(getFacet().contains(altid, vk));
		assertFalse(getFacet().contains(id, VariableKey.getConstant("Var2")));
		dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t1, source,
					DataFacetChangeEvent.DATA_REMOVED);
		getFacet().dataRemoved(dfce);
		assertTrue(getFacet().contains(id, vk));
		dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t2, source,
					DataFacetChangeEvent.DATA_REMOVED);
		getFacet().dataRemoved(dfce);
		assertFalse(getFacet().contains(id, vk));
	}

	@Test
	public void testAddRemove2First()
	{
		Object source = new Object();
		CDOMObject t1 = new PCTemplate();
		CDOMObject t2 = new PCTemplate();
		VariableKey vk = VariableKey.getConstant("Var1");
		t1.put(vk, FormulaFactory.getFormulaFor(2));
		t2.put(vk, FormulaFactory.getFormulaFor(4));
		DataFacetChangeEvent<CDOMObject> dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t1, source,
					DataFacetChangeEvent.DATA_ADDED);
		getFacet().dataAdded(dfce);
		assertTrue(getFacet().contains(id, vk));
		assertFalse(getFacet().contains(altid, vk));
		assertFalse(getFacet().contains(id, VariableKey.getConstant("Var2")));
		dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t2, source,
					DataFacetChangeEvent.DATA_ADDED);
		getFacet().dataAdded(dfce);
		assertTrue(getFacet().contains(id, vk));
		assertFalse(getFacet().contains(altid, vk));
		assertFalse(getFacet().contains(id, VariableKey.getConstant("Var2")));
		dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t2, source,
					DataFacetChangeEvent.DATA_REMOVED);
		getFacet().dataRemoved(dfce);
		assertTrue(getFacet().contains(id, vk));
		dfce =
				new DataFacetChangeEvent<CDOMObject>(id, t1, source,
					DataFacetChangeEvent.DATA_REMOVED);
		getFacet().dataRemoved(dfce);
		assertFalse(getFacet().contains(id, vk));
	}

	private VariableFacet getFacet()
	{
		return facet;
	}
}
