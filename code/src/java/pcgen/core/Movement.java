/*
 * Copyright 2005 (C) Tom Parker <thpr@sourceforge.net>
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
 * Created on July 22, 2005.
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 */
package pcgen.core;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

import pcgen.util.Logging;

/**
 * @author Tom Parker <thpr@sourceforge.net>
 */
public class Movement
{

	/**
	 * Contains the movement Types for this Movement (e.g. "Walk", "Fly")
	 */
	private String[] movementTypes;

	/**
	 * Contains the associated movement rate (in feet) for the movement type of
	 * the same index. A movement rate must be greater than or equal to zero.
	 *
	 * REFACTOR This should be changed to double[] once PlayerCharacter can
	 * handle it
	 */
	private Double[] movements;

	/**
	 * The movement multiplier for the movement type of the same index. A
	 * movement Multiplier be greater than zero.
	 *
	 * REFACTOR This should be changed to double[] once PlayerCharacter can
	 * handle it
	 */
	private Double[] movementMult;

	/**
	 * The movement operation for the movement type of the same index. (e.g. "*"
	 * or "/")
	 */
	private String[] movementMultOp;

	/**
	 * The Movement Rates flag indicating which type of Movement object this is
	 * 0 indicates a basic assignment
	 * 1 indicates the movement rates are added to the existing movement rate 
	 * for the contained types
	 * 2 indicates this clones one movement rate into another movement rate
	 */
	private int moveRatesFlag;

	/*
	 * A class invariant is that the four above arrays should always have the
	 * same length.
	 */

	/*
	 * CONSIDER I don't know why this variable exists?? - it seems to me it's
	 * duplicate of movements[0]
	 */
	private int movement;

	/*
	 * REFACTOR Once PlayerCharacter is capable of using a CompositeMovement to
	 * do movement resolution, then this should be refactored to
	 * ConcreteMovement and implement the Movement interface (because
	 * CompositeMovement will be a BasicMovement)
	 */

	/**
	 * Creates a Movement object with arrays of the given length. It is assumed
	 * that the user of this constructor will initialize all of the arrays, as
	 * this constructor does not perform initialization.
	 *
	 * @param i
	 *            The length of the movement arrays to be assigned.
	 */
	public Movement(int i)
	{
		if (i <= 0)
		{
			throw new IllegalArgumentException(
				"Argument of array length to ConcreteMovement"
					+ "constructor must be positive");
		}
		movementTypes = new String[i];
		movements = new Double[i];
		movementMult = new Double[i];
		movementMultOp = new String[i];

		// default the basic movement to the first movement type, if the creature has a
		// walk speed in some entry other than 0 this will be changed by the assign
		// movement operation.
		movement = 0;
	}

	/**
	 * Sets the Move Rates Flag on this Movement object.
	 *
	 * @param i
	 *            The move rates flag.
	 */
	public void setMoveRatesFlag(int i)
	{
		if (i != 0 && i != 2)
		{
			throw new IllegalArgumentException("Rate Flag must be 0 or 2");
		}
		moveRatesFlag = i;
	}

	/**
	 * Gets the Movement Rates Flag for this Movement object.
	 * @return move rates flag
	 */
	public int getMoveRatesFlag()
	{
		return moveRatesFlag;
	}

	/**
	 * Return the creature's basic movement, this will be set to the walk
	 * speed (if the creature has one) by the assign movement operation.
	 * If no walk speed is assigned to the creature then the first movement
	 * defined is returned.
	 * @return movement as a Double
	 */
	public Double getDoubleMovement()
	{
		return movements[movement];
	}

	/**
	 * Get a movement multiplier
	 * @param index of the specified movement multiplier
	 * @return a movement multiplier
	 */
	public Double getMovementMult(int index)
	{
		return movementMult[index];
	}

	/**
	 * a movement multiplier operator
	 * @param index of the specified movement
	 * @return a movement multiplier operator
	 */
	public String getMovementMultOp(int index)
	{
		return movementMultOp[index];
	}

	/**
	 * Get all of the movement multipliers
	 * @return clone of the movement multipliers array
	 */
	public Double[] getMovementMult()
	{
		return movementMult.clone();
	}

	/**
	 * Get all of the movement multiplier operators
	 * @return clone of the movement multiplier operators array
	 */
	public String[] getMovementMultOp()
	{
		return movementMultOp.clone();
	}

	/**
	 * Get the number of movement types
	 * @return the number of movement types
	 */
	public int getNumberOfMovementTypes()
	{
		return (movementTypes != null) ? movementTypes.length : 0;
	}

	/**
	 * Set the movement types
	 * @param arrayString
	 */
	public void setMovementTypes(String[] arrayString)
	{
		movementTypes = arrayString;
	}

	/**
	 * Get the movement type from the array 
	 * @param i
	 * @return movement type
	 */
	public String getMovementType(int i)
	{
		if ((movementTypes != null) && (i < movementTypes.length))
		{
			return movementTypes[i];
		}

		return "";
	}

	/**
	 * Get the movement types
	 * @return the movement types
	 */
	public String[] getMovementTypes()
	{
		return movementTypes.clone();
	}

	/**
	 * Get the movement at index i
	 * @param i
	 * @return the movement at index i or 0
	 */
	public Double getMovement(int i)
	{
		if ((movements != null) && (i < movements.length))
		{
			return movements[i];
		}

		return Double.valueOf(0.0);
	}

	/**
	 * Get the number of movements
	 * @return number of movements
	 */
	public int getNumberOfMovements()
	{
		return (movements != null) ? movements.length : 0;
	}

