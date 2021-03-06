/**
 * CompareEqualInt.java
 * Copyright 2005 (c) Andrew Wilson <nuance@sourceforge.net>
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
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 */

package pcgen.util.testchecker;

import pcgen.util.TestChecker;

/**
 * Compare ints
 */
public class CompareEqualInt extends TestChecker
{
	private int i;

	/**
	 * Constructor
	 * @param i
	 */
	public CompareEqualInt(int i)
	{
		this.i = i;
	}

	public boolean check(Object obj)
	{
		return obj.equals(Integer.valueOf(this.i));
	}

	public StringBuffer scribe(StringBuffer buf)
	{
		buf.append("an int ");
		buf.append(this.i);
		return buf;
	}
}
