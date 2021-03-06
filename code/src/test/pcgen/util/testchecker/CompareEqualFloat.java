/**
 * CompareEqualFloat.java
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
 * Compare floats
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CompareEqualFloat extends TestChecker
{
	private float fl;

	/**
	 * Constructor
	 * @param fl
	 */
	public CompareEqualFloat(float fl)
	{
		this.fl = fl;
	}

	public boolean check(Object obj)
	{
		return obj.equals(Float.valueOf(this.fl));
	}

	public StringBuffer scribe(StringBuffer buf)
	{
		buf.append("a float ");
		buf.append(this.fl);
		return buf;
	}
}
