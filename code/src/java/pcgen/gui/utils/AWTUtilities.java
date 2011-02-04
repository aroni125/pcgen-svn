/*
 * Copyright (c) 2006, 2009.
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
 * 
 * Created on Mar 10, 2006
 */
package pcgen.gui.utils;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;

public final class AWTUtilities
{

	private AWTUtilities()
	{
		// Can't instantiate
	}

	public static Point computeWindowLocation(Window window)
	{
		Dimension scrnSz = window.getToolkit().getScreenSize();
		Dimension windowSize = window.getPreferredSize();

		int x = (scrnSz.width - windowSize.width) / 2;
		int y = (scrnSz.height - windowSize.height) / 2;

		return new Point(x, y);
	}

}
