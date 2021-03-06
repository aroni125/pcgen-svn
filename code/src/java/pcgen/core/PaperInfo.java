/*
 * PaperInfo.java
 * Copyright 2010 (C) Thomas Parker <thpr@users.sourceforge.net>
 * Copyright 2001 (C) Greg Bingleman <byngl@hotmail.com>
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
 * Created on February 25, 2002, 10:15 PM
 *
 * $Id$
 */
package pcgen.core;

import java.net.URI;

import pcgen.cdom.base.Loadable;

/**
 * The Paper information for output sheets
 *
 * @author Greg Bingleman <byngl@hotmail.com>
 * @version $Revision$
 */
public final class PaperInfo implements Loadable
{
	private URI sourceURI;
	private String infoName;

	/** Array of 6 paper information variables to keep hold of */
	private final String[] paperInfo = new String[7];

	public static final int NAME = 0;
	public static final int HEIGHT = 1;
	public static final int WIDTH = 2;
	public static final int TOPMARGIN = 3;
	public static final int BOTTOMMARGIN = 4;
	public static final int LEFTMARGIN = 5;
	public static final int RIGHTMARGIN = 6;

	/**
	 * Set a paper info item
	 * 
	 * @param infoType The type (key)
	 * @param info The value
	 */
	public void setPaperInfo(final int infoType, final String info)
	{
		if (!validIndex(infoType))
		{
			throw new IndexOutOfBoundsException("invalid index: " + infoType);
		}

		paperInfo[infoType] = info;
	}

	String getName()
	{
		return getPaperInfo(PaperInfo.NAME);
	}

	public String getPaperInfo(final int infoType)
	{
		if (!validIndex(infoType))
		{
			return null;
		}

		return paperInfo[infoType];
	}

	private static boolean validIndex(final int index)
	{
		switch (index)
		{
			case PaperInfo.NAME:
			case PaperInfo.HEIGHT:
			case PaperInfo.WIDTH:
			case PaperInfo.TOPMARGIN:
			case PaperInfo.BOTTOMMARGIN:
			case PaperInfo.LEFTMARGIN:
			case PaperInfo.RIGHTMARGIN:
				break;

			default:
				return false;
		}

		return true;
	}

	public URI getSourceURI()
	{
		return sourceURI;
	}

	public void setSourceURI(URI source)
	{
		sourceURI = source;
	}

	public void setName(String name)
	{
		infoName = name;
		paperInfo[0] = name;
	}

	public String getDisplayName()
	{
		return infoName;
	}

	public String getKeyName()
	{
		return getDisplayName();
	}

	public String getLSTformat()
	{
		return getDisplayName();
	}

	public boolean isInternal()
	{
		return false;
	}

	public boolean isType(String type)
	{
		return false;
	}

}
