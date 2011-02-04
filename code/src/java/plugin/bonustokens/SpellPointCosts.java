/**
 * 
 */
package plugin.bonustokens;

import java.util.StringTokenizer;

import pcgen.core.bonus.BonusObj;
import pcgen.core.bonus.util.SpellPointCostInfo;
import pcgen.core.bonus.util.SpellPointCostInfo.SpellPointFilterType;
import pcgen.rules.context.LoadContext;
import pcgen.util.Logging;

/**
 * @author Joe.Frazier
 *
 */
public class SpellPointCosts extends BonusObj
{
	@Override
	protected boolean parseToken(LoadContext context, final String token)
	{
		SpellPointCostInfo spi = null;
		SpellPointFilterType type =null;
		String typeValue =null;
		String part = null;
		
		if (token == null)
		{
			Logging.errorPrint("Malformed BONUS:SPELLPOINTCOST.");
			return false;
		}
		StringTokenizer aTok = new StringTokenizer(token, ";");
		String val = aTok.nextToken();
		StringTokenizer aTok2 = new StringTokenizer(val, ".");
		if(aTok2.countTokens() >=1 )
		{
			String theType = aTok2.nextToken();
			if(theType.equals("SCHOOL"))
			{
				type = SpellPointFilterType.SCHOOL;
			}
			else if(theType.equals("SUBSCHOOL"))
			{
				type = SpellPointFilterType.SUBSCHOOL;
			}
			else if(theType.equals("SPELL"))
			{
				type = SpellPointFilterType.SPELL;
			}
			else 
			{
				Logging.errorPrint("Malformed BONUS:SPELLPOINTCOST: " + token);
				return false;
			}
			typeValue = aTok2.nextToken();
		}
		else 
		{
			Logging.errorPrint("Malformed BONUS:SPELLPOINTCOST: " + token);
			return false;
		}
		
		if(aTok.hasMoreTokens())
		{
			part = aTok.nextToken();
		}
		else 
		{
			part = "TOTAL";
		}
			
		
		if(type == null || typeValue == null || part == null)
		{
			Logging.errorPrint("Malformed BONUS:SPELLPOINTCOST: " + token);
			return false;
		}
		spi = new SpellPointCostInfo(type, typeValue, part, false);
		
		
		addBonusInfo(spi);

		return true;
	}
	
	@Override
	protected String unparseToken(Object obj)
	{
		SpellPointCostInfo spInfo = (SpellPointCostInfo) obj;
		StringBuffer sb = new StringBuffer();
		sb.append(spInfo.getSpellPointPartFilter().toString());
		sb.append(".");
		sb.append(spInfo.getSpellPointPartFilterValue());
		
		sb.append(";");
		sb.append(spInfo.getSpellPointPart());

		return sb.toString();
	}

	@Override
	public String getBonusHandled()
	{
		return "SPELLPOINTCOST";
	}
	
}
