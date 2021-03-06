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
package plugin.lsttokens.testsupport;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.SpecialProperty;
import pcgen.persistence.PersistenceLayerException;
import plugin.pretokens.parser.PreClassParser;
import plugin.pretokens.parser.PreLevelParser;
import plugin.pretokens.writer.PreClassWriter;
import plugin.pretokens.writer.PreLevelWriter;

public abstract class AbstractTextPropertyTokenTestCase<T extends CDOMObject>
		extends AbstractTokenTestCase<T>
{

	private static boolean classSetUpFired = false;

	@BeforeClass
	public static final void localClassSetUp() throws URISyntaxException,
			PersistenceLayerException
	{
		TokenRegistration.register(new PreLevelParser());
		TokenRegistration.register(new PreClassParser());
		TokenRegistration.register(new PreLevelWriter());
		TokenRegistration.register(new PreClassWriter());
		classSetUpFired = true;
	}

	@Override
	@Before
	public void setUp() throws PersistenceLayerException, URISyntaxException
	{
		super.setUp();
		if (!classSetUpFired)
		{
			localClassSetUp();
		}
	}

	@Test
	public void testInvalidEmpty() throws PersistenceLayerException
	{
		assertFalse(parse(""));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidPipeOnly() throws PersistenceLayerException
	{
		assertFalse(parse("|"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidEndsPipe() throws PersistenceLayerException
	{
		assertFalse(parse("Yarra Valley|"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidStartsPipe() throws PersistenceLayerException
	{
		assertFalse(parse("|Yarra Valley"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidDoublePipe() throws PersistenceLayerException
	{
		assertFalse(parse("Yarra Valley||Rheinhessen"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidOnlyPre() throws PersistenceLayerException
	{
		assertFalse(parse("!PRELEVEL:MIN=3"));
		assertNoSideEffects();
	}

	// TODO Special Property allowed this :(
	// @Test
	// public void testInvalidEmbeddedNotPre() throws PersistenceLayerException
	// {
	// assertFalse(parse("Yarra Valley|!PRELEVEL:MIN=3|Rheinhessen"));
	// assertNoSideEffects();
	// }

	@Test
	public void testInvalidBadPre() throws PersistenceLayerException
	{
		assertFalse(parse("Yarra Valley|Rheinhessen|PREFOO:3"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidNotBadPre() throws PersistenceLayerException
	{
		assertFalse(parse("Yarra Valley|Rheinhessen|!PREFOO:3"));
		assertNoSideEffects();
	}

	// TODO Special Property allowed this :(
	// @Test
	// public void testInvalidEmbeddedPre() throws PersistenceLayerException
	// {
	// assertFalse(parse("Yarra Valley|PRELEVEL:MIN=4|Rheinhessen"));
	// assertNoSideEffects();
	// }

	@Test
	public void testRoundRobinBase() throws PersistenceLayerException
	{
		runRoundRobin("Rheinhessen");
	}

	@Test
	public void testRoundRobinWithSpace() throws PersistenceLayerException
	{
		runRoundRobin("Finger Lakes");
	}

	@Test
	public void testRoundRobinNonEnglishAndN() throws PersistenceLayerException
	{
		runRoundRobin("Niederösterreich");
	}

	@Test
	public void testRoundRobinHyphen() throws PersistenceLayerException
	{
		runRoundRobin("Languedoc-Roussillon");
	}

	@Test
	public void testRoundRobinY() throws PersistenceLayerException
	{
		runRoundRobin("Yarra Valley");
	}

	@Test
	public void testRoundRobinVariable() throws PersistenceLayerException
	{
		runRoundRobin("Rheinhessen|Variable");
	}

	@Test
	public void testRoundRobinThreeVariable() throws PersistenceLayerException
	{
		runRoundRobin("Rheinhessen|VarOne|VarTwo|VarThree");
	}

	@Test
	public void testRoundRobinPre() throws PersistenceLayerException
	{
		runRoundRobin("Rheinhessen|VarOne|VarTwo|PRELEVEL:MIN=5");
	}

	@Test
	public void testRoundRobinNotPre() throws PersistenceLayerException
	{
		runRoundRobin("Rheinhessen|VarOne|VarTwo|!PRELEVEL:MIN=5");
	}

	@Test
	public void testRoundRobinDoublePre() throws PersistenceLayerException
	{
		runRoundRobin("Rheinhessen|VarOne|VarTwo|PRECLASS:1,Fighter=1|PRELEVEL:MIN=5");
	}

	@Override
	protected String getAlternateLegalValue()
	{
		return "Yarra Valley";
	}

	@Override
	protected String getLegalValue()
	{
		return "Rheinhessen|VarOne|VarTwo|PRELEVEL:MIN=5";
	}

	@Test
	public void testUnparseNull() throws PersistenceLayerException
	{
		primaryProf.removeListFor(getListKey());
		assertNull(getToken().unparse(primaryContext, primaryProf));
	}

	@Test
	public void testUnparseSingle() throws PersistenceLayerException
	{
		primaryProf.addToListFor(getListKey(), getConstant(getLegalValue()));
		String[] unparsed = getToken().unparse(primaryContext, primaryProf);
		expectSingle(unparsed, getLegalValue());
	}

	private SpecialProperty getConstant(String value)
	{
		return SpecialProperty.createFromLst(value);
	}

	@Test
	public void testUnparseNullInList() throws PersistenceLayerException
	{
		primaryProf.addToListFor(getListKey(), null);
		try
		{
			getToken().unparse(primaryContext, primaryProf);
			fail();
		}
		catch (NullPointerException e)
		{
			// Yep!
		}
	}

	@Test
	public void testUnparseMultiple() throws PersistenceLayerException
	{
		primaryProf.addToListFor(getListKey(), getConstant(getLegalValue()));
		primaryProf.addToListFor(getListKey(),
				getConstant(getAlternateLegalValue()));
		String[] unparsed = getToken().unparse(primaryContext, primaryProf);
		assertNotNull(unparsed);
		assertEquals(2, unparsed.length);
		List<String> upList = Arrays.asList(unparsed);
		assertTrue(upList.contains(getLegalValue()));
		assertTrue(upList.contains(getAlternateLegalValue()));
	}

	private ListKey<SpecialProperty> getListKey()
	{
		return ListKey.SPECIAL_PROPERTIES;
	}

	/*
	 * TODO Need to define the appropriate behavior here - is this the token's
	 * responsibility?
	 */
	@Test
	public void testUnparseGenericsFail() throws PersistenceLayerException
	{
		ListKey objectKey = getListKey();
		primaryProf.addToListFor(objectKey, new Object());
		try
		{
			String[] unparsed = getToken().unparse(primaryContext, primaryProf);
			fail();
		}
		catch (ClassCastException e)
		{
			// Yep!
		}
	}

	@Override
	protected ConsolidationRule getConsolidationRule()
	{
		return ConsolidationRule.SEPARATE;
	}
}
