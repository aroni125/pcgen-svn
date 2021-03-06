/*
 * Copyright (c) 2009 Mark Jeffries <motorviper@users.sourceforge.net>
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */
package pcgen.rules.persistence.token;

import java.util.LinkedList;
import java.util.logging.Level;

import pcgen.util.Logging;

/**
 * Class that implements ParseResult for providing more complicated feedback.
 * See plugin.lsttokens.pcclass.ExchangeLevelToken.
 */
public class ComplexParseResult implements ParseResult
{
	private final LinkedList<QueuedMessage> queuedMessages =
			new LinkedList<QueuedMessage>();

	public ComplexParseResult()
	{
	}

	public ComplexParseResult(String error)
	{
		addErrorMessage(error);
	}

	public ComplexParseResult(ComplexParseResult toCopy)
	{
		addMessages(toCopy);
	}

	public void addErrorMessage(String msg)
	{
		addParseMessage(Logging.LST_ERROR, msg);
	}

    public void addWarningMessage(String msg)
    {
        addParseMessage(Logging.LST_WARNING, msg);
    }

    public void addInfoMessage(String msg)
    {
        addParseMessage(Logging.LST_INFO, msg);
    }

	protected void addParseMessage(Level lvl, String msg)
	{
		queuedMessages.add(new QueuedMessage(lvl, msg));
	}

	public void addMessages(ComplexParseResult pr)
	{
		for (QueuedMessage msg : pr.queuedMessages)
		{
			queuedMessages.add(msg);
		}
	}

	public void printMessages()
	{
		for (QueuedMessage msg : queuedMessages)
		{
			Logging.log(msg.level, msg.message, msg.stackTrace);
		}
	}

	public void addMessagesToLog()
	{
		for (QueuedMessage msg : queuedMessages)
		{
			Logging.addParseMessage(msg.level, msg.message, msg.stackTrace);
		}
	}

	public boolean passed()
	{
		for (QueuedMessage msg : queuedMessages)
		{
			if (msg.level == Logging.LST_ERROR)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Copy messages from another ParseResult object.
	 * @param pr The object to copy from.
	 */
	public void copyMessages(ParseResult pr)
	{
		if (pr instanceof ComplexParseResult)
		{
			ComplexParseResult cpr = (ComplexParseResult) pr;
			for (QueuedMessage msg : cpr.queuedMessages)
			{
				queuedMessages.add(msg);
			}
		}
		else if (pr instanceof ParseResult.Fail)
		{
			ParseResult.Fail fail = (ParseResult.Fail) pr;
			queuedMessages.add(fail.getError());
		}
	}
}