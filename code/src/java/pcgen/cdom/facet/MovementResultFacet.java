/*
 * Copyright (c) 2010 Tom Parker <thpr@users.sourceforge.net>
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
package pcgen.cdom.facet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import pcgen.base.util.NamedValue;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.Constants;
import pcgen.cdom.base.FormulaFactory;
import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.Equipment;
import pcgen.core.Globals;
import pcgen.core.Movement;
import pcgen.core.Race;
import pcgen.core.SettingsHandler;
import pcgen.core.utils.CoreUtility;
import pcgen.util.enumeration.Load;

public class MovementResultFacet extends AbstractStorageFacet implements
		DataFacetChangeListener<CDOMObject>
{
	private final Class<?> thisClass = getClass();

	private MovementFacet movementFacet;
	private RaceFacet raceFacet;
	private TemplateFacet templateFacet;
	private DeityFacet deityFacet;
	private EquipmentFacet equipmentFacet;
	private BonusCheckingFacet bonusCheckingFacet;
	private UnencumberedArmorFacet unencumberedArmorFacet;
	private UnencumberedLoadFacet unencumberedLoadFacet;
	private FormulaResolvingFacet formulaResolvingFacet;
	private LoadFacet loadFacet;

	public double movementOfType(CharID id, String moveType)
	{
		MovementCacheInfo mci = getInfo(id);
		if (mci == null)
		{
			return 0.0;
		}
		return mci.movementOfType(moveType);
	}

	/**
	 * Returns the type-safe MovementCacheInfo for this MoneyFacet and the given
	 * CharID. Will return a new, empty MovementCacheInfo if no Money
	 * information has been set for the given CharID. Will not return null.
	 * 
	 * Note that this method SHOULD NOT be public. The MovementCacheInfo object
	 * is owned by MoneyFacet, and since it can be modified, a reference to that
	 * object should not be exposed to any object other than MoneyFacet.
	 * 
	 * @param id
	 *            The CharID for which the MovementCacheInfo should be returned
	 * @return The MovementCacheInfo for the Player Character represented by the
	 *         given CharID.
	 */
	private MovementCacheInfo getConstructingInfo(CharID id)
	{
		MovementCacheInfo rci = getInfo(id);
		if (rci == null)
		{
			rci = new MovementCacheInfo(id);
			setCache(id, thisClass, rci);
		}
		return rci;
	}

	/**
	 * Returns the type-safe MovementCacheInfo for this MoneyFacet and the given
	 * CharID. May return null if no Movement information has been set for the
	 * given CharID.
	 * 
	 * Note that this method SHOULD NOT be public. The MovementCacheInfo object
	 * is owned by MoneyFacet, and since it can be modified, a reference to that
	 * object should not be exposed to any object other than MoneyFacet.
	 * 
	 * @param id
	 *            The CharID for which the MovementCacheInfo should be returned
	 * @return The MovementCacheInfo for the Player Character represented by the
	 *         given CharID; null if no Movement information has been set for
	 *         the Player Character.
	 */
	private MovementCacheInfo getInfo(CharID id)
	{
		return (MovementCacheInfo) getCache(id, thisClass);
	}

	public class MovementCacheInfo
	{
		private final CharID id;

		public MovementCacheInfo(CharID charid)
		{
			id = charid;
		}

		private Double[] movementMult = Globals.EMPTY_DOUBLE_ARRAY;
		private String[] movementMultOp = Globals.EMPTY_STRING_ARRAY;
		private String[] movementTypes = Globals.EMPTY_STRING_ARRAY;

		// Movement lists
		private Double[] movements = Globals.EMPTY_DOUBLE_ARRAY;

		public double movementOfType(String moveType)
		{
			if (movementTypes == null)
				return 0.0;
			for (int moveIdx = 0; moveIdx < movementTypes.length; moveIdx++)
			{
				if (movementTypes[moveIdx].equalsIgnoreCase(moveType))
					return movement(moveIdx);
			}
			return 0.0;
		}

		public int countMovementTypes()
		{
			return (movements != null) ? movements.length : 0;
		}

		/**
		 * recalculate all the move rates and modifiers
		 */
		public void adjustMoveRates()
		{
			movements = null;
			movementTypes = null;
			movementMult = null;
			movementMultOp = null;

			Race race = raceFacet.get(id);
			if (race == null)
			{
				return;
			}

			List<Movement> mms = race.getListFor(ListKey.MOVEMENT);
			if (mms == null || mms.isEmpty() || (!mms.get(0).isInitialized()))
			{
				return;
			}

			Movement movement = mms.get(0);
			movements = movement.getMovements();
			movementTypes = movement.getMovementTypes();
			movementMult = movement.getMovementMult();
			movementMultOp = movement.getMovementMultOp();

			for (Movement mv : movementFacet.getSet(id))
			{
				for (int i1 = 0; i1 < mv.getNumberOfMovements(); i1++)
				{
					setMyMoveRates(mv.getMovementType(i1), mv.getMovement(i1)
							.doubleValue(), mv.getMovementMult(i1), mv
							.getMovementMultOp(i1), mv.getMoveRatesFlag());
				}
			}

			// Need to create movement entries if there is a BONUS:MOVEADD
			// associated with that type of movement
			for (String moveType : bonusCheckingFacet.getExpandedBonusInfo(id, "MOVEADD"))
			{
				if (moveType.startsWith("TYPE"))
				{
					moveType = moveType.substring(5);
				}

				if (!moveType.equalsIgnoreCase("ALL"))
				{
					moveType = CoreUtility.capitalizeFirstLetter(moveType);
	
					boolean found = false;
	
					for (int i = 0; i < movements.length; i++)
					{
						if (moveType.equals(movementTypes[i]))
						{
							found = true;
						}
					}
	
					if (!found)
					{
						setMyMoveRates(moveType, 0.0, Double.valueOf(0.0), "", 0);
					}
				}
			}
		}

		/**
		 * sets up the movement arrays creates them if they do not exist
		 * 
		 * @param moveType
		 * @param anDouble
		 * @param moveMult
		 * @param multOp
		 * @param moveFlag
		 */
		private void setMyMoveRates(String moveType, double anDouble,
				Double moveMult, String multOp, int moveFlag)
		{
			//
			// NOTE: can not use getMovements() accessor as it calls
			// this function, so use the variable: movements
			//
			Double moveRate;

			// The ALL type can only be applied to existing movement
			// so just loop and add or set as appropriate
			if ("ALL".equals(moveType))
			{
				if (moveFlag == 0)
				{ // set all types of movement to moveRate

					for (int i = 0; i < movements.length; i++)
					{
						moveRate = new Double(anDouble);
						movements[i] = moveRate;
					}
				}
				else
				{ // add moveRate to all types of movement.

					for (int i = 0; i < movements.length; i++)
					{
						moveRate = new Double(anDouble
								+ movements[i].doubleValue());
						movements[i] = moveRate;
					}
				}
			}
			else
			{
				if (moveFlag == 0)
				{ // set movement to moveRate
					moveRate = new Double(anDouble);

					for (int i = 0; i < movements.length; i++)
					{
						if (moveType.equals(movementTypes[i]))
						{
							if (moveRate > movements[i])
							{
								movements[i] = moveRate;
							}
							if (multOp != null
									&& (movementMultOp[i] == null || multOp
											.length() > 0))
							{
								movementMult[i] = moveMult;
								movementMultOp[i] = multOp;
							}

							return;
						}
					}

					increaseMoveArray(moveRate, moveType, moveMult, multOp);
				}
				else
				{ // get base movement, then add moveRate
					moveRate = new Double(anDouble + movements[0].doubleValue());

					// for existing types of movement:
					for (int i = 0; i < movements.length; i++)
					{
						if (moveType.equals(movementTypes[i]))
						{
							movements[i] = moveRate;
							movementMult[i] = moveMult;
							movementMultOp[i] = multOp;

							return;
						}
					}

					increaseMoveArray(moveRate, moveType, moveMult, multOp);
				}
			}
		}

		private void increaseMoveArray(Double moveRate, String moveType,
				Double moveMult, String multOp)
		{
			// could not find an existing one so
			// need to add new item to array
			//
			Double[] tempMove = movements;
			String[] tempType = movementTypes;
			Double[] tempMult = movementMult;
			String[] tempMultOp = movementMultOp;

			// now increase the size of the array by one
			movements = new Double[tempMove.length + 1];
			movementTypes = new String[tempMove.length + 1];
			movementMult = new Double[tempMove.length + 1];
			movementMultOp = new String[tempMove.length + 1];

			System.arraycopy(tempMove, 0, movements, 0, tempMove.length);
			System.arraycopy(tempType, 0, movementTypes, 0, tempMove.length);
			System.arraycopy(tempMult, 0, movementMult, 0, tempMove.length);
			System.arraycopy(tempMultOp, 0, movementMultOp, 0, tempMove.length);

			// the size is larger, but arrays start at 0
			// so an array length=3 would have 0, 1, 2 as the targets
			movements[tempMove.length] = moveRate;
			movementTypes[tempMove.length] = moveType;
			movementMult[tempMove.length] = moveMult;
			movementMultOp[tempMove.length] = multOp;
		}

		/**
		 * get the base MOVE: plus any bonuses from BONUS:MOVE additions takes
		 * into account Armor restrictions to movement and load carried
		 * 
		 * @param moveIdx
		 * @return movement
		 */
		public double movement(int moveIdx)
		{
			// get base movement
			double moveInFeet = getMovement(moveIdx).doubleValue();

			// First get the MOVEADD bonus
			moveInFeet += bonusCheckingFacet.getBonus(id, "MOVEADD", "TYPE."
					+ getMovementType(moveIdx).toUpperCase());

			// also check for special case of TYPE=ALL
			moveInFeet += bonusCheckingFacet.getBonus(id, "MOVEADD", "TYPE.ALL");

			double calcMove = moveInFeet;

			// now we apply any multipliers to the BASE move + MOVEADD move
			// First we get possible multipliers/divisors from the MOVE:
			// MOVEA: and MOVECLONE: tags
			if (getMovementMult(moveIdx).doubleValue() > 0)
			{
				calcMove = calcMoveMult(moveInFeet, moveIdx);
			}

			// Now we get the BONUS:MOVEMULT multipliers
			double moveMult = bonusCheckingFacet.getBonus(id, "MOVEMULT", "TYPE."
					+ getMovementType(moveIdx).toUpperCase());

			// also check for special case of TYPE=ALL
			moveMult += bonusCheckingFacet.getBonus(id, "MOVEMULT", "TYPE.ALL");

			if (moveMult > 0)
			{
				calcMove = (int) (calcMove * moveMult);
			}

			double postMove = calcMove;

			// now add on any POSTMOVE bonuses
			postMove += bonusCheckingFacet.getBonus(id, "POSTMOVEADD", "TYPE."
					+ getMovementType(moveIdx).toUpperCase());

			// also check for special case of TYPE=ALL
			postMove += bonusCheckingFacet.getBonus(id, "POSTMOVEADD", "TYPE.ALL");

			// because POSTMOVE is magical movement which should not be
			// multiplied by magical items, etc, we now see which is larger,
			// (baseMove + postMove) or (baseMove * moveMultiplier)
			// and keep the larger one, discarding the other
			moveInFeet = Math.max(calcMove, postMove);

			// get a list of all equipped Armor
			Load armorLoad = Load.LIGHT;

			for (Equipment eq : equipmentFacet.getSet(id))
			{
				if (!eq.typeStringContains("Armor") || !eq.isEquipped()
						|| eq.isShield())
				{
					continue;
				}
				if (eq.isHeavy()
						&& !unencumberedArmorFacet.ignoreLoad(id, Load.HEAVY))
				{
					armorLoad = armorLoad.max(Load.HEAVY);
				}
				else if (eq.isMedium()
						&& !unencumberedArmorFacet.ignoreLoad(id, Load.MEDIUM))
				{
					armorLoad = armorLoad.max(Load.MEDIUM);
				}
			}

			double armorMove = Globals
					.calcEncumberedMove(armorLoad, moveInFeet);

			Load pcLoad = loadFacet.getLoadType(id);
			double loadMove = calcEncumberedMove(pcLoad, moveInFeet);

			// It is possible to have a PC that is not encumbered by Armor
			// But is encumbered by Weight carried (and visa-versa)
			// So do two calcs and take the slowest
			moveInFeet = Math.min(armorMove, loadMove);

			return moveInFeet;
		}

		/**
		 * @param moveIdx
		 * @return the integer movement speed for Index
		 */
		private Double getMovement(int moveIdx)
		{
			if ((movements != null) && (moveIdx < movements.length))
			{
				return movements[moveIdx];
			}
			return Double.valueOf(0);
		}

		public String getMovementType(int moveIdx)
		{
			if ((movementTypes != null) && (moveIdx < movementTypes.length))
			{
				return movementTypes[moveIdx];
			}
			return Constants.EMPTY_STRING;
		}

		/**
		 * @param moveIdx
		 * @return the integer movement speed multiplier for Index
		 */
		private Double getMovementMult(int moveIdx)
		{
			if ((movements != null) && (moveIdx < movementMult.length))
			{
				return movementMult[moveIdx];
			}
			return Double.valueOf(0);
		}

		private double calcMoveMult(double move, int index)
		{
			double iMove = 0;

			if (movementMultOp[index].charAt(0) == '*')
			{
				iMove = move * movementMult[index].doubleValue();
			}
			else if (movementMultOp[index].charAt(0) == '/')
			{
				iMove = move / movementMult[index].doubleValue();
			}

			if (iMove > 0)
			{
				return iMove;
			}

			return move;
		}

		public List<NamedValue> getMovementValues()
		{
			List<NamedValue> list = new ArrayList<NamedValue>();
			for (int i = 0; i < countMovementTypes(); i++)
			{
				list.add(new NamedValue(getMovementType(i), movement(i)));
			}
			return list;
		}

		public Double getMovementOfType(String moveType)
		{
			for (int x = 0; x < countMovementTypes(); ++x)
			{
				String type = getMovementType(x);
				if (moveType.equalsIgnoreCase(type))
				{
					return getMovement(x);
				}
			}
			return Double.valueOf(0);
		}

		public int getBaseMovement(String moveType, Load load)
		{
			for (int i = 0; i < countMovementTypes(); i++)
			{
				if (getMovementType(i).equalsIgnoreCase(moveType))
				{
					return getMovement(i).intValue();
				}
			}
			return 0;
		}

		public boolean hasMovement(String moveType)
		{
			for (int i = 0; i < countMovementTypes(); i++)
			{
				if (getMovementType(i).equalsIgnoreCase(moveType))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * Works for dnd according to the method noted in the faq. (NOTE: The
		 * table in the dnd faq is wrong for speeds 80 and 90) Not as sure it
		 * works for all other d20 games.
		 * 
		 * @param load
		 * @param unencumberedMove
		 *            the unencumbered move value
		 * @return encumbered move as an integer
		 */
		public double calcEncumberedMove(Load load, double unencumberedMove)
		{
			double encumberedMove;

			//
			// Can we ignore any encumberance for this type? If we can, then
			// there's
			// no
			// need to do any more calculations.
			//
			if (unencumberedLoadFacet.ignoreLoad(id, load))
			{
				encumberedMove = unencumberedMove;
			}
			else
			{
				String formula = SettingsHandler.getGame().getLoadInfo()
						.getLoadMoveFormula(load.toString());
				if (formula.length() != 0)
				{
					formula = formula.replaceAll(Pattern.quote("$$MOVE$$"),
							Double.toString(Math.floor(unencumberedMove)));
					return formulaResolvingFacet.resolve(id,
							FormulaFactory.getFormulaFor(formula), "")
							.doubleValue();
				}

				return Globals.calcEncumberedMove(load, unencumberedMove);
			}

			return encumberedMove;
		}

		@Override
		public int hashCode()
		{
			return (movementTypes.length == 0) ? -1 : movementTypes[0].hashCode();
		}

		@Override
		public boolean equals(Object o)
		{
			if (o == this)
			{
				return true;
			}
			if (o instanceof MovementCacheInfo)
			{
				MovementCacheInfo ci = (MovementCacheInfo) o;
				return Arrays.deepEquals(movementMult, ci.movementMult)
					&& Arrays.deepEquals(movementMultOp, ci.movementMultOp)
					&& Arrays.deepEquals(movementTypes, ci.movementTypes)
					&& Arrays.deepEquals(movements, ci.movements);
			}
			return false;
		}
	}

	public int countMovementTypes(CharID id)
	{
		MovementCacheInfo mci = getInfo(id);
		if (mci == null)
		{
			return 0;
		}
		return mci.countMovementTypes();
	}

	public void reset(CharID id)
	{
		getConstructingInfo(id).adjustMoveRates();
	}

	public List<NamedValue> getMovementValues(CharID id)
	{
		MovementCacheInfo mci = getInfo(id);
		if (mci == null)
		{
			return Collections.emptyList();
		}
		return mci.getMovementValues();
	}

	public Double getMovementOfType(CharID id, String moveType)
	{
		MovementCacheInfo mci = getInfo(id);
		if (mci == null)
		{
			return Double.valueOf(0);
		}
		return mci.getMovementOfType(moveType);
	}

	public int getBaseMovement(CharID id, String moveType, Load load)
	{
		MovementCacheInfo mci = getInfo(id);
		if (mci == null)
		{
			return 0;
		}
		return mci.getBaseMovement(moveType, load);
	}

	public boolean hasMovement(CharID id, String moveType)
	{
		MovementCacheInfo mci = getInfo(id);
		if (mci == null)
		{
			return false;
		}
		return mci.hasMovement(moveType);
	}

	@Override
	public void dataAdded(DataFacetChangeEvent<CDOMObject> dfce)
	{
		reset(dfce.getCharID());
	}

	@Override
	public void dataRemoved(DataFacetChangeEvent<CDOMObject> dfce)
	{
		reset(dfce.getCharID());
	}

	public void setMovementFacet(MovementFacet movementFacet)
	{
		this.movementFacet = movementFacet;
	}

	public void setRaceFacet(RaceFacet raceFacet)
	{
		this.raceFacet = raceFacet;
	}

	public void setTemplateFacet(TemplateFacet templateFacet)
	{
		this.templateFacet = templateFacet;
	}

	public void setDeityFacet(DeityFacet deityFacet)
	{
		this.deityFacet = deityFacet;
	}

	public void setEquipmentFacet(EquipmentFacet equipmentFacet)
	{
		this.equipmentFacet = equipmentFacet;
	}

	public void setBonusCheckingFacet(BonusCheckingFacet bonusCheckingFacet)
	{
		this.bonusCheckingFacet = bonusCheckingFacet;
	}

	public void setUnencumberedArmorFacet(
		UnencumberedArmorFacet unencumberedArmorFacet)
	{
		this.unencumberedArmorFacet = unencumberedArmorFacet;
	}

	public void setUnencumberedLoadFacet(UnencumberedLoadFacet unencumberedLoadFacet)
	{
		this.unencumberedLoadFacet = unencumberedLoadFacet;
	}

	public void setFormulaResolvingFacet(FormulaResolvingFacet formulaResolvingFacet)
	{
		this.formulaResolvingFacet = formulaResolvingFacet;
	}

	public void setLoadFacet(LoadFacet loadFacet)
	{
		this.loadFacet = loadFacet;
	}

	public void init()
	{
		raceFacet.addDataFacetChangeListener(2000, this);
		deityFacet.addDataFacetChangeListener(2000, this);
		templateFacet.addDataFacetChangeListener(2000, this);
	}

	@Override
	public void copyContents(CharID source, CharID copy)
	{
		MovementCacheInfo mci = getInfo(source);
		if (mci != null)
		{
			MovementCacheInfo copymci = getConstructingInfo(copy);
			copymci.movementMult = mci.movementMult.clone();
			copymci.movementMultOp = mci.movementMultOp.clone();
			copymci.movements = mci.movements.clone();
			copymci.movementTypes = mci.movementTypes.clone();
		}
	}
}
