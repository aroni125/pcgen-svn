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

public class HidetypeTokenTest extends AbstractTokenTestCase<Campaign>
{
	static HidetypeToken token = new HidetypeToken();
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
	public void testInvalidNoPipe() throws PersistenceLayerException
	{
		assertFalse(parse("NoPipe"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidTwoPipe() throws PersistenceLayerException
	{
		assertFalse(parse("One|Two|Three"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidDoublePipe() throws PersistenceLayerException
	{
		assertFalse(parse("Two||Pipe"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidEmpty() throws PersistenceLayerException
	{
		assertFalse(parse(""));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidOnlyPipe() throws PersistenceLayerException
	{
		assertFalse(parse("|"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidEmptyKey() throws PersistenceLayerException
	{
		assertFalse(parse("|Value"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidEmptyValue() throws PersistenceLayerException
	{
		assertFalse(parse("SKILL|"));
		assertNoSideEffects();
	}

	@Test
	public void testRoundRobinSkill() throws PersistenceLayerException
	{
		runRoundRobin("SKILL|QualityValue");
	}

	@Test
	public void testRoundRobinEquip() throws PersistenceLayerException
	{
		runRoundRobin("EQUIP|Quality Value");
	}

	@Test
	public void testRoundRobinFeat() throws PersistenceLayerException
	{
		runRoundRobin("FEAT|Niederösterreich|Finger Lakes");
	}

	@Override
	protected String getAlternateLegalValue()
	{
		return "SKILL|QualityValue";
	}

	@Override
	protected String getLegalValue()
	{
		return "EQUIP|Quality Value";
	}

	@Override
	protected ConsolidationRule getConsolidationRule()
	{
		return ConsolidationRule.SEPARATE;
	}
}
