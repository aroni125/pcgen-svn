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
package plugin.lsttokens.pcclass;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.ChoiceSet;
import pcgen.cdom.base.ConcretePersistentTransitionChoice;
import pcgen.cdom.base.FormulaFactory;
import pcgen.cdom.base.PersistentTransitionChoice;
import pcgen.cdom.choiceset.ReferenceChoiceSet;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.list.ClassSkillList;
import pcgen.cdom.reference.CDOMDirectSingleRef;
import pcgen.core.PCClass;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;
import plugin.lsttokens.testsupport.ConsolidationRule;

public class SkillListTokenTest extends AbstractTokenTestCase<PCClass>
{
	static SkilllistToken token = new SkilllistToken();
	static CDOMTokenLoader<PCClass> loader = new CDOMTokenLoader<PCClass>(
			PCClass.class);

	@Override
	public Class<? extends PCClass> getCDOMClass()
	{
		return PCClass.class;
	}

	@Override
	public CDOMLoader<PCClass> getLoader()
	{
		return loader;
	}

	@Override
	public CDOMPrimaryToken<PCClass> getToken()
	{
		return token;
	}

	@Test
	public void testInvalidInputString() throws PersistenceLayerException
	{
		assertFalse(parse("String"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputOnlyNumber() throws PersistenceLayerException
	{
		assertFalse(parse("1"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputNaN() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		assertFalse(parse("NaN|TestWP1"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidNoObject() throws PersistenceLayerException
	{
		assertFalse(parse("1|"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidNoCount() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		assertFalse(parse("|TestWP1"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidDoublePipe() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		assertFalse(parse("1||TestWP1"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputType() throws PersistenceLayerException
	{
		try
		{
			assertFalse(parse("1|TYPE=Test"));
		}
		catch (IllegalArgumentException e)
		{
			// OK
		}
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputUnbuilt() throws PersistenceLayerException
	{
		assertTrue(parse("1|String"));
		assertConstructionError();
	}

	@Test
	public void testInvalidInputNoCount() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		assertFalse(parse("|TestWP1|TestWP2"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputCountNaN() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		assertFalse(parse("x|TestWP1|TestWP2"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputJoinedDot() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		assertTrue(parse("1|TestWP1.TestWP2"));
		assertConstructionError();
	}

	@Test
	public void testInvalidInputAnyItem() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		assertFalse(parse("1|ALL|TestWP1"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputItemAny() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		assertFalse(parse("1|TestWP1|ALL"));
		assertNoSideEffects();
	}

	@Test
	public void testInputInvalidAddsAllNoSideEffect()
			throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(secondaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		construct(secondaryContext, "TestWP2");
		construct(primaryContext, "TestWP3");
		construct(secondaryContext, "TestWP3");
		assertTrue(parse("1|TestWP1|TestWP2"));
		assertTrue(parseSecondary("1|TestWP1|TestWP2"));
		assertFalse(parse("1|TestWP3|ALL"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidListEnd() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		assertFalse(parse("1|TestWP1|"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidZeroCount() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		assertFalse(parse("0|TestWP1"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidNegativeCount() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		assertFalse(parse("-1|TestWP1|TestWP2"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidListDoubleJoin() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		assertFalse(parse("1|TestWP2||TestWP1"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputCheckMult() throws PersistenceLayerException
	{
		// Explicitly do NOT build TestWP2
		construct(primaryContext, "TestWP1");
		assertTrue(parse("1|TestWP1|TestWP2"));
		assertConstructionError();
	}

	@Test
	public void testRoundRobinOne() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		construct(secondaryContext, "TestWP1");
		construct(secondaryContext, "TestWP2");
		runRoundRobin("1|TestWP1");
	}

	@Test
	public void testRoundRobinAll() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(secondaryContext, "TestWP1");
		runRoundRobin("1|ALL");
	}

	@Test
	public void testRoundRobinThree() throws PersistenceLayerException
	{
		construct(primaryContext, "TestWP1");
		construct(primaryContext, "TestWP2");
		construct(primaryContext, "TestWP3");
		construct(secondaryContext, "TestWP1");
		construct(secondaryContext, "TestWP2");
		construct(secondaryContext, "TestWP3");
		runRoundRobin("2|TestWP1|TestWP2|TestWP3");
	}

	protected ClassSkillList construct(LoadContext loadContext, String one)
	{
		return loadContext.ref.constructCDOMObject(ClassSkillList.class, one);
	}

	@Override
	protected String getAlternateLegalValue()
	{
		return "2|TestWP1|TestWP3";
	}

	@Override
	protected String getLegalValue()
	{
		return "1|TestWP1|TestWP2";
	}

	@Override
	protected ConsolidationRule getConsolidationRule()
	{
		return ConsolidationRule.OVERWRITE;
	}

	protected PersistentTransitionChoice<ClassSkillList> buildChoice(
			CDOMReference<ClassSkillList>... refs)
	{
		ReferenceChoiceSet<ClassSkillList> rcs = buildRCS(refs);
		assertTrue(rcs.getGroupingState().isValid());
		return buildTC(rcs);
	}

	protected PersistentTransitionChoice<ClassSkillList> buildTC(
			ReferenceChoiceSet<ClassSkillList> rcs)
	{
		ChoiceSet<ClassSkillList> cs = new ChoiceSet<ClassSkillList>(getToken()
				.getTokenName(), rcs);
		cs.setTitle("Pick a ClassSkillList");
		PersistentTransitionChoice<ClassSkillList> tc = new ConcretePersistentTransitionChoice<ClassSkillList>(
				cs, FormulaFactory.ONE);
		return tc;
	}

	protected ReferenceChoiceSet<ClassSkillList> buildRCS(
			CDOMReference<ClassSkillList>... refs)
	{
		ReferenceChoiceSet<ClassSkillList> rcs = new ReferenceChoiceSet<ClassSkillList>(
				Arrays.asList(refs));
		return rcs;
	}

	@Test
	public void testUnparseNull() throws PersistenceLayerException
	{
		primaryProf.put(ObjectKey.CHOOSE_LANGAUTO, null);
		assertNull(getToken().unparse(primaryContext, primaryProf));
	}

	@Test
	public void testUnparseSingle() throws PersistenceLayerException
	{
		ClassSkillList wp1 = construct(primaryContext, "TestWP1");
		PersistentTransitionChoice<ClassSkillList> tc = buildChoice(CDOMDirectSingleRef
				.getRef(wp1));
		primaryProf.put(ObjectKey.SKILLLIST_CHOICE, tc);
		String[] unparsed = getToken().unparse(primaryContext, primaryProf);
		expectSingle(unparsed, "1|TestWP1");
	}

	@Test
	public void testUnparseBadCount() throws PersistenceLayerException
	{
		ClassSkillList wp1 = construct(primaryContext, "TestWP1");
		ReferenceChoiceSet<ClassSkillList> rcs = new ReferenceChoiceSet<ClassSkillList>(
				Collections.singletonList(CDOMDirectSingleRef.getRef(wp1)));
		ChoiceSet<ClassSkillList> cs = new ChoiceSet<ClassSkillList>(token.getTokenName(), rcs);
		cs.setTitle("Pick a ClassSkillList");
		PersistentTransitionChoice<ClassSkillList> tc1 = new ConcretePersistentTransitionChoice<ClassSkillList>(
				cs, null);
		primaryProf.put(ObjectKey.SKILLLIST_CHOICE, tc1);
		assertBadUnparse();
	}

	/*
	 * TODO Need to figure out who's responsibility this is!
	 */
	// @Test
	// public void testUnparseBadList() throws PersistenceLayerException
	// {
	// Language wp1 = construct(primaryContext, "TestWP1");
	// ReferenceChoiceSet<Language> rcs = buildRCS(CDOMDirectSingleRef
	// .getRef(wp1), primaryContext.ref
	// .getCDOMAllReference(getTargetClass()));
	// assertFalse(rcs.getGroupingState().isValid());
	// PersistentTransitionChoice<Language> tc = buildTC(rcs);
	// tc.setChoiceActor(subtoken);
	// primaryProf.put(ObjectKey.CHOOSE_LANGAUTO, tc);
	// assertBadUnparse();
	// }

	@Test
	public void testUnparseMultiple() throws PersistenceLayerException
	{
		ClassSkillList wp1 = construct(primaryContext, "TestWP1");
		ClassSkillList wp2 = construct(primaryContext, "TestWP2");
		PersistentTransitionChoice<ClassSkillList> tc = buildChoice(
				CDOMDirectSingleRef.getRef(wp1), CDOMDirectSingleRef
						.getRef(wp2));
		primaryProf.put(ObjectKey.SKILLLIST_CHOICE, tc);
		String[] unparsed = getToken().unparse(primaryContext, primaryProf);
		expectSingle(unparsed, "1|TestWP1|TestWP2");
	}

	@Test
	public void testUnparseNullInList() throws PersistenceLayerException
	{
		ClassSkillList wp1 = construct(primaryContext, "TestWP1");
		ReferenceChoiceSet<ClassSkillList> rcs = buildRCS(CDOMDirectSingleRef
				.getRef(wp1), null);
		PersistentTransitionChoice<ClassSkillList> tc = buildTC(rcs);
		primaryProf.put(ObjectKey.SKILLLIST_CHOICE, tc);
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
	public void testUnparseGenericsFail() throws PersistenceLayerException
	{
		ObjectKey objectKey = ObjectKey.SKILLLIST_CHOICE;
		primaryProf.put(objectKey, new Object());
		try
		{
			getToken().unparse(primaryContext, primaryProf);
			fail();
		}
		catch (ClassCastException e)
		{
			// Yep!
		}
	}
}
