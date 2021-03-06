/*
 * Copyright (c) Thomas Parker, 2009.
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
package pcgen.cdom.facet;


public class FacetInitialization
{

	public static void initialize()
	{
		doBridges();
		TemplateFacet templateFacet = FacetLibrary
				.getFacet(TemplateFacet.class);
		ConditionalTemplateFacet conditionalTemplateFacet = FacetLibrary
				.getFacet(ConditionalTemplateFacet.class);
		RaceFacet raceFacet = FacetLibrary.getFacet(RaceFacet.class);
		ClassFacet classFacet = FacetLibrary.getFacet(ClassFacet.class);
		ClassLevelFacet classLevelFacet = FacetLibrary
				.getFacet(ClassLevelFacet.class);
		CampaignFacet campaignFacet = FacetLibrary
				.getFacet(CampaignFacet.class);
		ExpandedCampaignFacet expandedCampaignFacet = FacetLibrary
				.getFacet(ExpandedCampaignFacet.class);
		EquipmentFacet equipmentFacet = FacetLibrary
				.getFacet(EquipmentFacet.class);
		EquippedEquipmentFacet equippedFacet = FacetLibrary
				.getFacet(EquippedEquipmentFacet.class);
		NaturalEquipmentFacet naturalEquipmentFacet = FacetLibrary
				.getFacet(NaturalEquipmentFacet.class);
		SourcedEquipmentFacet activeEquipmentFacet = FacetLibrary
				.getFacet(SourcedEquipmentFacet.class);
		ActiveEqModFacet activeEqModFacet = FacetLibrary
				.getFacet(ActiveEqModFacet.class);

		AlignmentFacet alignmentFacet = FacetLibrary
				.getFacet(AlignmentFacet.class);
		BioSetFacet bioSetFacet = FacetLibrary.getFacet(BioSetFacet.class);
		CheckFacet checkFacet = FacetLibrary.getFacet(CheckFacet.class);

		LanguageFacet languageFacet = FacetLibrary
				.getFacet(LanguageFacet.class);
		FreeLanguageFacet freeLangFacet = FacetLibrary
				.getFacet(FreeLanguageFacet.class);
		AutoLanguageFacet autoLangFacet = FacetLibrary
				.getFacet(AutoLanguageFacet.class);
		AddLanguageFacet addLangFacet = FacetLibrary
				.getFacet(AddLanguageFacet.class);
		SkillLanguageFacet skillLangFacet = FacetLibrary
				.getFacet(SkillLanguageFacet.class);
		WeaponProfFacet weaponProfFacet = FacetLibrary
				.getFacet(WeaponProfFacet.class);

		LevelFacet levelFacet = FacetLibrary.getFacet(LevelFacet.class);
		SizeFacet sizeFacet = FacetLibrary.getFacet(SizeFacet.class);
		BonusChangeFacet bonusChangeFacet = FacetLibrary
				.getFacet(BonusChangeFacet.class);
		DeityFacet deityFacet = FacetLibrary.getFacet(DeityFacet.class);
		DomainFacet domainFacet = FacetLibrary.getFacet(DomainFacet.class);
		CompanionModFacet companionModFacet = FacetLibrary
				.getFacet(CompanionModFacet.class);
		StatFacet statFacet = FacetLibrary.getFacet(StatFacet.class);
		SkillFacet skillFacet = FacetLibrary.getFacet(SkillFacet.class);
		ActiveAbilityFacet abFacet = FacetLibrary
				.getFacet(ActiveAbilityFacet.class);
		AutoListShieldProfFacet splFacet = FacetLibrary
				.getFacet(AutoListShieldProfFacet.class);
		ShieldProfProviderFacet sppFacet = FacetLibrary
				.getFacet(ShieldProfProviderFacet.class);
		AutoListArmorProfFacet aplFacet = FacetLibrary
				.getFacet(AutoListArmorProfFacet.class);
		ArmorProfProviderFacet appFacet = FacetLibrary
				.getFacet(ArmorProfProviderFacet.class);

		NaturalWeaponProfFacet nwpFacet = FacetLibrary
				.getFacet(NaturalWeaponProfFacet.class);
		AutoListWeaponProfFacet alWeaponProfFacet = FacetLibrary
				.getFacet(AutoListWeaponProfFacet.class);
		BonusWeaponProfFacet wpBonusFacet = FacetLibrary
				.getFacet(BonusWeaponProfFacet.class);
		UserEquipmentFacet userEquipmentFacet = FacetLibrary
				.getFacet(UserEquipmentFacet.class);
		NaturalWeaponFacet naturalWeaponFacet =
				FacetLibrary.getFacet(NaturalWeaponFacet.class);
		EquipSetFacet equipSetFacet =
				FacetLibrary.getFacet(EquipSetFacet.class);
		ChooseDriverFacet chooseDriverFacet = FacetLibrary
				.getFacet(ChooseDriverFacet.class);

		CDOMObjectConsolidationFacet cdomObjectFacet = FacetLibrary
				.getFacet(CDOMObjectConsolidationFacet.class);
		CDOMObjectSourceFacet cdomSourceFacet = FacetLibrary
				.getFacet(CDOMObjectSourceFacet.class);
		CharacterConsolidationFacet charObjectFacet = FacetLibrary
				.getFacet(CharacterConsolidationFacet.class);
		EquipmentConsolidationFacet eqObjectFacet = FacetLibrary
				.getFacet(EquipmentConsolidationFacet.class);
		GrantedAbilityFacet grantedAbilityFacet = FacetLibrary
				.getFacet(GrantedAbilityFacet.class);
		DirectAbilityFacet directAbilityFacet = FacetLibrary
				.getFacet(DirectAbilityFacet.class);
		ConditionallyGrantedAbilityFacet cabFacet = FacetLibrary
				.getFacet(ConditionallyGrantedAbilityFacet.class);

		autoLangFacet.addDataFacetChangeListener(languageFacet);
		freeLangFacet.addDataFacetChangeListener(languageFacet);
		addLangFacet.addDataFacetChangeListener(languageFacet);
		skillLangFacet.addDataFacetChangeListener(languageFacet);

		equipmentFacet.addDataFacetChangeListener(naturalEquipmentFacet);
		equippedFacet.addDataFacetChangeListener(activeEquipmentFacet);
		naturalEquipmentFacet.addDataFacetChangeListener(activeEquipmentFacet);
		activeEquipmentFacet.addDataFacetChangeListener(activeEqModFacet);

		campaignFacet.addDataFacetChangeListener(expandedCampaignFacet);

		wpBonusFacet.addDataFacetChangeListener(weaponProfFacet);
		nwpFacet.addDataFacetChangeListener(weaponProfFacet);
		alWeaponProfFacet.addDataFacetChangeListener(weaponProfFacet);

		domainFacet.addDataFacetChangeListener(-1000, chooseDriverFacet);
		raceFacet.addDataFacetChangeListener(-1000, chooseDriverFacet);
		templateFacet.addDataFacetChangeListener(-1000, chooseDriverFacet);

		aplFacet.addDataFacetChangeListener(appFacet);
		splFacet.addDataFacetChangeListener(sppFacet);

		charObjectFacet.addDataFacetChangeListener(naturalWeaponFacet);
		naturalWeaponFacet.addDataFacetChangeListener(equipmentFacet);
		naturalWeaponFacet.addDataFacetChangeListener(userEquipmentFacet);
		naturalWeaponFacet.addDataFacetChangeListener(equipSetFacet);

		classFacet.addLevelChangeListener(classLevelFacet);
		classFacet.addLevelChangeListener(levelFacet);
		levelFacet.addLevelChangeListener(conditionalTemplateFacet);

		directAbilityFacet.addDataFacetChangeListener(grantedAbilityFacet);
		cabFacet.addDataFacetChangeListener(grantedAbilityFacet);

		raceFacet.addDataFacetChangeListener(bioSetFacet);

		raceFacet.addDataFacetChangeListener(sizeFacet);
		templateFacet.addDataFacetChangeListener(sizeFacet);
		conditionalTemplateFacet.addDataFacetChangeListener(sizeFacet);
		bonusChangeFacet.addBonusChangeListener(sizeFacet, "SIZEMOD", "NUMBER");

		expandedCampaignFacet.addDataFacetChangeListener(charObjectFacet);
		alignmentFacet.addDataFacetChangeListener(charObjectFacet);
		bioSetFacet.addDataFacetChangeListener(charObjectFacet);
		checkFacet.addDataFacetChangeListener(charObjectFacet);
		classFacet.addDataFacetChangeListener(charObjectFacet);
		deityFacet.addDataFacetChangeListener(charObjectFacet);
		domainFacet.addDataFacetChangeListener(charObjectFacet);
		abFacet.addDataFacetChangeListener(charObjectFacet);
		raceFacet.addDataFacetChangeListener(charObjectFacet);
		sizeFacet.addDataFacetChangeListener(charObjectFacet);
		skillFacet.addDataFacetChangeListener(charObjectFacet);
		statFacet.addDataFacetChangeListener(charObjectFacet);
		templateFacet.addDataFacetChangeListener(charObjectFacet);

		// weaponProfList is still just a list of Strings
		// results.addAll(getWeaponProfList());
		classLevelFacet.addDataFacetChangeListener(charObjectFacet);
		conditionalTemplateFacet.addDataFacetChangeListener(charObjectFacet);
		grantedAbilityFacet.addDataFacetChangeListener(charObjectFacet);
		companionModFacet.addDataFacetChangeListener(charObjectFacet);

		activeEquipmentFacet.addDataFacetChangeListener(eqObjectFacet);
		activeEqModFacet.addDataFacetChangeListener(eqObjectFacet);

		eqObjectFacet.addDataFacetChangeListener(cdomObjectFacet);
		charObjectFacet.addDataFacetChangeListener(cdomObjectFacet);

		cdomObjectFacet.addDataFacetChangeListener(nwpFacet);
		cdomSourceFacet.addDataFacetChangeListener(autoLangFacet);
	}

	private static void doBridges()
	{
		/*
		 * TODO These are required because they are "bridges" - meaning they
		 * refer to others, but no one refers to them. Need to consider if these
		 * need to be redesigned...
		 */
		FacetLibrary.getFacet(AgeSetKitFacet.class);
		FacetLibrary.getFacet(DomainSpellListFacet.class);
		FacetLibrary.getFacet(NaturalEquipSetFacet.class);
		FacetLibrary.getFacet(ShieldProfFacet.class);
		FacetLibrary.getFacet(ArmorProfFacet.class);
		FacetLibrary.getFacet(MonsterClassFacet.class);
		FacetLibrary.getFacet(KitChoiceFacet.class);
		FacetLibrary.getFacet(RegionChoiceFacet.class);
		FacetLibrary.getFacet(AddFacet.class);
		FacetLibrary.getFacet(RemoveFacet.class);
		FacetLibrary.getFacet(CalcBonusFacet.class);
		FacetLibrary.getFacet(DomainSpellsFacet.class);
		FacetLibrary.getFacet(ObjectAdditionFacet.class);
		FacetLibrary.getFacet(AddLevelFacet.class);
		//This one is a just in case
		FacetLibrary.getFacet(ChangeProfFacet.class);
	}
}
