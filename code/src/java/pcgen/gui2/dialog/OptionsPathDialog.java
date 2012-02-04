/*
 * OptionsPathDialog.java
 * Copyright 2009 Connor Petty <cpmeister@users.sourceforge.net>
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
 * Created on Sep 3, 2009, 9:08:14 PM
 */
package pcgen.gui2.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import org.apache.commons.lang.SystemUtils;
import pcgen.system.ConfigurationSettings;
import pcgen.system.ConfigurationSettings.SettingsFilesPath;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public class OptionsPathDialog extends JDialog
{

	private final JTextField dirField;
	private final JButton dirButton;
	private String selectedDir;

	public OptionsPathDialog(JFrame frame)
	{
		super(frame, true);
		this.dirField = new JTextField();
		this.dirButton = new JButton();
		this.selectedDir = SettingsFilesPath.pcgen.name();
		initComponents();
	}

	public static String promptSettingsPath()
	{
		JFrame tempFrame = new JFrame("Select Settings Path");
		tempFrame.setLocationRelativeTo(null);
		OptionsPathDialog dialog = new OptionsPathDialog(tempFrame);
		
		tempFrame.setVisible(true);
		dialog.setVisible(true);
		tempFrame.setVisible(false);
		tempFrame.dispose();
		return dialog.getSelectedDirectory();
	}

	private void initComponents()
	{
		setResizable(false);
		setTitle("Directory for options.ini location");
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		getContentPane().setLayout(new java.awt.GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		JLabel label = new JLabel("Select a directory to store PCGen options in:");
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(4, 4, 0, 4);
		getContentPane().add(label, gridBagConstraints);

		gridBagConstraints.insets = new Insets(2, 0, 2, 0);
		getContentPane().add(new JSeparator(), gridBagConstraints);

		label = new JLabel("If you have an existing options.ini file," +
				"then select the directory containing that file");
		gridBagConstraints.insets = new Insets(4, 4, 4, 4);
		getContentPane().add(label, gridBagConstraints);

		ActionHandler handler = new ActionHandler();
		ButtonGroup group = new ButtonGroup();

		gridBagConstraints.insets = new Insets(0, 4, 0, 4);
		addRadioButton(
				"<html><b>PCGen Dir</b>: This is the directory that PCGen is installed into (default)",
				SettingsFilesPath.pcgen.name(), group, handler, gridBagConstraints);
		if (SystemUtils.IS_OS_MAC_OSX)
		{
			addRadioButton("<html><b>Mac User Dir</b>",
						   SettingsFilesPath.mac_user.name(), group, handler, gridBagConstraints);
		}
		addRadioButton("<html><b>Home Dir</b>: This is your home directory",
					   SettingsFilesPath.user.name(), group, handler, gridBagConstraints);
		addRadioButton("Select a directory to use",
					   "select", group, handler, gridBagConstraints);


		dirField.setText(ConfigurationSettings.getSettingsDirFromFilePath(selectedDir));
		dirField.setEditable(false);

		gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.insets = new Insets(0, 4, 0, 0);
		getContentPane().add(dirField, gridBagConstraints);


		dirButton.setText("...");
		dirButton.setEnabled(false);
		dirButton.addActionListener(handler);
		dirButton.setActionCommand("custom");
		dirButton.setMargin(new Insets(2, 2, 2, 2));

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new Insets(0, 0, 0, 4);
		getContentPane().add(dirButton, gridBagConstraints);

		JButton okButton = new JButton("OK");
		okButton.setPreferredSize(new Dimension(75, 23));
		okButton.setActionCommand("ok");
		okButton.addActionListener(handler);

		gridBagConstraints.insets = new Insets(4, 0, 4, 0);
		getContentPane().add(okButton, gridBagConstraints);

		pack();
		setLocationRelativeTo(null);
	}

	public String getSelectedDirectory()
	{
		return selectedDir;
	}

	private void addRadioButton(String text, String command, ButtonGroup group,
								ActionListener listener, GridBagConstraints gbc)
	{
		JRadioButton rButton = new JRadioButton(text);
		rButton.setActionCommand(command);
		rButton.setSelected(command.equals(selectedDir));
		rButton.addActionListener(listener);
		group.add(rButton);
		getContentPane().add(rButton, gbc);
	}

	private class ActionHandler implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			String command = e.getActionCommand();
			if (command.equals("custom"))
			{
				JFileChooser fc = new JFileChooser(dirField.getText());

				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				final int rVal = fc.showOpenDialog(null);

				if (rVal == JFileChooser.APPROVE_OPTION)
				{
					selectedDir = String.valueOf(fc.getSelectedFile());
					dirField.setText(selectedDir);
				}
			}
			else if (command.equals("select"))
			{
				dirButton.setEnabled(true);
			}
			else if (command.equals("ok"))
			{
				OptionsPathDialog.this.dispose();
			}
			else
			{
				dirButton.setEnabled(false);
				selectedDir = command;
				dirField.setText(ConfigurationSettings.getSettingsDirFromFilePath(selectedDir));
			}
		}

	}

}
