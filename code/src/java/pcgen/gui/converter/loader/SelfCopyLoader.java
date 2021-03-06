/*
 * Copyright (c) 2009 Tom Parker <thpr@users.sourceforge.net>
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
package pcgen.gui.converter.loader;

import java.util.Collections;
import java.util.List;

import pcgen.cdom.base.CDOMObject;
import pcgen.core.Campaign;
import pcgen.gui.converter.ConversionDecider;
import pcgen.gui.converter.Loader;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.lst.CampaignSourceEntry;

public class SelfCopyLoader implements Loader
{

	public List<CDOMObject> process(StringBuilder sb, int line,
			String lineString, ConversionDecider decider)
			throws PersistenceLayerException, InterruptedException
	{
		sb.append(lineString);
		return null;
	}

	public List<CampaignSourceEntry> getFiles(Campaign c)
	{
		return Collections.singletonList(new CampaignSourceEntry(c, c
				.getSourceURI()));
	}

	public String getLoadName()
	{
		return "Self";
	}

}
