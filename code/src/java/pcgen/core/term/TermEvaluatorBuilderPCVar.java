/**
 * pcgen.core.term.EvaluatorFactoryPCVar.java
 * Copyright 2008 Andrew Wilson
 * <nuance@users.sourceforge.net>.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created 03 August 2008
 *
 * Current Ver: $Revision:$
 * Last Editor: $Author:$
 * Last Edited: $Date:$
 */

package pcgen.core.term;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pcgen.cdom.base.Constants;
import pcgen.core.AbilityCategory;
import pcgen.util.Logging;
import pcgen.util.TermUtilities;

/**
 * <code>EvaluatorFactoryPCVar</code> 
 *
 * This individual enumerations in this class are each responsible for making
 * and returning an object that implements the TermEvaluator interface.
 * Each enumeration has a regular expression that matches one of the
 * "hardcoded" internal variables that every PC has a value for.  They also
 * have an array of string keys that enumerate every string that the regular
 * expression can match (this is not as bad as it sounds since each can only
 * match at most eight strings).  The array of string is used to populate a
 * Map<String, Enum>
 */

public enum TermEvaluatorBuilderPCVar implements TermEvaluatorBuilder
{
	COMPLETE_PC_ACCHECK
			("AC{1,2}HECK",
			 new String[] { "ACCHECK" , "ACHECK"},
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString,
				final String src,
				final String matchedSection) {

			return new PCACcheckTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_ARMORACCHECK
			("ARMORAC{1,2}HECK",
			 new String[] { "ARMORACCHECK", "ARMORACHECK"  },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString,
				final String src,
				final String matchedSection) {

			return new PCArmourACcheckTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_BAB
			("BAB",
			 new String[] { "BAB" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString,
				final String src, 
				final String matchedSection) {

			return new PCBABTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_BASESPELLSTAT
			("BASESPELLSTAT",
			 new String[] { "BASESPELLSTAT" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			String source = (src.startsWith("CLASS:")) ? src.substring(6) : "";

			return new PCBaseSpellStatTermEvaluator(expressionString, source);
		}
	},

	COMPLETE_PC_CASTERLEVEL
			("(?:CASTERLEVEL\\.TOTAL|CASTERLEVEL)",
			 new String[] { "CASTERLEVEL", "CASTERLEVEL.TOTAL" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			if ("CASTERLEVEL".equals(matchedSection))
			{
				if (src.startsWith("RACE:"))
				{
					return new PCCasterLevelRaceTermEvaluator(
							expressionString,
							src.substring(5));
				}
				else if (src.startsWith("CLASS:"))
				{
					return new PCCasterLevelClassTermEvaluator(
							expressionString,
							src.substring(6));
				}
				else
				{
					return new PCCasterLevelTotalTermEvaluator(expressionString);
				}
			}
			return new PCCasterLevelTotalTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_ATTACKS
			("COUNT\\[ATTACKS\\]",
			 new String[] { "COUNT[ATTACKS]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountAttacksTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_CHECKS
			("COUNT\\[CHECKS\\]",
			 new String[] { "COUNT[CHECKS]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountChecksTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_CLASSES
			("COUNT\\[CLASSES\\]",
			 new String[] { "COUNT[CLASSES]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountClassesTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_CONTAINERS
			("COUNT\\[CONTAINERS\\]",
			 new String[] { "COUNT[CONTAINERS]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountContainersTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_DOMAINS
			("COUNT\\[DOMAINS\\]",
			 new String[] { "COUNT[DOMAINS]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountDomainsTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_FEATSNATUREALL
			("COUNT\\[FEATSALL(?:\\.ALL|\\.HIDDEN|\\.VISIBLE)?\\]",
			 new String[] { "COUNT[FEATSALL]",
							"COUNT[FEATSALL.ALL]",
			 				"COUNT[FEATSALL.HIDDEN]",
					 		"COUNT[FEATSALL.VISIBLE]"},
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			boolean visible = !(expressionString.endsWith("HIDDEN]"));

			boolean hidden  = (expressionString.endsWith("HIDDEN]") ||
			                   expressionString.endsWith(".ALL]"));

			AbilityCategory abCat = AbilityCategory.FEAT;
			
			return new PCCountAbilitiesNatureAllTermEvaluator(
					expressionString,
					abCat,
					visible,
					hidden);
		}
	},

	COMPLETE_PC_COUNT_FEATSNATUREAUTO
			("COUNT\\[FEATSAUTO(?:\\.ALL|\\.HIDDEN|\\.VISIBLE)?\\]",
			 new String[] { "COUNT[FEATSAUTO]",
							"COUNT[FEATSAUTO.ALL]",
			 				"COUNT[FEATSAUTO.HIDDEN]",
					 		"COUNT[FEATSAUTO.VISIBLE]"},
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			boolean visible = !(expressionString.endsWith("HIDDEN]"));

			boolean hidden  = (expressionString.endsWith("HIDDEN]") ||
			                   expressionString.endsWith("ALL]"));

			AbilityCategory abCat = AbilityCategory.FEAT;
			
			return new PCCountAbilitiesNatureAutoTermEvaluator(
					expressionString,
					abCat,
					visible,
					hidden);
		}
	},

	
	COMPLETE_PC_COUNT_FEATSNATURENORMAL
			("COUNT\\[FEATS(?:\\.ALL|\\.HIDDEN|\\.VISIBLE)?\\]",
			 new String[] { "COUNT[FEATS]",
							"COUNT[FEATS.ALL]",
			 				"COUNT[FEATS.HIDDEN]",
					 		"COUNT[FEATS.VISIBLE]"},
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			boolean visible = !(expressionString.endsWith("HIDDEN]"));

			boolean hidden  = (expressionString.endsWith("HIDDEN]") ||
			                   expressionString.endsWith("ALL]"));

			AbilityCategory abCat = AbilityCategory.FEAT;

			return new PCCountAbilitiesNatureNormalTermEvaluator(
					expressionString,
					abCat,
					visible,
					hidden);
		}
	},


	COMPLETE_PC_COUNT_FEATSNATUREVIRTUAL
			("COUNT\\[VFEATS(?:\\.ALL|\\.HIDDEN|\\.VISIBLE)?\\]",
			 new String[] { "COUNT[VFEATS]",
							"COUNT[VFEATS.ALL]",
			 				"COUNT[VFEATS.HIDDEN]",
					 		"COUNT[VFEATS.VISIBLE]"},
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			boolean visible = !(expressionString.endsWith("HIDDEN]"));

			boolean hidden  = (expressionString.endsWith("HIDDEN]") ||
			                   expressionString.endsWith("ALL]"));

			AbilityCategory abCat = AbilityCategory.FEAT;
			
			return new PCCountAbilitiesNatureVirtualTermEvaluator(
					expressionString,
					abCat,
					visible,
					hidden);
		}
	},
	
	COMPLETE_PC_COUNT_FOLLOWERS
			("COUNT\\[FOLLOWERS\\]",
			 new String[] { "COUNT[FOLLOWERS]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountFollowersTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_LANGUAGES
			("COUNT\\[LANGUAGES\\]",
			 new String[] { "COUNT[LANGUAGES]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountLanguagesTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_MISC_COMPANIONS
			("COUNT\\[MISC\\.COMPANIONS\\]",
			 new String[] { "COUNT[MISC.COMPANIONS]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountMiscCompanionsTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_MISC_FUNDS
			("COUNT\\[MISC\\.FUNDS\\]",
			 new String[] { "COUNT[MISC.FUNDS]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountMiscFundsTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_MISC_MAGIC
			("COUNT\\[MISC\\.MAGIC\\]",
			 new String[] { "COUNT[MISC.MAGIC]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountMiscMagicTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_MOVE
			("COUNT\\[MOVE\\]",
			 new String[] { "COUNT[MOVE]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountMoveTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_NOTES
			("COUNT\\[NOTES\\]",
			 new String[] { "COUNT[NOTES]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountNotesTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_RACESUBTYPES
			("COUNT\\[RACESUBTYPES\\]",
			 new String[] { "COUNT[RACESUBTYPES]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountRaceSubTypesTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_SA
			("COUNT\\[SA\\]",
			 new String[] { "COUNT[SA]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountSABTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_SKILLS
			("COUNT\\[SKILLS\\]",
			 new String[] { "COUNT[SKILLS]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountSkillsTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_SPELLCLASSES
			("COUNT\\[SPELLCLASSES\\]",
			 new String[] { "COUNT[SPELLCLASSES]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountSpellClassesTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_SPELLRACE
			("COUNT\\[SPELLRACE\\]",
			 new String[] { "COUNT[SPELLRACE]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountSpellRaceTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_STATS
			("COUNT\\[STATS\\]",
			 new String[] { "COUNT[STATS]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountStatsTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_TEMPBONUSNAMES
			("COUNT\\[TEMPBONUSNAMES\\]",
			 new String[] { "COUNT[TEMPBONUSNAMES]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountTempBonusNamesTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_TEMPLATES
			("COUNT\\[TEMPLATES\\]",
			 new String[] { "COUNT[TEMPLATES]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountTemplatesTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_VISIBLETEMPLATES
			("COUNT\\[VISIBLETEMPLATES\\]",
			 new String[] { "COUNT[VISIBLETEMPLATES]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountVisibleTemplatesTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_COUNT_VISION
			("COUNT\\[VISION\\]",
			 new String[] { "COUNT[VISION]" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCCountVisionTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_ENCUMBERANCE
			("ENCUMBERANCE",
			 new String[] { "ENCUMBERANCE" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCEncumberanceTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_HD
			("HD",
			 new String[] { "HD" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCHDTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_HP
			("HP",
			 new String[] { "HP" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCHPTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_MAXCASTABLE
			("MAXCASTABLE",
			 new String[] { "MAXCASTABLE" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{
			if (src.startsWith("CLASS:"))
			{
				return new PCMaxCastableClassTermEvaluator(
						expressionString,
						src.substring(6));
			}
			else if (src.startsWith("DOMAIN:"))
			{
				return new PCMaxCastableDomainTermEvaluator(
						expressionString,
						src.substring(7));
			}
			else if (src.startsWith("SPELLTYPE:"))
			{
				return new PCMaxCastableSpellTypeTermEvaluator(
						expressionString,
						src.substring(10));
			}
			else if ("ANY".equals(src))
			{
				return new PCMaxCastableAnyTermEvaluator(
						expressionString);
			}
			
			StringBuilder sB = new StringBuilder();
			sB.append("MAXCASTABLE is not usable in ");
			sB.append(src);
			throw new TermEvaulatorException(sB.toString());
		}
	},

	COMPLETE_PC_MOVEBASE
			("MOVEBASE",
			 new String[] { "MOVEBASE" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCMoveBaseTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_PC_HEIGHT
			("PC\\.HEIGHT",
			 new String[] { "PC.HEIGHT" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCHeightTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_PC_WEIGHT
			("PC\\.WEIGHT",
			 new String[] { "PC.WEIGHT" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCWeightTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_PROFACCHECK
			("PROFACCHECK",
			 new String[] { "PROFACCHECK" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			String source = (src.startsWith("EQ:")) ? src.substring(3) : "";

			return new PCProfACCheckTermEvaluator(
					expressionString, 
					source);
		}
	},

	COMPLETE_PC_RACESIZE
			("RACESIZE",
			 new String[] { "RACESIZE" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCRaceSizeTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_SCORE
			("SCORE",
			 new String[] { "SCORE" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			try
			{
				int i = Integer.parseInt(src);
				return new FixedTermEvaluator(i);
			}
			catch (NumberFormatException e)
			{
				//OK
			}
			String source = (src.startsWith("STAT:")) ? src.substring(5) : "";
			
			return new PCScoreTermEvaluator(expressionString, source);
		}
	},

	COMPLETE_PC_SHIELDACCHECK
			("SHIELDAC{1,2}HECK",
			 new String[] { "SHIELDACCHECK", "SHIELDACHECK" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCShieldACcheckTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_SIZEMOD
			("SIZEMOD|SIZE",
			 new String[] { "SIZEMOD", "SIZE" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			if ("SIZEMOD".equals(matchedSection))
			{
				return new PCSizeModEvaluatorTermEvaluator(expressionString);				
			}
			else
			{
				return new PCSizeTermEvaluator(expressionString);	
			}
		}
	},

	COMPLETE_PC_SPELLBASESTAT
			("SPELLBASESTATSCORE|SPELLBASESTAT",
			 new String[] { "SPELLBASESTAT", "SPELLBASESTATSCORE" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			String source = (src.startsWith("CLASS:")) ? src.substring(6) : "";

			if (expressionString.endsWith("SCORE"))
			{
				return new PCSPellBaseStatScoreEvaluatorTermEvaluator(expressionString, source);
			}
			return new PCSPellBaseStatTermEvaluator(expressionString, source);
		}
	},

	COMPLETE_PC_SPELLLEVEL
			("SPELLLEVEL",
			 new String[] { "SPELLLEVEL" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCSpellLevelTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_TL
			("TL",
			 new String[] { "TL" },
			 true) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			return new PCTLTermEvaluator(expressionString);
		}
	},

	COMPLETE_PC_FAVCLASSLEVEL
		("FAVCLASSLEVEL", 
			new String[]{"FAVCLASSLEVEL"},
			true) {

		public TermEvaluator getTermEvaluator(final String expressionString,
			final String src, final String matchedSection)
		{

			return new PCFavClassLevelTermEvaluator(expressionString);
		}
	},

	PC_CAST_ATWILL
		("ATWILL", 
			new String[]{"ATWILL"}, 
			true) {

		public TermEvaluator getTermEvaluator(final String expressionString,
			final String src, final String matchedSection)
		{
			return new PCCastTimesAtWillTermEvaluator(expressionString);
		}
	},

	START_PC_BL
			("BL[.=]?",
			 new String[] { "BL.", "BL=", "BL" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			String classString;

			if (matchedSection.length() == expressionString.length())
			{
				classString = (src.startsWith("CLASS:")) ? src.substring(6) : "";
			}
			else 
			{
				classString = expressionString.substring(matchedSection.length());
			}
			
			return new PCBLTermEvaluator(
					expressionString,
					classString);
		}
	},

	START_PC_CL_BEFORELEVEL
			("CL;BEFORELEVEL[.=]",
			 new String[] { "CL;BEFORELEVEL.", "CL;BEFORELEVEL=" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			if (!src.startsWith("CLASS:")) {
				StringBuilder sB = new StringBuilder();
				sB.append(matchedSection);
				sB.append(" may only be used in a Class");
				throw new TermEvaulatorException(sB.toString());				
			}

			int i;
			
			try
			{
				i = Integer.parseInt(expressionString.substring(15));
			}
			catch (NumberFormatException e)
			{
				StringBuilder sB = new StringBuilder();
				sB.append("Badly formed formula ");
				sB.append(expressionString);
				sB.append(" in ");
				sB.append(src);
				sB.append(" should have an integer following ");
				sB.append(matchedSection);
				throw new TermEvaulatorException(sB.toString());
			}

			return new PCCLBeforeLevelTermEvaluator(
					expressionString,
					src.substring(6),
					i);
		}
	},

	START_PC_CLASSLEVEL
			("CLASSLEVEL[.=]",
			 new String[] { "CLASSLEVEL.", "CLASSLEVEL=" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			String exp = expressionString.replace('{', '(').replace('}', ')');

			String classString = exp.substring(11);

			return new PCCLTermEvaluator(
					expressionString,
					classString);
		}
	},

	START_PC_CLASS
			("CLASS[.=]",
			 new String[] { "CLASS.", "CLASS=" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			return new PCHasClassTermEvaluator(
					expressionString,
					expressionString.substring(6));
		}
	},

	START_PC_CL
			("CL[.=]?",
			 new String[] { "CL.", "CL=", "CL" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			String classString;

			if (2 == expressionString.length())
			{
				if (!src.startsWith("CLASS:")) {
					StringBuilder sB = new StringBuilder();
					sB.append(matchedSection);
					sB.append(" may only be used in a Class");
					throw new TermEvaulatorException(sB.toString());				
				}
				
				classString = (src.startsWith("CLASS:")) ? src.substring(6) : "";
			}
			else
			{
				classString = expressionString.substring(3);
			}

			return new PCCLTermEvaluator(
					expressionString,
					classString);
		}
	},

	START_PC_COUNT_EQTYPE
			("COUNT\\[EQTYPE\\.?",
			 new String[] { "COUNT[EQTYPE", "COUNT[EQTYPE." },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			// The types string inside the brackets
			String typesString = 
					TermUtilities.extractContentsOfBrackets(
							expressionString,
							src,
							matchedSection.length());
			
			// In the case of the empty string, the split will give us a one
			// element array containing only the empty string (which is the
			// desired result)
			String[] fullTypes = typesString.split("\\.", -1);

			int merge = "MERGENONE".equals(fullTypes[0]) ? Constants.MERGE_NONE :
						"MERGELOC".equals(fullTypes[0])  ? Constants.MERGE_LOCATION :
						Constants.MERGE_ALL;

			int first = (Constants.MERGE_ALL == merge) ? 0 : 1;

			String[] types;
			if (fullTypes.length > first) {
				TermUtilities.checkEqTypeTypesArray(
						expressionString,
						fullTypes,
						first);

				int len = fullTypes.length - first;
				types   = new String[len];
				System.arraycopy(fullTypes, first, types, 0, len);
			}
			else
			{
				types = new String[] {""};
			}

			return new PCCountEqTypeTermEvaluator(
					expressionString,
					types,
					merge);
		}
	},
	
	START_PC_COUNT_EQUIPMENT
			("COUNT\\[EQUIPMENT\\.?",
			 new String[] { "COUNT[EQUIPMENT.", "COUNT[EQUIPMENT" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			// The types string inside the brackets
			String typesString = 
					TermUtilities.extractContentsOfBrackets(
							expressionString,
							src,
							matchedSection.length());
			
			// In the case of the empty string, the split will give us a one
			// element array containing only the empty string (which is the
			// desired result)
			String[] fullTypes = typesString.split("\\.");

			int merge = "MERGENONE".equals(fullTypes[0]) ? Constants.MERGE_NONE :
						"MERGELOC".equals(fullTypes[0])  ? Constants.MERGE_LOCATION :
						Constants.MERGE_ALL;

			int first = (Constants.MERGE_ALL == merge) ? 0 : 1;

			String[] types;
			if (fullTypes.length > first) {
				TermUtilities.checkEquipmentTypesArray(
						expressionString,
						fullTypes,
						first);

				int len = fullTypes.length - first;
				types   = new String[len];
				System.arraycopy(fullTypes, first, types, 0, len);
			} else {
				types = new String[] {""};
			}
			
			return new PCCountEquipmentTermEvaluator(
					expressionString,
					types,
					merge);
		}
	},

	START_PC_COUNT_FEATTYPE
			("COUNT\\[(?:FEATAUTOTYPE|FEATNAME|FEATTYPE|VFEATTYPE)[.=]",
			 new String[] { "COUNT[FEATAUTOTYPE.", "COUNT[FEATAUTOTYPE=",
							"COUNT[FEATNAME.", "COUNT[FEATNAME=",
							"COUNT[FEATTYPE.", "COUNT[FEATTYPE=", 
			 				"COUNT[VFEATTYPE.", "COUNT[VFEATTYPE=" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			Matcher subtokenMat = subtokenPat.matcher(expressionString);

			if (!subtokenMat.find())
			{
				StringBuilder sB = new StringBuilder();
				sB.append("Impossible error while parsing \"");
				sB.append(expressionString);
				sB.append("\" in ");
				sB.append(src);
				throw new TermEvaulatorException(sB.toString());				
			}

			int start = expressionString.startsWith("COUNT[FEATA") ? 19 :
						expressionString.startsWith("COUNT[V")     ? 16 : 15;
			
			// The types string inside the brackets
			String typesString = 
					TermUtilities.extractContentsOfBrackets(expressionString, src, start); 
			
			// In the case of the empty string, the split will give us a one
			// element array containing only the empty string (which is the
			// desired result)
			String[] types = typesString.split("\\.", -1);

			boolean visible = !(expressionString.endsWith("HIDDEN]"));

			boolean hidden  = (expressionString.endsWith("HIDDEN]") ||
			                   expressionString.endsWith("ALL]"));

			if ("ALL".equals(types[types.length - 1])    ||
				"HIDDEN".equals(types[types.length - 1]) ||
				"VISIBLE".equals(types[types.length - 1]))
			{
				if (types.length > 1)
				{
					int len = types.length - 1;
					String[] t = new String[len];
					System.arraycopy(types, 0, t, 0, len);
					types = t;
				}
				else
				{
					types = new String[] {""};
				}
			}

			AbilityCategory abCat = AbilityCategory.FEAT;

			if ("FEATAUTOTYPE".equals(subtokenMat.group()))
			{
				return new PCCountAbilitiesTypeNatureAutoTermEvaluator(
						expressionString,
						abCat,
						types,
						visible,
						hidden);
			}
			else if ("FEATNAME".equals(subtokenMat.group()))
			{
				return new PCCountAbilityNameTermEvaluator(
						expressionString,
						abCat,
						types[0],
						visible,
						hidden);
			}
			else if ("FEATTYPE".equals(subtokenMat.group()))
			{
				return new PCCountAbilitiesTypeNatureAllTermEvaluator(
						expressionString,
						abCat,
						types, 
						visible,
						hidden);
			}
			else
			{
				return new PCCountAbilitiesTypeNatureVirtualTermEvaluator(
						expressionString,
						abCat,
						types, 
						visible,
						hidden);
			}
		}
	},

	START_PC_COUNT_FOLLOWERTYPE
			("COUNT\\[FOLLOWERTYPE\\.",
			 new String[] { "COUNT[FOLLOWERTYPE." },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			// The types string inside the brackets
			String typesString = 
					TermUtilities.extractContentsOfBrackets(expressionString, src, 19); 
			
			String[] types = typesString.split("\\.", 3);

			if (types.length == 1)
			{
				// This covers COUNT[FOLLOWERTYPE.Animal Companions] syntax
				return new PCCountFollowerTypeTermEvaluator(
						expressionString,
						types[0]);
			}
			else if (types.length == 2)
			{
				StringBuilder sB = new StringBuilder();
				sB.append("Badly formed formula ");
				sB.append(expressionString);
				sB.append(" in ");
				sB.append(src);
				throw new TermEvaulatorException(sB.toString());
			}
			else
			{
				Matcher numMat = numPat.matcher(types[1]);

				if (!numMat.find())
				{
					StringBuilder sB = new StringBuilder();
					sB.append("Badly formed formula ");
					sB.append(expressionString);
					sB.append(" in ");
					sB.append(src);
					throw new TermEvaulatorException(sB.toString());
				}
			
				String newCount = "COUNT[" + types[2] + "]";

				// This will do COUNT[FOLLOWERTYPE.Animal Companions.0.xxx],
				// returning the same as COUNT[xxx] if applied to the right follower
				return new PCCountFollowerTypeTransitiveTermEvaluator(
						expressionString,
						types[0],
						Integer.valueOf(numMat.group()),
						newCount);
			}
		}
	},

	START_PC_COUNT_SKILLTYPE
			("COUNT\\[SKILLTYPE[.=]",
			 new String[] { "COUNT[SKILLTYPE.", "COUNT[SKILLTYPE=" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			String type = TermUtilities.extractContentsOfBrackets(expressionString, src, 16); 

			return new PCSkillTypeTermEvaluator(
					expressionString,
					type);
		}
	},

	START_PC_COUNT_SPELLBOOKS
			("COUNT\\[SPELLBOOKS",
			 new String[] { "COUNT[SPELLBOOKS" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			TermUtilities.extractContentsOfBrackets(expressionString, src, 16);

			return new PCCountSpellbookTermEvaluator(
					expressionString);
		}
	},

	START_PC_COUNT_SPELLSINBOOK
			("COUNT\\[SPELLSINBOOK",
			 new String[] { "COUNT[SPELLSINBOOK" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString,
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			return new PCCountSpellsInbookTermEvaluator(
					expressionString,
					TermUtilities.extractContentsOfBrackets(expressionString, src, 19));
		}
	},

	START_PC_COUNT_SPELLSKNOWN
			("COUNT\\[SPELLSKNOWN",
			 new String[] { "COUNT[SPELLSKNOWN" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString,
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			// The spells string inside the brackets
			String spellsString = 
					TermUtilities.extractContentsOfBrackets(expressionString, src, 17);

			// make an array with one element in case we need it in the
			// catch block.  The string could legitimatey be empty in which
			// case a numberFormatException will be thrown
			int[] nums = { -1 };

			if (spellsString.length() > 1 && spellsString.startsWith("."))
			{
				String s = spellsString.substring(1);
				try
				{
					nums = TermUtilities.convertToIntegers(
							expressionString,
							s,
							matchedSection.length(),
							2);
				}
				catch (NumberFormatException e)
				{
					// the -1 means get them all (i.e. no filtering by class
					//  or spellbook)
					nums[0] = -1;
				}
			}

			return new PCCountSpellsKnownTermEvaluator(
					expressionString,
					nums);
		}
	},

	START_PC_COUNT_SPELLSLEVELSINBOOK
			("COUNT\\[SPELLSLEVELSINBOOK",
			 new String[] { "COUNT[SPELLSLEVELSINBOOK" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			String intString = 
					TermUtilities.extractContentsOfBrackets(
							expressionString,
							src,
							24);

			int[] nums = new int[] {-1};

			if (intString.length() > 1 && intString.startsWith("."))
			{
				String s = intString.substring(1);
				try
				{
					nums = TermUtilities.convertToIntegers(
							expressionString,
							s,
							matchedSection.length(),
							2);
				}
				catch (NumberFormatException e)
				{
					// the -1 means get them all (i.e. no filtering by class
					//  or spellbook)
					nums[0] = -1;
				}
			}
			else
			{
				StringBuilder sB = new StringBuilder();
				sB.append("Badly formed formula ");
				sB.append(expressionString);
				sB.append(" following ");
				sB.append(matchedSection);
				sB.append(" should be 2 ");
				sB.append("integers separated by dots");
				throw new TermEvaulatorException(sB.toString());
			}

			return new PCCountSpellsLevelsInBookTermEvaluator(
					expressionString, nums);
		}
	},

	START_PC_COUNT_SPELLTIMES
			("COUNT\\[SPELLTIMES",
			 new String[] { "COUNT[SPELLTIMES" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			String intString = 
					TermUtilities.extractContentsOfBrackets(expressionString, src, 16);

			int[] nums = new int[] {-1};

			if (intString.length() > 1 && intString.startsWith("."))
			{
				String s = intString.substring(1);
				try
				{
					nums = TermUtilities.convertToIntegers(
							expressionString,
							s,
							matchedSection.length(),
							4);
				}
				catch (NumberFormatException e)
				{
					// the -1 means get them all (i.e. no filtering by class
					//  or spellbook)
					nums[0] = -1;
				}
			}
			else
			{
				StringBuilder sB = new StringBuilder();
				sB.append("Badly formed formula ");
				sB.append(expressionString);
				sB.append(" following ");
				sB.append(matchedSection);
				sB.append(" should be 4 ");
				sB.append("integers separated by dots");
				throw new TermEvaulatorException(sB.toString());
			}
			
			return new PCCountSpellTimesTermEvaluator(
					expressionString,
					nums);
		}
	},

	START_PC_EQTYPE
			("EQTYPE",
			 new String[] { "EQTYPE" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			return new PCEqTypeTermEvaluator(expressionString);
		}
	},

	START_PC_HASDEITY
			("HASDEITY:",
			 new String[] { "HASDEITY:" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			return new PCHasDeityTermEvaluator(
					expressionString,
					expressionString.substring(9));
		}
	},

	START_PC_HASFEAT
			("HASFEAT:",
			 new String[] { "HASFEAT:" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			return new PCHasFeatTermEvaluator(
					expressionString,
					expressionString.substring(8));
		}
	},

	START_PC_MAXLEVEL("MAXLEVEL", new String[]{"MAXLEVEL"}, true) {

		public TermEvaluator getTermEvaluator(final String expressionString,
			final String src, final String matchedSection)
		{

			if (src.startsWith("CLASS:") || src.startsWith("CLASS|"))
			{
				return new PCMaxLevelTermEvaluator(expressionString, src.substring(6));
			}
			else
			{
				Logging.errorPrint("MAXLEVEL term called without a CLASS source");
				return new PCMaxLevelTermEvaluator(expressionString, "");
			}
		}
	},

	START_PC_MODEQUIP
			("MODEQUIP",
			 new String[] { "MODEQUIP" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			return new PCModEquipTermEvaluator(
					expressionString, 
					expressionString.substring(8));
		}
	},

	START_PC_MOVE
			("MOVE\\[",
			 new String[] { "MOVE[" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			return new PCMovementTermEvaluator(
					expressionString, 
					TermUtilities.extractContentsOfBrackets(expressionString, src, 5));			
		}
	},

	START_PC_PC_SIZE
			("PC\\.SIZE(?:\\.INT)?",
			 new String[] { "PC.SIZE.INT", "PC.SIZE" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) {

			if (matchedSection.length() == 11)
			{
				if (src.startsWith("EQ:"))
				{
					return new PCSizeIntEQTermEvaluator(
							expressionString,
							src.substring(3));
				}
				else
				{
					return new PCSizeIntTermEvaluator(expressionString);
				}
			}
			else
			{
				return new PCSizeTermEvaluator(expressionString);
			}
		}
	},

	START_PC_SKILLRANK
			("SKILLRANK[.=]",
			 new String[] { "SKILLRANK.", "SKILLRANK=" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			String skillString = 
					expressionString.substring(10)
							.replace('{', '(').replace('}', ')');
			
			return new PCSkillRankTermEvaluator(
					expressionString,
					skillString);
		}
	},

	START_PC_SKILLTOTAL
			("SKILLTOTAL[.=]",
			 new String[] { "SKILLTOTAL.", "SKILLTOTAL=" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			String skillString = 
					expressionString.substring(11)
							.replace('{', '(').replace('}', ')');
			
			return new PCSkillTotalTermEvaluator(
					expressionString,
					skillString);
		}
	},

	START_PC_VARDEFINED
			("VARDEFINED:",
			 new String[] { "VARDEFINED:" },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString, 
				final String src, 
				final String matchedSection) throws TermEvaulatorException
		{

			String varString = expressionString.substring(11);

			return new PCVarDefinedTermEvaluator(
					expressionString,
					varString);
		}
	},

	START_PC_WEIGHT
			("WEIGHT\\.",
			 new String[] { "WEIGHT." },
			 false) {

		public TermEvaluator getTermEvaluator(
				final String expressionString,
				final String src,
				final String matchedSection) throws TermEvaulatorException
		{

			// The type of weight we want the value for
			String valString = expressionString.substring(7);

			if ("CARRIED".equals(valString))
			{
				return new PCCarriedWeightTermEvaluator(expressionString);
			}
			else if ("EQUIPPED".equals(valString))
			{
				// TODO: not carried, equipped!
				return new PCCarriedWeightTermEvaluator (expressionString);
			}
			else if ("PC".equals(valString))
			{
				return new PCWeightTermEvaluator(expressionString);
			}
			else if ("TOTAL".equals(valString))
			{
				// total weight of PC and all carried equipment
				return new PCTotalWeightTermEvaluator(expressionString);
			}

			StringBuilder sB = new StringBuilder();
			sB.append("invalid string following WEIGHT. in ");
			sB.append(expressionString);

			throw new TermEvaulatorException(sB.toString());
		}
	},

	COMPLETE_PC_BONUSLANG("BONUSLANG", new String[] { "BONUSLANG" }, true)
	{

		public TermEvaluator getTermEvaluator(final String expressionString,
				final String src, final String matchedSection)
		{
			return new PCBonusLangTermEvaluator(expressionString);
		}
	};

	static String subtokenString = "(FEATAUTOTYPE|FEATNAME|FEATTYPE|VFEATTYPE)";
	static Pattern subtokenPat = Pattern.compile(subtokenString);

	static Pattern numPat = Pattern.compile("\\d+");

	private String   termConstructorPattern;
	private String[] termConstructorKeys;
	private boolean  patternMatchesEntireTerm;

	TermEvaluatorBuilderPCVar(
			String pattern,
			String[] keys,
			boolean matchEntireTerm)
	{
		termConstructorPattern   = pattern;
		termConstructorKeys      = keys;
		patternMatchesEntireTerm = matchEntireTerm;
	}

	public String getTermConstructorPattern()
	{
		return termConstructorPattern;
	}

	public String[] getTermConstructorKeys()
	{
		return termConstructorKeys;
	}

	public boolean isEntireTerm()
	{
		return patternMatchesEntireTerm;
	}
}
