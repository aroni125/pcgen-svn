package plugin.lsttokens.gamemode;

import java.net.URI;

import pcgen.core.GameMode;
import pcgen.persistence.lst.GameModeLstToken;

/**
 * Class deals with WEAPONCATEGORY Token
 */
public class WeaponreachformulaToken implements GameModeLstToken
{

	public String getTokenName()
	{
		return "WEAPONREACH";
	}

	public boolean parse(GameMode gameMode, String value, URI source)
	{
		gameMode.setWeaponReachFormula(value);
		return true;
	}
}
