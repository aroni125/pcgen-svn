/*
 * SourcesPanel.java
 * Copyright 2008 (C) James Dempsey
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
 * Created on 16/11/2008 11:00:00
 *
 * $Id: $
 */
package pcgen.gui.prefs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.SourceFormat;
import pcgen.core.Globals;
import pcgen.core.SettingsHandler;
import pcgen.core.utils.MessageType;
import pcgen.core.utils.ShowMessageDelegate;
import pcgen.gui.utils.JComboBoxEx;
import pcgen.gui.utils.Utility;
import pcgen.util.Logging;
import pcgen.util.PropertyFactory;

/**
 * The Class <code>SourcesPanel</code> is responsible for 
 * displaying source related preferences and allowing the 
 * preferences to be edited by the user.
 * 
 * Last Editor: $Author: $
 * Last Edited: $Date:  $
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision:  $
 */
@SuppressWarnings("serial")
public class SourcesPanel extends PCGenPrefsPanel
{
	private static String in_sources =
		PropertyFactory.getString("in_Prefs_sources");
	private JCheckBox campLoad = new JCheckBox();
	private JCheckBox charCampLoad = new JCheckBox();
	private JCheckBox allowOptsInSource = new JCheckBox();
	private JCheckBox saveCustom = new JCheckBox();
	private JCheckBox showOGL = new JCheckBox();
	private JCheckBox showMature = new JCheckBox();
	private JCheckBox showd20 = new JCheckBox();
	private JCheckBox showSponsors = new JCheckBox();
	private JComboBoxEx sourceOptions = new JComboBoxEx();
	private JCheckBox loadURL = new JCheckBox();
	private JCheckBox allowOverride = new JCheckBox();
	private JCheckBox useAdvancedSourceSelect = new JCheckBox();

