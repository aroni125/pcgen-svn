/*
 * Copyright (c) 2010 Tom Parker <thpr@users.sourceforge.net>
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

import java.util.List;

import pcgen.cdom.content.ACControl;
import pcgen.cdom.enumeration.CharID;
import pcgen.core.PlayerCharacter;
import pcgen.core.SettingsHandler;
import pcgen.io.exporttoken.BonusToken;
import pcgen.util.Logging;

public class ArmorClassFacet
{

	private PrerequisiteFacet prerequisiteFacet;

	private PlayerCharacterTrackingFacet trackingFacet = FacetLibrary
			.getFacet(PlayerCharacterTrackingFacet.class);

	public int calcACOfType(CharID id, String type)
	{
		final List<ACControl> addList = SettingsHandler.getGame()
				.getACTypeAddString(type);
		final List<ACControl> removeList = SettingsHandler.getGame()
				.getACTypeRemoveString(type);

		if ((addList == null) && (removeList == null))
		{
			Logging.errorPrint("Invalid ACType: " + type);
			return 0;
		}

		int armorClass = 0;

		if (addList != null)
		{
			PlayerCharacter pc = trackingFacet.getPC(id);
			for (ACControl acc : addList)
			{
				if (prerequisiteFacet.qualifies(id, acc, null))
				{
					armorClass += Integer.parseInt(BonusToken.getBonusToken(
							"BONUS.COMBAT.AC." + acc.getType(), pc));
				}
			}
		}

		if (removeList != null)
		{
			PlayerCharacter pc = trackingFacet.getPC(id);
			for (ACControl acc : removeList)
			{
				if (prerequisiteFacet.qualifies(id, acc, null))
				{
					armorClass -= Integer.parseInt(BonusToken.getBonusToken(
							"BONUS.COMBAT.AC." + acc.getType(), pc));
				}
			}
		}

		return armorClass;
	}

	public void setPrerequisiteFacet(PrerequisiteFacet prerequisiteFacet)
	{
		this.prerequisiteFacet = prerequisiteFacet;
	}

}
