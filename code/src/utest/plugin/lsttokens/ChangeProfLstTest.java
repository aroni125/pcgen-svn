/*
 * Copyright (c) 2007 Tom Parker <thpr@users.sourceforge.net>
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */
package plugin.lsttokens;

import org.junit.Test;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.Type;
import pcgen.core.PCTemplate;
import pcgen.core.WeaponProf;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractGlobalTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;
import plugin.lsttokens.testsupport.ConsolidationRule;

public class ChangeProfLstTest extends AbstractGlobalTokenTestCase
{

	static CDOMPrimaryToken<CDOMObject> token = new ChangeprofLst();
	static CDOMTokenLoader<PCTemplate> loader = new CDOMTokenLoader<PCTemplate>(
			PCTemplate.class);

	@Override
	public CDOMLoader<PCTemplate> getLoader()
	{
		return loader;
	}

	@Override
	public Class<PCTemplate> getCDOMClass()
	{
		return PCTemplate.class;
	}

	@Override
	public CDOMPrimaryToken<CDOMObject> getToken()
	{
		return token;
	}

	@Test
	public void testInvalidEmpty() throws PersistenceLayerException
	{
		assertFalse(parse(""));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidSourceOnly() throws PersistenceLayerException
	{
		assertFalse(parse("Hammer"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidSourceEqualOnly() throws PersistenceLayerException
	{
		assertFalse(parse("Hammer="));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidSourceEqualOnlyTypeTwo()
			throws PersistenceLayerException
	{
		assertFalse(parse("Hammer=Martial|Pipe="));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidEmptySource() throws PersistenceLayerException
	{
		assertFalse(parse("=Martial"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidTwoEquals() throws PersistenceLayerException
	{
		assertFalse(parse("Hammer==Martial"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidTwoEqualsTypeTwo() throws PersistenceLayerException
	{
		assertFalse(parse("Hammer=TYPE.Heavy=Martial"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidBarEnding() throws PersistenceLayerException
	{
		assertFalse(parse("Hammer=Martial|"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidBarStarting() throws PersistenceLayerException
	{
		assertFalse(parse("|Hammer=Martial"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidDoublePipe() throws PersistenceLayerException
	{
		assertFalse(parse("Hammer=Martial||Pipe=Exotic"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidReversed() throws PersistenceLayerException
	{
		assertTrue(parse("Martial=Hammer"));
		assertConstructionError();
	}

	@Test
	public void testInvalidResultPrimitive() throws PersistenceLayerException
	{
		assertTrue(parse("Hammer=Pipe"));
		assertConstructionError();
	}

	@Test
	public void testInvalidResultType() throws PersistenceLayerException
	{
		try
		{
			assertFalse(parse("Hammer=TYPE.Heavy"));
		}
		catch (IllegalArgumentException e)
		{
			// This is okay too
		}
		assertNoSideEffects();
	}

	@Test
	public void testRoundRobinSimple() throws PersistenceLayerException
	{
		primaryContext.ref.constructCDOMObject(WeaponProf.class, "Hammer");
		secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Hammer");
		WeaponProf a = primaryContext.ref.constructCDOMObject(WeaponProf.class, "Longsword");
		a.addToListFor(ListKey.TYPE, Type.getConstant("Martial"));
		WeaponProf b = secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Longsword");
		b.addToListFor(ListKey.TYPE, Type.getConstant("Martial"));
		runRoundRobin("Hammer=Martial");
	}

	@Test
	public void testRoundRobinTwo() throws PersistenceLayerException
	{
		primaryContext.ref.constructCDOMObject(WeaponProf.class, "Hammer");
		secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Hammer");
		primaryContext.ref.constructCDOMObject(WeaponProf.class, "Pipe");
		secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Pipe");
		WeaponProf a = primaryContext.ref.constructCDOMObject(WeaponProf.class, "Longsword");
		a.addToListFor(ListKey.TYPE, Type.getConstant("Martial"));
		WeaponProf b = secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Longsword");
		b.addToListFor(ListKey.TYPE, Type.getConstant("Martial"));
		runRoundRobin("Hammer,Pipe=Martial");
	}

	@Test
	public void testRoundRobinType() throws PersistenceLayerException
	{
		primaryContext.ref.constructCDOMObject(WeaponProf.class, "Hammer");
		secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Hammer");
		WeaponProf a = primaryContext.ref.constructCDOMObject(WeaponProf.class, "Longsword");
		a.addToListFor(ListKey.TYPE, Type.getConstant("Martial"));
		WeaponProf b = secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Longsword");
		b.addToListFor(ListKey.TYPE, Type.getConstant("Martial"));
		WeaponProf c = primaryContext.ref.constructCDOMObject(WeaponProf.class, "Sledgehammer");
		c.addToListFor(ListKey.TYPE, Type.getConstant("Heavy"));
		WeaponProf d = secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Sledgehammer");
		d.addToListFor(ListKey.TYPE, Type.getConstant("Heavy"));
		runRoundRobin("Hammer,TYPE.Heavy=Martial");
	}

	@Test
	public void testRoundRobinTwoResult() throws PersistenceLayerException
	{
		primaryContext.ref.constructCDOMObject(WeaponProf.class, "Hammer");
		secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Hammer");
		primaryContext.ref.constructCDOMObject(WeaponProf.class, "Pipe");
		secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Pipe");
		WeaponProf a = primaryContext.ref.constructCDOMObject(WeaponProf.class, "Longsword");
		a.addToListFor(ListKey.TYPE, Type.getConstant("Martial"));
		WeaponProf b = secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Longsword");
		b.addToListFor(ListKey.TYPE, Type.getConstant("Martial"));
		WeaponProf c = primaryContext.ref.constructCDOMObject(WeaponProf.class, "Bolas");
		c.addToListFor(ListKey.TYPE, Type.getConstant("Exotic"));
		WeaponProf d = secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Bolas");
		d.addToListFor(ListKey.TYPE, Type.getConstant("Exotic"));
		runRoundRobin("Hammer=Martial|Pipe=Exotic");
	}

	@Test
	public void testRoundRobinComplex() throws PersistenceLayerException
	{
		primaryContext.ref.constructCDOMObject(WeaponProf.class, "Hammer");
		secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Hammer");
		primaryContext.ref.constructCDOMObject(WeaponProf.class, "Nail");
		secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Nail");
		WeaponProf a = primaryContext.ref.constructCDOMObject(WeaponProf.class, "Longsword");
		a.addToListFor(ListKey.TYPE, Type.getConstant("Martial"));
		WeaponProf b = secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Longsword");
		b.addToListFor(ListKey.TYPE, Type.getConstant("Martial"));
		WeaponProf c = primaryContext.ref.constructCDOMObject(WeaponProf.class, "Bolas");
		c.addToListFor(ListKey.TYPE, Type.getConstant("Exotic"));
		WeaponProf d = secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Bolas");
		d.addToListFor(ListKey.TYPE, Type.getConstant("Exotic"));
		WeaponProf e = primaryContext.ref.constructCDOMObject(WeaponProf.class, "Sledgehammer");
		e.addToListFor(ListKey.TYPE, Type.getConstant("Heavy"));
		WeaponProf f = secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Sledgehammer");
		f.addToListFor(ListKey.TYPE, Type.getConstant("Heavy"));
		WeaponProf g = primaryContext.ref.constructCDOMObject(WeaponProf.class, "Average Weapon");
		g.addToListFor(ListKey.TYPE, Type.getConstant("Medium"));
		WeaponProf h = secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Average Weapon");
		h.addToListFor(ListKey.TYPE, Type.getConstant("Medium"));
		WeaponProf k = primaryContext.ref.constructCDOMObject(WeaponProf.class, "Loaded Diaper");
		k.addToListFor(ListKey.TYPE, Type.getConstant("Disposable"));
		k.addToListFor(ListKey.TYPE, Type.getConstant("Crazy"));
		WeaponProf l = secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Loaded Diaper");
		l.addToListFor(ListKey.TYPE, Type.getConstant("Crazy"));
		l.addToListFor(ListKey.TYPE, Type.getConstant("Disposable"));
		runRoundRobin("Hammer,TYPE.Heavy,TYPE.Medium=Martial|Nail,TYPE.Crazy,TYPE.Disposable=Exotic");
	}

	@Override
	protected String getLegalValue()
	{
		// TODO What happens in consolidation of ChangeProf if Wand is reused?
		// What "wins"?
		return "Pipe=Martial";// |Wand=Exotic";
	}

	@Override
	protected String getAlternateLegalValue()
	{
		return "Hammer,Wand,TYPE.Heavy,TYPE.Medium=Martial|Nail,TYPE.Crazy,TYPE.Disposable=Exotic";
	}

	@Override
	protected ConsolidationRule getConsolidationRule()
	{
		return new ConsolidationRule()
		{
			public String[] getAnswer(String... strings)
			{
				return new String[] { "Hammer,Pipe,Wand,TYPE.Heavy,TYPE.Medium=Martial|Nail,TYPE.Crazy,TYPE.Disposable=Exotic" };
			}
		};
	}
}