	/**
	 * Instantiates a new monster panel.
	 */
	public SourcesPanel()
	{
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		JLabel label;
		Border etched = null;
		TitledBorder title1 =
				BorderFactory.createTitledBorder(etched, in_sources);

		title1.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(title1);
		this.setLayout(gridbag);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(2, 2, 2, 2);

		Utility.buildConstraints(c, 0, 0, 3, 1, 0, 0);
		label = new JLabel(PropertyFactory.getString("in_Prefs_autoLoadAtStart") + ": ");
		gridbag.setConstraints(label, c);
		this.add(label);
		Utility.buildConstraints(c, 3, 0, 1, 1, 0, 0);
		gridbag.setConstraints(campLoad, c);
		this.add(campLoad);

		Utility.buildConstraints(c, 0, 1, 3, 1, 0, 0);
		label = new JLabel(PropertyFactory.getString("in_Prefs_autoLoadWithPC") + ": ");
		gridbag.setConstraints(label, c);
		this.add(label);
		Utility.buildConstraints(c, 3, 1, 1, 1, 0, 0);
		gridbag.setConstraints(charCampLoad, c);
		this.add(charCampLoad);

		Utility.buildConstraints(c, 0, 2, 3, 1, 0, 0);
		label =
				new JLabel(PropertyFactory
					.getString("in_Prefs_allowOptionInSource")
					+ ": ");
		gridbag.setConstraints(label, c);
		this.add(label);
		Utility.buildConstraints(c, 3, 2, 1, 1, 0, 0);
		gridbag.setConstraints(allowOptsInSource, c);
		this.add(allowOptsInSource);

		Utility.buildConstraints(c, 0, 3, 3, 1, 0, 0);
		label = new JLabel(PropertyFactory.getString("in_Prefs_saveCustom") + ": ");
		gridbag.setConstraints(label, c);
		this.add(label);
		Utility.buildConstraints(c, 3, 3, 1, 1, 0, 0);
		gridbag.setConstraints(saveCustom, c);
		this.add(saveCustom);

		Utility.buildConstraints(c, 0, 4, 3, 1, 0, 0);
		label = new JLabel(PropertyFactory.getString("in_Prefs_displayOGL") + ": ");
		gridbag.setConstraints(label, c);
		this.add(label);
		Utility.buildConstraints(c, 3, 4, 1, 1, 0, 0);
		gridbag.setConstraints(showOGL, c);
		this.add(showOGL);

		Utility.buildConstraints(c, 0, 5, 3, 1, 0, 0);
		label = new JLabel(PropertyFactory.getString("in_Prefs_displayd20") + ": ");
		gridbag.setConstraints(label, c);
		this.add(label);
		Utility.buildConstraints(c, 3, 5, 1, 1, 0, 0);
		gridbag.setConstraints(showd20, c);
		this.add(showd20);

		Utility.buildConstraints(c, 0, 6, 3, 1, 0, 0);
		label = new JLabel(PropertyFactory.getString("in_Prefs_displaySponsors") + ": ");
		gridbag.setConstraints(label, c);
		this.add(label);
		Utility.buildConstraints(c, 3, 6, 1, 1, 0, 0);
		gridbag.setConstraints(showSponsors, c);
		this.add(showSponsors);

		Utility.buildConstraints(c, 0, 7, 3, 1, 0, 0);
		label = new JLabel(PropertyFactory.getString("in_Prefs_displayMature") + ": ");
		gridbag.setConstraints(label, c);
		this.add(label);
		Utility.buildConstraints(c, 3, 7, 1, 1, 0, 0);
		gridbag.setConstraints(showMature, c);
		this.add(showMature);

		Utility.buildConstraints(c, 0, 8, 3, 1, 0, 0);
		label = new JLabel(PropertyFactory.getString("in_Prefs_sourceDisplay") + ": ");
		gridbag.setConstraints(label, c);
		this.add(label);
		Utility.buildConstraints(c, 3, 8, 1, 1, 0, 0);
		sourceOptions =
				new JComboBoxEx(new String[]{PropertyFactory.getString("in_Prefs_sdLong"), PropertyFactory.getString("in_Prefs_sdMedium"),
					PropertyFactory.getString("in_Prefs_sdShort"), PropertyFactory.getString("in_Prefs_sdPage"), PropertyFactory.getString("in_Prefs_sdWeb")});
		gridbag.setConstraints(sourceOptions, c);
		this.add(sourceOptions);

		Utility.buildConstraints(c, 0, 9, 3, 1, 0, 0);
		label = new JLabel(PropertyFactory.getString("in_Prefs_loadURLs") + ": ");
		gridbag.setConstraints(label, c);
		this.add(label);
		Utility.buildConstraints(c, 3, 9, 1, 1, 0, 0);
		gridbag.setConstraints(loadURL, c);
		this.add(loadURL);
		loadURL.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				if (((JCheckBox) evt.getSource()).isSelected())
				{
					ShowMessageDelegate.showMessageDialog(PropertyFactory
						.getString("in_Prefs_urlBlocked"), Constants.s_APPNAME,
						MessageType.WARNING);
				}
			}
		});

		Utility.buildConstraints(c, 0, 10, 3, 1, 0, 0);
		label = new JLabel(PropertyFactory.getString("in_Prefs_allowOverride") + ": ");
		gridbag.setConstraints(label, c);
		this.add(label);
		Utility.buildConstraints(c, 3, 10, 1, 1, 0, 0);
		gridbag.setConstraints(allowOverride, c);
		this.add(allowOverride);

		Utility.buildConstraints(c, 0, 11, 3, 1, 0, 0);
		label = new JLabel(PropertyFactory.getString("in_Prefs_useAdvancedSourceSelect") + ": ");
		gridbag.setConstraints(label, c);
		this.add(label);
		Utility.buildConstraints(c, 3, 11, 1, 1, 0, 0);
		gridbag.setConstraints(useAdvancedSourceSelect, c);
		this.add(useAdvancedSourceSelect);

		Utility.buildConstraints(c, 5, 20, 1, 1, 1, 1);
		c.fill = GridBagConstraints.BOTH;
		label = new JLabel(" ");
		gridbag.setConstraints(label, c);
		this.add(label);
	}

	/* (non-Javadoc)
	 * @see pcgen.gui.prefs.PCGenPrefsPanel#getTitle()
	 */
	@Override
	public String getTitle()
	{
		return in_sources;
	}
	
	/* (non-Javadoc)
	 * @see pcgen.gui.prefs.PreferencesPanel#applyPreferences()
	 */
	@Override
	public void setOptionsBasedOnControls()
	{
		SettingsHandler.setLoadCampaignsAtStart(campLoad.isSelected());
		SettingsHandler.setLoadCampaignsWithPC(charCampLoad.isSelected());
		SettingsHandler.setOptionAllowedInSources(allowOptsInSource
			.isSelected());
		SettingsHandler.setSaveCustomEquipment(saveCustom.isSelected());
		SettingsHandler.setShowLicense(showOGL.isSelected());
		SettingsHandler.setShowMature(showMature.isSelected());
		SettingsHandler.setShowD20Info(showd20.isSelected());
		SettingsHandler.setShowSponsors(showSponsors.isSelected());
		SettingsHandler.setLoadURLs(loadURL.isSelected());
		SettingsHandler.setAllowOverride(allowOverride.isSelected());
		SettingsHandler.setUseAdvancedSourceSelect(useAdvancedSourceSelect.isSelected());

		switch (sourceOptions.getSelectedIndex())
		{
			case 0:
				Globals.setSourceDisplay(SourceFormat.LONG);
				break;

			case 1:
				Globals.setSourceDisplay(SourceFormat.MEDIUM);
				break;

			case 2:
				Globals.setSourceDisplay(SourceFormat.SHORT);
				break;

			case 3:
				Globals.setSourceDisplay(SourceFormat.PAGE);
				break;

			case 4:
				Globals.setSourceDisplay(SourceFormat.WEB);
				break;

			default:
				Logging
					.errorPrint("In PreferencesDialog.setOptionsBasedOnControls (sourceOptions) the index "
						+ sourceOptions.getSelectedIndex() + " is unsupported.");

				break;
		}
	}

	/* (non-Javadoc)
	 * @see pcgen.gui.prefs.PreferencesPanel#initPreferences()
	 */
	@Override
	public void applyOptionValuesToControls()
	{
		campLoad.setSelected(SettingsHandler.isLoadCampaignsAtStart());
		charCampLoad.setSelected(SettingsHandler.isLoadCampaignsWithPC());
		allowOptsInSource.setSelected(SettingsHandler
			.isOptionAllowedInSources());
		saveCustom.setSelected(SettingsHandler.getSaveCustomEquipment());
		showOGL.setSelected(SettingsHandler.showLicense());
		showMature.setSelected(SettingsHandler.showMature());
		showd20.setSelected(SettingsHandler.showD20Info());
		showSponsors.setSelected(SettingsHandler.showSponsors());
		loadURL.setSelected(SettingsHandler.isLoadURLs());
		allowOverride.setSelected(SettingsHandler.isAllowOverride());
		useAdvancedSourceSelect.setSelected(SettingsHandler.useAdvancedSourceSelect());
		
		switch (Globals.getSourceDisplay())
		{
			case LONG:
				sourceOptions.setSelectedIndex(0);

				break;

			case MEDIUM:
				sourceOptions.setSelectedIndex(1);

				break;

			case SHORT:
				sourceOptions.setSelectedIndex(2);

				break;

			case PAGE:
				sourceOptions.setSelectedIndex(3);

				break;

			case WEB:
				sourceOptions.setSelectedIndex(4);

				break;

			default:
				Logging
					.errorPrint("In PreferencesDialog.applyOptionValuesToControls (source display) the option "
						+ Globals.getSourceDisplay() + " is unsupported.");

				break;
		}
	}

}
