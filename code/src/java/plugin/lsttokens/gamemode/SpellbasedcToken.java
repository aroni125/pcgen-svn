package plugin.lsttokens.gamemode;

import java.net.URI;

import pcgen.core.GameMode;
import pcgen.persistence.lst.GameModeLstToken;

/**
 * Class deals with SPELLBASEDC Token
 */
public class SpellbasedcToken implements GameModeLstToken
{

	public String getTokenName()
	{
		return "SPELLBASEDC";
	}

	public boolean parse(GameMode gameMode, String value, URI source)
	{
		gameMode.setSpellBaseDC(value);
		return true;
	}
}
