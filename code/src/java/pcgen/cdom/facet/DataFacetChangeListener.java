/*
 * Copyright (c) Thomas Parker, 2009.
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
package pcgen.cdom.facet;

import java.util.EventListener;

/**
 * @author Thomas Parker (thpr [at] yahoo.com)
 * 
 * The listener interface for receiving DataFacetChangeEvents. When a CDOMObject
 * has been added to or removed from a PlayerCharacter, the respective method in
 * the listener object is invoked, and the DataFacetChangeEvent is passed to it.
 */
public interface DataFacetChangeListener<T> extends EventListener
{
	/**
	 * Method called when a CDOMObject has been added to a PlayerCharacter and
	 * this DataFacetChangeListener has been added as a DataFacetChangeListener
	 * to the source DataFacet.
	 * 
	 * @param dfce
	 *            The DataFacetChangeEvent that occurred.
	 */
	public void dataAdded(DataFacetChangeEvent<T> dfce);

	/**
	 * Method called when a CDOMObject has been removed from a PlayerCharacter
	 * and this DataFacetChangeListener has been added as a
	 * DataFacetChangeListener to the source DataFacet.
	 * 
	 * @param dfce
	 *            The DataFacetChangeEvent that occurred.
	 */
	public void dataRemoved(DataFacetChangeEvent<T> dfce);
}