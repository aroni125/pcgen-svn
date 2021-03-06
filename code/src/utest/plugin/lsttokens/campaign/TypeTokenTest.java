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
package plugin.lsttokens.campaign;

import org.junit.Test;

import pcgen.core.Campaign;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;
import plugin.lsttokens.testsupport.ConsolidationRule;

public class TypeTokenTest extends AbstractTokenTestCase<Campaign>
{

	static TypeToken token = new TypeToken();
	static CDOMTokenLoader<Campaign> loader = new CDOMTokenLoader<Campaign>(
			Campaign.class);

	@Override
	public Class<Campaign> getCDOMClass()
	{
		return Campaign.class;
	}

	@Override
	public CDOMLoader<Campaign> getLoader()
	{
		return loader;
	}

	@Override
	public CDOMPrimaryToken<Campaign> getToken()
	{
		return token;
	}

	@Test
	public void dummyTest()
	{
		// Just to get Eclipse to recognize this as a JUnit 4.0 Test Case
	}

	@Test
	public void testInvalidListEmpty() throws PersistenceLayerException
	{
		assertFalse(parse("."));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidListTrailing() throws PersistenceLayerException
	{
		assertFalse(parse("Type."));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidListLeading() throws PersistenceLayerException
	{
		assertFalse(parse(".Type"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidListDouble() throws PersistenceLayerException
	{
		assertFalse(parse("One..Type"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidListTooMany() throws PersistenceLayerException
	{
		assertFalse(parse("One.Two.Three.Oops"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidEmpty() throws PersistenceLayerException
	{
		assertFalse(parse(""));
		assertNoSideEffects();
	}

	@Test
	public void testRoundRobinBase() throws PersistenceLayerException
	{
		runRoundRobin("Rheinhessen");
	}

	@Test
	public void testRoundRobinWithSpaceInternational() throws PersistenceLayerException
	{
		runRoundRobin("Finger Lakes.Niederösterreich");
	}

	@Test
	public void testRoundRobinHyphen() throws PersistenceLayerException
	{
		runRoundRobin("Languedoc-Roussillon.Two.Yarra Valley");
	}

	@Override
	protected String getAlternateLegalValue()
	{
		return "Finger Lakes.Niederösterreich";
	}

	@Override
	protected String getLegalValue()
	{
		return "Languedoc-Roussillon.Two.Yarra Valley";
	}

	@Override
	protected ConsolidationRule getConsolidationRule()
	{
		return ConsolidationRule.OVERWRITE;
	}
}
