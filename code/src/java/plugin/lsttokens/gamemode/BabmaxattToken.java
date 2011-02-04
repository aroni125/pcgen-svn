package plugin.lsttokens.gamemode;

import java.net.URI;

import pcgen.core.GameMode;
import pcgen.persistence.lst.GameModeLstToken;

/**
 * Class deals with BABMAXATT Token
 */
public class BabmaxattToken implements GameModeLstToken
{

	public String getTokenName()
	{
		return "BABMAXATT";
	}

	public boolean parse(GameMode gameMode, String value, URI source)
	{
		try
		{
			gameMode.setBabMaxAtt(Integer.parseInt(value));
			return true;
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
	}
}
