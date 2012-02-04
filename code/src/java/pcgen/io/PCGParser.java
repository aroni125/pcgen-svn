/*
 * PCGParser.java
 * Copyright 2002 (C) Thomas Behr <ravenlock@gmx.de>
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
 * Created on September 07, 2002, 11:30 AM
 */
package pcgen.io;

import java.util.List;

import pcgen.core.facade.SourceSelectionFacade;

/**
 * <code>PCGParser</code><br>
 * @author Thomas Behr 07-09-02
 * @version $Revision$
 */
interface PCGParser
{
	/**
	 * Returns the list of warning messages generated by parsing the current
	 * document.
	 *
	 * <br>author: Thomas Behr 07-09-02
	 *
	 * @return a list of warning messages
	 */
	List<String> getWarnings();

	/**
	 * parse a String in PCG format
	 *
	 * <br>author: Thomas Behr 07-09-02
	 *
	 * @param lines   the String to parse
	 * @throws PCGParseException
	 */
	public void parsePCG(String[] lines) throws PCGParseException;

	/**
	 * Check the game mode and then build a list of campaigns the character 
	 * requires to be loaded.
	 *   
	 * @param lines The PCG lines to be parsed.
	 * @return The list of campaigns.
	 * @throws PCGParseException If the lines are invalid 
	 */
	public SourceSelectionFacade parcePCGSourceOnly(String[] lines) throws PCGParseException;
	

	/**
	 * @return the baseFeatPool
	 */
	public double getBaseFeatPool();

	/**
	 * @return the calcFeatPoolAfterLoad
	 */
	public boolean isCalcFeatPoolAfterLoad();
}