	/**
	 * True if movements is not null
	 * @return True if movements is not null
	 */
	public boolean isInitialized()
	{
		return movements != null;
	}

	/**
	 * Get movements
	 * @return movements
	 */
	public Double[] getMovements()
	{
		return movements.clone();
	}

	/**
	 * Provides a String representation of this Movement object, suitable for
	 * display to a user.
	 * @return String
	 */
	@Override
	public String toString()
	{
		final StringBuffer movelabel = new StringBuffer();
		// movementTypes can be empty if a race is created without a MOVE tag in 
		// the LST editor
		if (movementTypes != null && movementTypes.length > 0)
		{
			movelabel.append(movementTypes[0]);
			movelabel.append(' ').append(
				Globals.getGameModeUnitSet().convertDistanceToUnitSet(
					movements[0].doubleValue()));
			movelabel.append(Globals.getGameModeUnitSet().getDistanceUnit());
			if (movementMult[0].doubleValue() != 0)
			{
				movelabel.append('(').append(movementMultOp[0]).append(
					movementMult[0]).append(')');
			}

			for (int i = 1; i < movementTypes.length; ++i)
			{
				movelabel.append(", ");
				movelabel.append(movementTypes[i]);
				movelabel.append(' ').append(
					Globals.getGameModeUnitSet().convertDistanceToUnitSet(
						movements[i].doubleValue()));
				movelabel
					.append(Globals.getGameModeUnitSet().getDistanceUnit());
				if (movementMult[i].doubleValue() != 0)
				{
					movelabel.append('(').append(movementMultOp[i]).append(
						movementMult[i]).append(')');
				}
			}
		}
		return movelabel.toString();
	}

	public void addTokenContents(StringBuilder txt)
	{
		if (moveRatesFlag == 2)
		{
			txt.append(movementTypes[0]);
			txt.append(',');
			txt.append(movementTypes[1]);
			txt.append(',');
			if (movementMultOp[1].length() > 0)
			{
				txt.append(movementMultOp[1]).append(movementMult[1].intValue());
			}
			else
			{
				txt.append(new DecimalFormat("###0").format(movements[1]));
			}
			return;
		}
		for (int index = 0; index < movementTypes.length; ++index)
		{
			if (index > 0)
			{
				txt.append(',');
			}

			if ((movementTypes[index] != null)
				&& (movementTypes[index].length() > 0))
			{
				txt.append(movementTypes[index]).append(',');
			}

			if (movementMultOp[index].length() > 0)
			{
				txt.append(movementMultOp[index]).append(movementMult[index]);
			}
			else
			{
				txt.append(new DecimalFormat("###0").format(movements[index]));
			}
		}
	}

	/**
	 * Returns a ConcreteMovement object initialized from the given string. This
	 * string can be any legal string for the MOVE, MOVEA, or MOVECLONE tags.
	 * The object which calls getMovementFrom MUST subsequently assign the move
	 * rates flag of the returned ConcreteMovement in order for the
	 * ConcreteMovement to function properly. (The default move rates flag is
	 * zero, so assignment in that case is not necessary)
	 *
	 * @param moveparse
	 *            The String from which a new ConcreteMovement should be
	 *            initialized
	 * @return A new ConcreteMovement initialized from the given String.
	 */
	public static Movement getMovementFrom(final String moveparse)
	{
		if (moveparse == null)
		{
			throw new IllegalArgumentException(
				"Null initialization String illegal");
		}
		final StringTokenizer moves = new StringTokenizer(moveparse, ",");
		Movement cm;

		if (moves.countTokens() == 1)
		{
			cm = new Movement(1);
			cm.assignMovement(0, "Walk", moves.nextToken());
		}
		else
		{
			cm = new Movement(moves.countTokens() / 2);

			int x = 0;
			while (moves.countTokens() > 1)
			{
				cm.assignMovement(x++, moves.nextToken(), moves.nextToken());
			}
			if (moves.countTokens() != 0)
			{
				Logging.errorPrint("Badly formed MOVE token "
					+ "(extra value at end of list): " + moveparse);
			}
		}
		return cm;
	}

	public void assignMovement(int x, String type, String mod)
	{
		movementTypes[x] = type; // e.g. "Walk"
		movementMult[x] = Double.valueOf(0.0);
		movementMultOp[x] = "";

		if ((mod.length() > 0)
			&& ((mod.charAt(0) == '*') || (mod.charAt(0) == '/')))
		{
			movements[x] = Double.valueOf(0.0);
			try
			{
				double multValue = Double.parseDouble(mod.substring(1));
				if (multValue < 0)
				{
					Logging.errorPrint("Illegal movement multiplier: "
						+ multValue + " in movement string " + mod);
				}
				movementMult[x] = Double.valueOf(multValue);
				movementMultOp[x] = mod.substring(0, 1);
			}
			catch (NumberFormatException e)
			{
				Logging.errorPrint("Badly formed MOVE token: " + mod);
				movementMult[x] = Double.valueOf(0.0);
				movementMultOp[x] = "";
			}
		}
		else if (mod.length() > 0)
		{
			movementMult[x] = Double.valueOf(0.0);
			movementMultOp[x] = "";

			try
			{
				movements[x] = new Double(mod);
			}
			catch (NumberFormatException e)
			{
				Logging.errorPrint("Badly formed MOVE token: " + mod);
				movements[x] = Double.valueOf(0.0);
			}

			if ("Walk".equals(movementTypes[x]))
			{
				movement = x;
			}
		}
	}
}
