package plugin.bonustokens;

import pcgen.core.bonus.BonusObj;
import pcgen.rules.context.LoadContext;

public class Followers extends BonusObj
{
	@Override
	public String getBonusHandled()
	{
		return "FOLLOWERS";
	}

	@Override
	protected boolean parseToken(LoadContext context, final String argToken)
	{
		//		Collection<String> followerTypes = Globals.getFollowerTypes();
		//		if ( followerTypes.contains( argToken ) )
		//		{
		addBonusInfo(argToken);
		return true;
		//		}
		//		return false;
	}

	@Override
	protected String unparseToken(final Object obj)
	{
		return (String) obj;
	}
}
