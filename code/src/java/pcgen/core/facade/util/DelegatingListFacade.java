/*
 * DelegatingListFacade.java
 * Copyright 2010 Connor Petty <cpmeister@users.sourceforge.net>
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
 * 
 * Created on Apr 30, 2010, 3:26:19 PM
 */
package pcgen.core.facade.util;

import pcgen.core.facade.event.ListEvent;
import pcgen.core.facade.event.ListListener;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public class DelegatingListFacade<E> extends AbstractListFacade<E> implements ListListener<E>
{

	protected ListFacade<E> delegate = null;

	public E getElementAt(int index)
	{
		if (delegate == null)
		{
			throw new IllegalStateException();
		}
		return delegate.getElementAt(index);
	}

	public int getSize()
	{
		if (delegate == null)
		{
			return 0;
		}
		return delegate.getSize();
	}

	public void setDelegate(ListFacade<E> list)
	{
		if (delegate == null && list == null)
		{
			return;
		}
		ListFacade<E> oldList = this.delegate;
		if (oldList == list)
		{
			return;
		}
		if (oldList != null)
		{
			oldList.removeListListener(this);
		}
		this.delegate = list;
		if (list != null)
		{
			list.addListListener(this);
		}
		fireElementsChanged(this);
	}

	public void elementAdded(ListEvent<E> e)
	{
		fireElementAdded(this, e.getElement(), e.getIndex());
	}

	public void elementRemoved(ListEvent<E> e)
	{
		fireElementRemoved(this, e.getElement(), e.getIndex());
	}

	public void elementsChanged(ListEvent<E> e)
	{
		fireElementsChanged(this);
	}

}
