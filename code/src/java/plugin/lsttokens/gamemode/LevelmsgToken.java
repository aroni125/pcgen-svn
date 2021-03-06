package plugin.lsttokens.gamemode;

import java.net.URI;

import pcgen.core.GameMode;
import pcgen.persistence.lst.GameModeLstToken;

/**
 * Class deals with LEVELMSG Token
 */
public class LevelmsgToken implements GameModeLstToken
{

	public String getTokenName()
	{
		return "LEVELMSG";
	}

	public boolean parse(GameMode gameMode, String value, URI source)
	{
		gameMode.setLevelUpMessage(value.replace('|', '\n'));
		return true;
	}
}
