/*
 * InitiativeBonusToken.java
 * Copyright 2003 (C) Devon Jones <soulcatcher@evilsoft.org>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.     See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on December 15, 2003, 12:21 PM
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 *
 */
package plugin.exporttokens;

import pcgen.core.PlayerCharacter;
import pcgen.io.ExportHandler;
import pcgen.io.exporttoken.Token;
import pcgen.util.Delta;

/**
 * Deals with INITIATIVEBONUS Token
 */
public class InitiativeBonusToken extends Token
{
	/** Token Name */
	public static final String TOKENNAME = "INITIATIVEBONUS";

	/**
	 * @see pcgen.io.exporttoken.Token#getTokenName()
	 */
	@Override
	public String getTokenName()
	{
		return TOKENNAME;
	}

	//TODO: Merge InitiativeBonusToken, InitiativeMiscToken and InitiativeModToken
	//to become INITIATIVE.BONUS, INITAITIVE.MISC & INITIATIVE.MOD
	/**
	 * @see pcgen.io.exporttoken.Token#getToken(java.lang.String, pcgen.core.PlayerCharacter, pcgen.io.ExportHandler)
	 */
	@Override
	public String getToken(String tokenSource, PlayerCharacter pc,
		ExportHandler eh)
	{
		return Delta.toString(getInitiativeBonusToken(pc));
	}

	/**
	 * Get the Token
	 * @param pc
	 * @return the token
	 */
	public static int getInitiativeBonusToken(PlayerCharacter pc)
	{
		return pc.initiativeMod()
			- pc.getVariableValue("INITCOMP", "").intValue();
	}
}
