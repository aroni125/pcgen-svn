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
package pcgen.cdom.testsupport;

import java.util.Set;

import org.junit.Test;

import pcgen.cdom.facet.DataFacetChangeEvent;
import pcgen.cdom.facet.DataFacetChangeListener;

public abstract class AbstractConsolidatingFacetTest<T> extends
		AbstractSourcedListFacetTest<T>
{

	protected abstract DataFacetChangeListener<T> getListener();

	@Test
	public void testConTypeAddNullSource()
	{
		Object source = new Object();
		T t1 = getObject();
		DataFacetChangeEvent<T> dfce =
				new DataFacetChangeEvent<T>(id, t1, source,
					DataFacetChangeEvent.DATA_ADDED);
		getListener().dataAdded(dfce);
		assertEquals(1, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		assertNotNull(getFacet().getSet(id));
		assertEquals(1, getFacet().getSet(id).size());
		assertEquals(t1, getFacet().getSet(id).iterator().next());
		assertEventCount(1, 0);
		// No cross-pollution
		assertEquals(0, getFacet().getCount(altid));
		assertTrue(getFacet().isEmpty(altid));
		assertNotNull(getFacet().getSet(altid));
		assertTrue(getFacet().getSet(altid).isEmpty());
	}

	@Test
	public void testConTypeAddSingleGet()
	{
		Object source = new Object();
		T t1 = getObject();
		DataFacetChangeEvent<T> dfce =
				new DataFacetChangeEvent<T>(id, t1, source,
					DataFacetChangeEvent.DATA_ADDED);
		getListener().dataAdded(dfce);
		assertEquals(1, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		assertNotNull(getFacet().getSet(id));
		assertEquals(1, getFacet().getSet(id).size());
		assertEquals(t1, getFacet().getSet(id).iterator().next());
		assertEventCount(1, 0);
		// No cross-pollution
		assertEquals(0, getFacet().getCount(altid));
		assertTrue(getFacet().isEmpty(altid));
		assertNotNull(getFacet().getSet(altid));
		assertTrue(getFacet().getSet(altid).isEmpty());
	}

	@Test
	public void testConTypeAddSingleSourceTwiceGet()
	{
		Object source = new Object();
		T t1 = getObject();
		DataFacetChangeEvent<T> dfce =
				new DataFacetChangeEvent<T>(id, t1, source,
					DataFacetChangeEvent.DATA_ADDED);
		getListener().dataAdded(dfce);
		assertEquals(1, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		assertNotNull(getFacet().getSet(id));
		assertEquals(1, getFacet().getSet(id).size());
		assertEquals(t1, getFacet().getSet(id).iterator().next());
		assertEventCount(1, 0);
		// Add same, still only once in set (and only one event)
		dfce =
				new DataFacetChangeEvent<T>(id, t1, source,
					DataFacetChangeEvent.DATA_ADDED);
		getListener().dataAdded(dfce);
		assertEquals(1, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		assertNotNull(getFacet().getSet(id));
		assertEquals(1, getFacet().getSet(id).size());
		assertEquals(t1, getFacet().getSet(id).iterator().next());
		assertEventCount(1, 0);
	}

	@Test
	public void testConTypeAddSingleTwiceTwoSourceGet()
	{
		Object source = new Object();
		T t1 = getObject();
		DataFacetChangeEvent<T> dfce =
				new DataFacetChangeEvent<T>(id, t1, source,
					DataFacetChangeEvent.DATA_ADDED);
		getListener().dataAdded(dfce);
		assertEquals(1, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		assertNotNull(getFacet().getSet(id));
		assertEquals(1, getFacet().getSet(id).size());
		assertEquals(t1, getFacet().getSet(id).iterator().next());
		assertEventCount(1, 0);
		// Add same, still only once in set (and only one event)
		Object source2 = new Object();
		dfce =
				new DataFacetChangeEvent<T>(id, t1, source2,
					DataFacetChangeEvent.DATA_ADDED);
		getListener().dataAdded(dfce);
		assertEquals(1, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		assertNotNull(getFacet().getSet(id));
		assertEquals(1, getFacet().getSet(id).size());
		assertEquals(t1, getFacet().getSet(id).iterator().next());
		assertEventCount(1, 0);
	}

	@Test
	public void testConTypeAddMultGet()
	{
		Object source = new Object();
		T t1 = getObject();
		DataFacetChangeEvent<T> dfce =
				new DataFacetChangeEvent<T>(id, t1, source,
					DataFacetChangeEvent.DATA_ADDED);
		getListener().dataAdded(dfce);
		assertEquals(1, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		Set<T> setofone = getFacet().getSet(id);
		assertNotNull(setofone);
		assertEquals(1, setofone.size());
		assertEquals(t1, setofone.iterator().next());
		assertEventCount(1, 0);
		T t2 = getObject();
		dfce =
				new DataFacetChangeEvent<T>(id, t2, source,
					DataFacetChangeEvent.DATA_ADDED);
		getListener().dataAdded(dfce);
		assertEquals(2, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		Set<T> setoftwo = getFacet().getSet(id);
		assertNotNull(setoftwo);
		assertEquals(2, setoftwo.size());
		assertTrue(setoftwo.contains(t1));
		assertTrue(setoftwo.contains(t2));
		assertEventCount(2, 0);
	}

	@Test
	public void testConTypeContains()
	{
		Object source1 = new Object();
		T t1 = getObject();
		DataFacetChangeEvent<T> dfce =
				new DataFacetChangeEvent<T>(id, t1, source1,
					DataFacetChangeEvent.DATA_ADDED);
		assertFalse(getFacet().contains(id, t1));
		getListener().dataAdded(dfce);
		assertTrue(getFacet().contains(id, t1));
		dfce =
				new DataFacetChangeEvent<T>(id, t1, source1,
					DataFacetChangeEvent.DATA_REMOVED);
		getListener().dataRemoved(dfce);
		assertFalse(getFacet().contains(id, t1));
	}

	@Test
	public void testConTypeRemoveUselessSource()
	{
		Object source1 = new Object();
		T t1 = getObject();
		DataFacetChangeEvent<T> dfce =
				new DataFacetChangeEvent<T>(id, t1, source1,
					DataFacetChangeEvent.DATA_ADDED);
		getListener().dataAdded(dfce);
		assertEquals(1, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		assertNotNull(getFacet().getSet(id));
		assertEquals(1, getFacet().getSet(id).size());
		assertEquals(t1, getFacet().getSet(id).iterator().next());
		assertEventCount(1, 0);
		Object source2 = new Object();
		dfce =
				new DataFacetChangeEvent<T>(id, t1, source2,
					DataFacetChangeEvent.DATA_REMOVED);
		getListener().dataRemoved(dfce);
		// No change (wrong source)
		assertEquals(1, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		assertNotNull(getFacet().getSet(id));
		assertEquals(1, getFacet().getSet(id).size());
		assertEquals(t1, getFacet().getSet(id).iterator().next());
		assertEventCount(1, 0);
	}

	@Test
	public void testConTypeAddSingleRemove()
	{
		Object source1 = new Object();
		T t1 = getObject();
		DataFacetChangeEvent<T> dfce =
				new DataFacetChangeEvent<T>(id, t1, source1,
					DataFacetChangeEvent.DATA_ADDED);
		getListener().dataAdded(dfce);
		assertEquals(1, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		assertNotNull(getFacet().getSet(id));
		assertEquals(1, getFacet().getSet(id).size());
		assertEquals(t1, getFacet().getSet(id).iterator().next());
		assertEventCount(1, 0);
		// Remove
		dfce =
				new DataFacetChangeEvent<T>(id, t1, source1,
					DataFacetChangeEvent.DATA_REMOVED);
		getListener().dataRemoved(dfce);
		assertEquals(0, getFacet().getCount(id));
		assertTrue(getFacet().isEmpty(id));
		assertNotNull(getFacet().getSet(id));
		assertTrue(getFacet().getSet(id).isEmpty());
		assertEventCount(1, 1);
	}

	@Test
	public void testConTypeAddUselessRemove()
	{
		Object source1 = new Object();
		T t1 = getObject();
		DataFacetChangeEvent<T> dfce =
				new DataFacetChangeEvent<T>(id, t1, source1,
					DataFacetChangeEvent.DATA_ADDED);
		getListener().dataAdded(dfce);
		assertEquals(1, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		assertNotNull(getFacet().getSet(id));
		assertEquals(1, getFacet().getSet(id).size());
		assertEquals(t1, getFacet().getSet(id).iterator().next());
		assertEventCount(1, 0);
		// Useless Remove
		dfce =
				new DataFacetChangeEvent<T>(id, getAltObject(), source1,
					DataFacetChangeEvent.DATA_REMOVED);
		getListener().dataRemoved(dfce);
		assertEquals(1, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		assertNotNull(getFacet().getSet(id));
		assertEquals(1, getFacet().getSet(id).size());
		assertEquals(t1, getFacet().getSet(id).iterator().next());
		assertEventCount(1, 0);
	}

	@Test
	public void testConTypeAddSingleTwiceRemove()
	{
		Object source1 = new Object();
		T t1 = getObject();
		DataFacetChangeEvent<T> dfce =
				new DataFacetChangeEvent<T>(id, t1, source1,
					DataFacetChangeEvent.DATA_ADDED);
		getListener().dataAdded(dfce);
		assertEquals(1, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		assertNotNull(getFacet().getSet(id));
		assertEquals(1, getFacet().getSet(id).size());
		assertEquals(t1, getFacet().getSet(id).iterator().next());
		assertEventCount(1, 0);
		// Add same, still only once in set (but twice on that source)
		dfce =
				new DataFacetChangeEvent<T>(id, t1, source1,
					DataFacetChangeEvent.DATA_ADDED);
		getListener().dataAdded(dfce);
		assertEquals(1, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		assertNotNull(getFacet().getSet(id));
		assertEquals(1, getFacet().getSet(id).size());
		assertEquals(t1, getFacet().getSet(id).iterator().next());
		assertEventCount(1, 0);
		// Only one Remove required to clear (source Set not source List)
		dfce =
				new DataFacetChangeEvent<T>(id, t1, source1,
					DataFacetChangeEvent.DATA_REMOVED);
		getListener().dataRemoved(dfce);
		testTypeUnsetZeroCount();
		testTypeUnsetEmpty();
		testTypeUnsetEmptySet();
		assertEventCount(1, 1);
		// Second remove useless
		dfce =
				new DataFacetChangeEvent<T>(id, t1, source1,
					DataFacetChangeEvent.DATA_REMOVED);
		getListener().dataRemoved(dfce);
		testTypeUnsetZeroCount();
		testTypeUnsetEmpty();
		testTypeUnsetEmptySet();
		assertEventCount(1, 1);
	}

	@Test
	public void testConTypeAddMultRemove()
	{
		Object source1 = new Object();
		T t1 = getObject();
		DataFacetChangeEvent<T> dfce =
				new DataFacetChangeEvent<T>(id, t1, source1,
					DataFacetChangeEvent.DATA_ADDED);
		getListener().dataAdded(dfce);
		T t2 = getObject();
		dfce =
				new DataFacetChangeEvent<T>(id, t2, source1,
					DataFacetChangeEvent.DATA_ADDED);
		getListener().dataAdded(dfce);
		dfce =
				new DataFacetChangeEvent<T>(id, t1, source1,
					DataFacetChangeEvent.DATA_REMOVED);
		getListener().dataRemoved(dfce);
		assertEquals(1, getFacet().getCount(id));
		assertFalse(getFacet().isEmpty(id));
		Set<T> setofone = getFacet().getSet(id);
		assertNotNull(setofone);
		assertEquals(1, setofone.size());
		assertTrue(setofone.contains(t2));
		assertEventCount(2, 1);
	}

}
