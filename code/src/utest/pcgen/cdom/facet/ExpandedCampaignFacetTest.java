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
package pcgen.cdom.facet;

import pcgen.cdom.testsupport.AbstractConsolidatingFacetTest;
import pcgen.core.Campaign;

public class ExpandedCampaignFacetTest extends
		AbstractConsolidatingFacetTest<Campaign>
{

	private ExpandedCampaignFacet facet = new ExpandedCampaignFacet();

	@Override
	protected AbstractSourcedListFacet<Campaign> getFacet()
	{
		return facet;
	}

	public static int n = 0;

	@Override
	protected Campaign getObject()
	{
		Campaign wp = new Campaign();
		wp.setName("WP" + n++);
		return wp;
	}

	@Override
	protected DataFacetChangeListener<Campaign> getListener()
	{
		return facet;
	}

}
