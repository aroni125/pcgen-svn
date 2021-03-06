package plugin.lsttokens.gamemode;

import java.net.URI;

import pcgen.core.GameMode;
import pcgen.persistence.lst.GameModeLstToken;


/**
 * @author Joe.Frazier
 *
 */
public class DieSizesToken implements GameModeLstToken
{

	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.GameModeLstToken#parse(pcgen.core.GameMode, java.lang.String, java.net.URI)
	 */
public boolean parse(final GameMode gameMode, final String value, final URI source)
	{
		gameMode.setDieSizes(value);
		return true;
	}

	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.LstToken#getTokenName()
	 */
	public String getTokenName()
	{
		return "DIESIZES";
	}

}
