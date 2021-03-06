/*
 *  Initiative - A role playing utility to track turns
 *  Copyright (C) 2002 Devon D Jones
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *  The author of this program grants you the ability to use this code
 *  in conjunction with code that is covered under the Open Gaming License
 *
 * PreferencesDamagePanel.java
 *
 * Created on July 11, 2003, 4:34 PM
 */
package plugin.experience.gui;

import pcgen.core.SettingsHandler;
import plugin.experience.ExperienceAdjusterPlugin;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Font;

/**
 *
 * @author soulcatcher
 */
public class PreferencesExperiencePanel extends gmgen.gui.PreferencesPanel
{

	public static final int EXPERIENCE_3 = 1;
	public static final int EXPERIENCE_35 = 2;

	private JPanel mainPanel;
	private JPanel expPanel;
	private ButtonGroup experienceGroup;
	private JRadioButton experienceRB1;
	private JRadioButton experienceRB2;

	/** Creates new form PreferencesDamagePanel */
	public PreferencesExperiencePanel()
	{
		initComponents();
		initPreferences();
	}

	public void setExperience(int exp)
	{
		if (exp == EXPERIENCE_3)
		{
			experienceRB1.setSelected(true);
		}
		else if (exp == EXPERIENCE_35)
		{
			experienceRB2.setSelected(true);
		}
	}

	public int getExperience()
	{
		int returnVal = 0;

		if (experienceRB1.isSelected())
		{
			returnVal = EXPERIENCE_3;
		}
		else if (experienceRB2.isSelected())
		{
			returnVal = EXPERIENCE_35;
		}

		return returnVal;
	}

	public void applyPreferences()
	{
		SettingsHandler.setGMGenOption(ExperienceAdjusterPlugin.LOG_NAME
			+ ".ExperienceType", getExperience());
	}

	public void initPreferences()
	{
		setExperience(SettingsHandler.getGMGenOption(
			ExperienceAdjusterPlugin.LOG_NAME + ".ExperienceType",
			EXPERIENCE_35));
	}

	@Override
	public String toString()
	{
		return "Experience";
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents()
	{ //GEN-BEGIN:initComponents
		mainPanel = new JPanel();

		expPanel = new JPanel();
		experienceGroup = new ButtonGroup();
		experienceRB1 = new JRadioButton();
		experienceRB2 = new JRadioButton();

		setLayout(new BorderLayout());

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		expPanel.setLayout(new BoxLayout(expPanel, BoxLayout.Y_AXIS));

		expPanel.setBorder(new TitledBorder(null,
			"Experience Calculation Type", TitledBorder.DEFAULT_JUSTIFICATION,
			TitledBorder.DEFAULT_POSITION, new Font("Dialog", 1, 11)));
		experienceRB1.setSelected(true);
		experienceRB1
			.setText("Calculate total experience by party level, divide equally (3rd Ed, Modern)");
		experienceGroup.add(experienceRB1);
		expPanel.add(experienceRB1);

		experienceRB2
			.setText("Calculate experience by individual PC level, divide total by number of PCs (3.5 Ed)");
		experienceGroup.add(experienceRB2);
		expPanel.add(experienceRB2);

		mainPanel.add(expPanel);

		JScrollPane jScrollPane1 = new JScrollPane();
		jScrollPane1.setViewportView(mainPanel);
		add(jScrollPane1, BorderLayout.CENTER);
	}
	//GEN-END:initComponents
}