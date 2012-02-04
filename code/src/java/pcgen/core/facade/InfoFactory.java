/*
 * InfoFactory.java
 * Copyright 2011 Connor Petty <cpmeister@users.sourceforge.net>
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
 * Created on Feb 6, 2011, 12:41:36 PM
 */
package pcgen.core.facade;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public interface InfoFactory
{

	public String getLevelAdjustment(TemplateFacade template);

	public String getModifier(TemplateFacade template);

	public String getPreReqHTML(TemplateFacade template);

	public float getCost(EquipmentFacade equipment);

	public float getWeight(EquipmentFacade equipment);

	public String getPreReqHTML(RaceFacade race);

	public String getStatAdjustments(RaceFacade race);

	public String getVision(RaceFacade race);

	public String getFavoredClass(RaceFacade race);

	public String getLevelAdjustment(RaceFacade race);

	public String getHTMLInfo(RaceFacade race);

	public String getHTMLInfo(ClassFacade pcClass, ClassFacade parentClass);

	public String getHTMLInfo(SkillFacade skill);

	public String getHTMLInfo(AbilityFacade ability);

	public String getHTMLInfo(DeityFacade deity);

	public String getHTMLInfo(DomainFacade domain);

	/**
	 * Produce the HTML information string for an item for equipment.
	 * @param equipFacade The equipment item
	 * @return The HTML information string
	 */
	public String getHTMLInfo(EquipmentFacade equipFacade);

	public String getHTMLInfo(TemplateFacade template);

	public String getHTMLInfo(SpellFacade spell);

	/**
	 * Produce the HTML information string for spell book or spell list.
	 * @param name The spell book or spell list.
	 * @return The HTML information string
	 */
	public String getSpellBookInfo(String name);

}
