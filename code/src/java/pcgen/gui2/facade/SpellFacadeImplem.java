/**
 * SpellFacadeImplem.java
 * Copyright James Dempsey, 2011
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
 *
 * Created on 15/10/2011 4:04:57 PM
 *
 * $Id$
 */
package pcgen.gui2.facade;

import java.util.List;

import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.SourceFormat;
import pcgen.core.Globals;
import pcgen.core.PlayerCharacter;
import pcgen.core.character.CharacterSpell;
import pcgen.core.character.SpellInfo;
import pcgen.core.facade.SpellFacade;
import pcgen.core.spell.Spell;

/**
 * The Class <code>SpellFacadeImplem</code> is ...
 *
 * <br/>
 * Last Editor: $Author$
 * Last Edited: $Date$
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision$
 */
public class SpellFacadeImplem implements SpellFacade
{
	private final PlayerCharacter pc;
	private final Spell spell;
	private final CharacterSpell charSpell;
	private final SpellInfo spellInfo;

	public SpellFacadeImplem(PlayerCharacter pc, Spell spell, CharacterSpell charSpell, SpellInfo spellInfo)
	{
		this.pc = pc;
		this.spell = spell;
		this.charSpell = charSpell;
		this.spellInfo = spellInfo;
		
	}
	
	/* (non-Javadoc)
	 * @see pcgen.core.facade.InfoFacade#getSource()
	 */
	public String getSource()
	{
		return SourceFormat.getFormattedString(spell,
			Globals.getSourceDisplay(), true);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.InfoFacade#getKeyName()
	 */
	public String getKeyName()
	{
		return spell.getKeyName();
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.SpellFacade#getSchool()
	 */
	public String getSchool()
	{
		return spell.getListAsString(ListKey.SPELL_SCHOOL);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.SpellFacade#getSubschool()
	 */
	public String getSubschool()
	{
        return spell.getListAsString(ListKey.SPELL_SUBSCHOOL);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.SpellFacade#getDescriptors()
	 */
	public String[] getDescriptors()
	{
        List<String> descriptors = spell.getListFor(ListKey.SPELL_DESCRIPTOR);
        if (descriptors== null)
        {
        	return new String[]{};
        }
		return descriptors.toArray(new String[]{});
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.SpellFacade#getComponents()
	 */
	public String getComponents()
	{
		return spell.getListAsString(ListKey.COMPONENTS);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.SpellFacade#getRange()
	 */
	public String getRange()
	{
        return pc.getSpellRange(charSpell, spellInfo);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.SpellFacade#getDuration()
	 */
	public String getDuration()
	{
		return pc.parseSpellString(charSpell, spell.getListAsString(ListKey.DURATION));
	}

	/**
	 * @return the spell
	 */
	Spell getSpell()
	{
		return spell;
	}

	/**
	 * @return the charSpell
	 */
	CharacterSpell getCharSpell()
	{
		return charSpell;
	}

	/**
	 * @return the spellInfo
	 */
	SpellInfo getSpellInfo()
	{
		return spellInfo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder buff = new StringBuilder();
		if (spell != null)
		{
			buff.append(spell.toString());
		}
		else if (charSpell != null)
		{
			buff.append(charSpell.toString());
		}
		if (spellInfo != null)
		{
			buff.append(spellInfo.toString());
		}
		return buff.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result =
				prime * result
					+ ((charSpell == null) ? 0 : charSpell.hashCode());
		result = prime * result + ((pc == null) ? 0 : pc.hashCode());
		result = prime * result + ((spell == null) ? 0 : spell.hashCode());
		result =
				prime * result
					+ ((spellInfo == null) ? 0 : spellInfo.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		SpellFacadeImplem other = (SpellFacadeImplem) obj;
		if (charSpell == null)
		{
			if (other.charSpell != null)
			{
				return false;
			}
		}
		else if (!charSpell.equals(other.charSpell))
		{
			return false;
		}
		if (pc == null)
		{
			if (other.pc != null)
			{
				return false;
			}
		}
		else if (!pc.equals(other.pc))
		{
			return false;
		}
		if (spell == null)
		{
			if (other.spell != null)
			{
				return false;
			}
		}
		else if (!spell.equals(other.spell))
		{
			return false;
		}
		if (spellInfo == null)
		{
			if (other.spellInfo != null)
			{
				return false;
			}
		}
		else if (!spellInfo.equals(other.spellInfo))
		{
			return false;
		}
		return true;
	}

}
