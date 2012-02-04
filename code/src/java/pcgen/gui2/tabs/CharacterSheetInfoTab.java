/**
 * CharacterSheetInfoTab.java
 * Copyright James Dempsey, 2010
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
 * Created on 29/09/2010 7:16:42 PM
 *
 * $Id: CharacterSheetInfoTab.java 14593 2011-02-23 06:16:07Z cpmeister $
 */
package pcgen.gui2.tabs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileFilter;
import java.util.Hashtable;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListDataEvent;
import pcgen.core.facade.CharacterFacade;
import pcgen.core.facade.EquipmentSetFacade;
import pcgen.core.facade.GameModeFacade;
import pcgen.core.facade.TempBonusFacade;
import pcgen.core.facade.event.ListEvent;
import pcgen.core.facade.event.ListListener;
import pcgen.core.facade.event.ReferenceEvent;
import pcgen.core.facade.event.ReferenceListener;
import pcgen.core.facade.util.ListFacades;
import pcgen.gui2.csheet.CharacterSheetPanel;
import pcgen.gui2.filter.Filter;
import pcgen.gui2.filter.FilteredListFacadeTableModel;
import pcgen.gui2.tools.FlippingSplitPane;
import pcgen.gui2.util.event.ListDataAdapter;
import pcgen.gui2.util.table.TableUtils;
import pcgen.system.ConfigurationSettings;
import pcgen.system.LanguageBundle;
import pcgen.system.Main;
import pcgen.util.Logging;

/**
 * The Class <code>CharacterSheetInfoTab</code> is a placeholder for the character 
 * sheet tab.
 *
 * <br/>
 * Last Editor: $Author: cpmeister $
 * Last Edited: $Date: 2011-02-22 22:16:07 -0800 (Tue, 22 Feb 2011) $
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision: 14593 $
 */
public class CharacterSheetInfoTab extends FlippingSplitPane implements CharacterInfoTab
{

	private final TabTitle tabTitle = new TabTitle("Character Sheet");
	private final CharacterSheetPanel csheet;
	private final JComboBox sheetBox;
	private final JTable equipSetTable;
	private final JTable tempBonusTable;
	private final JTable tempBonusRowTable;
	private final JTable equipSetRowTable;

	public CharacterSheetInfoTab()
	{
		this.csheet = new CharacterSheetPanel();
		this.sheetBox = new JComboBox();
		this.equipSetTable = TableUtils.createDefaultTable();
		this.equipSetRowTable = TableUtils.createDefaultTable();
		this.tempBonusTable = TableUtils.createDefaultTable();
		this.tempBonusRowTable = TableUtils.createDefaultTable();
		setOneTouchExpandable(true);

		setLeftComponent(csheet);

		JPanel panel = new JPanel(new BorderLayout());
		Box box = Box.createHorizontalBox();
		box.add(new JLabel(LanguageBundle.getString("in_character_sheet")));
		sheetBox.setRenderer(new DefaultListCellRenderer()
		{

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
			{
				if (value instanceof File)
				{
					value = ((File) value).getName();
				}
				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}

		});
		box.add(sheetBox);
		panel.add(box, BorderLayout.NORTH);
		FlippingSplitPane subPane = new FlippingSplitPane();
		subPane.setOrientation(VERTICAL_SPLIT);
		equipSetTable.getTableHeader().setReorderingAllowed(false);
		JScrollPane pane = TableUtils.createRadioBoxSelectionPane(equipSetTable, equipSetRowTable);
		pane.setPreferredSize(new Dimension(200, 100));
		subPane.setTopComponent(pane);

		tempBonusTable.getTableHeader().setReorderingAllowed(false);
		pane = TableUtils.createCheckBoxSelectionPane(tempBonusTable, tempBonusRowTable);
		pane.setPreferredSize(new Dimension(200, 100));
		subPane.setBottomComponent(pane);
		panel.add(subPane, BorderLayout.CENTER);
		setRightComponent(panel);
	}

	public Hashtable<Object, Object> createModels(CharacterFacade character)
	{
		Hashtable<Object, Object> state = new Hashtable<Object, Object>();
		state.put(BoxHandler.class, new BoxHandler(character));
		state.put(CSheetHandler.class, new CSheetHandler(character));
		state.put(TempBonusTableModel.class, new TempBonusTableModel(character));
		state.put(EquipSetTableModel.class, new EquipSetTableModel(character));
		return state;
	}

	public void restoreModels(Hashtable<?, ?> state)
	{
		((CSheetHandler) state.get(CSheetHandler.class)).install();
		((BoxHandler) state.get(BoxHandler.class)).install();
		equipSetTable.setModel((EquipSetTableModel) state.get(EquipSetTableModel.class));
		equipSetRowTable.setModel((EquipSetTableModel) state.get(EquipSetTableModel.class));
		tempBonusTable.setModel((TempBonusTableModel) state.get(TempBonusTableModel.class));
		tempBonusRowTable.setModel((TempBonusTableModel) state.get(TempBonusTableModel.class));
	}

	public void storeModels(Hashtable<Object, Object> state)
	{
		((CSheetHandler) state.get(CSheetHandler.class)).uninstall();
	}

	public TabTitle getTabTitle()
	{
		return tabTitle;
	}

	private class BoxHandler extends ListDataAdapter
	{

		private CharacterFacade character;
		private ComboBoxModel model;

		public BoxHandler(CharacterFacade character)
		{
			this.character = character;
			GameModeFacade game = character.getDataSet().getGameMode();
			String previewDir = ConfigurationSettings.getPreviewDir();
			File sheetDir = new File(previewDir, game.getCharSheetDir());
			if (sheetDir.exists() && sheetDir.isDirectory())
			{
				File[] files = sheetDir.listFiles(new FileFilter()
				{

					public boolean accept(File pathname)
					{
						return pathname.isFile() && !pathname.isHidden();
					}

				});
				model = new DefaultComboBoxModel(files);

				File file = null;
				String csheet = Main.getStartupCharacterSheet();
				if (csheet != null)
				{
					file = new File(sheetDir, csheet);
					if(!file.isFile())
					{
						Logging.errorPrint("Invalid Character Sheet: "+file.getAbsolutePath());
					}
				}
				if(file == null || !file.isFile())
				{
					file = new File(sheetDir, game.getDefaultCharSheet());
				}
				model.setSelectedItem(file);
			}
			else
			{
				model = new DefaultComboBoxModel();
			}
			model.addListDataListener(this);
		}

		public void install()
		{
			sheetBox.setModel(model);
			csheet.setCharacterSheet((File) model.getSelectedItem());
		}

		@Override
		public void listDataChanged(ListDataEvent e)
		{
			if (e.getIndex0() == -1 && e.getIndex1() == -1)
			{
				csheet.setCharacterSheet((File) sheetBox.getSelectedItem());
			}
		}

	}

	private class CSheetHandler implements ListListener<Object>, ReferenceListener<Object>
	{

		private CharacterFacade character;
		private String sheetDir;

		public CSheetHandler(CharacterFacade character)
		{
			this.character = character;
		}

		public void install()
		{
			csheet.setCharacter(character);

			character.getAgeCategoryRef().addReferenceListener(this);
			character.getAgeRef().addReferenceListener(this);
			character.getAlignmentRef().addReferenceListener(this);
			character.getCarriedWeightRef().addReferenceListener(this);
			character.getDeityRef().addReferenceListener(this);
			character.getGenderRef().addReferenceListener(this);
			character.getHandedRef().addReferenceListener(this);
			character.getLoadRef().addReferenceListener(this);
			character.getNameRef().addReferenceListener(this);
			character.getPlayersNameRef().addReferenceListener(this);
			character.getRaceRef().addReferenceListener(this);
			character.getStatTotalTextRef().addReferenceListener(this);
			character.getTotalHPRef().addReferenceListener(this);
			character.getXPRef().addReferenceListener(this);

			character.getDomains().addListListener(this);
			character.getLanguages().addListListener(this);
			character.getTempBonuses().addListListener(this);
			character.getTemplates().addListListener(this);
		}

		public void uninstall()
		{
			character.getAgeCategoryRef().removeReferenceListener(this);
			character.getAgeRef().removeReferenceListener(this);
			character.getAlignmentRef().removeReferenceListener(this);
			character.getCarriedWeightRef().removeReferenceListener(this);
			character.getDeityRef().removeReferenceListener(this);
			character.getGenderRef().removeReferenceListener(this);
			character.getHandedRef().removeReferenceListener(this);
			character.getLoadRef().removeReferenceListener(this);
			character.getNameRef().removeReferenceListener(this);
			character.getPlayersNameRef().removeReferenceListener(this);
			character.getRaceRef().removeReferenceListener(this);
			character.getStatTotalTextRef().removeReferenceListener(this);
			character.getTotalHPRef().removeReferenceListener(this);
			character.getXPRef().removeReferenceListener(this);

			character.getDomains().removeListListener(this);
			character.getLanguages().removeListListener(this);
			character.getTempBonuses().removeListListener(this);
			character.getTemplates().removeListListener(this);
		}

		public void elementAdded(ListEvent<Object> e)
		{
			csheet.refresh();
		}

		public void elementRemoved(ListEvent<Object> e)
		{
			csheet.refresh();
		}

		public void elementsChanged(ListEvent<Object> e)
		{
			csheet.refresh();
		}

		public void referenceChanged(ReferenceEvent<Object> e)
		{
			csheet.refresh();
		}

	}

	private class TempBonusTableModel extends FilteredListFacadeTableModel<TempBonusFacade>
			implements Filter<CharacterFacade, TempBonusFacade>
	{

		private ListListener<TempBonusFacade> listener = new ListListener<TempBonusFacade>()
		{

			public void elementAdded(ListEvent<TempBonusFacade> e)
			{
				int index = ListFacades.wrap(sortedList).indexOf(e.getElement());
				TempBonusTableModel.this.fireTableCellUpdated(index, -1);
			}

			public void elementRemoved(ListEvent<TempBonusFacade> e)
			{
				int index = ListFacades.wrap(sortedList).indexOf(e.getElement());
				TempBonusTableModel.this.fireTableCellUpdated(index, -1);
			}

			public void elementsChanged(ListEvent<TempBonusFacade> e)
			{
				TempBonusTableModel.this.fireTableRowsUpdated(0, sortedList.getSize() - 1);
			}

		};

		public TempBonusTableModel(CharacterFacade character)
		{
			super(character);
			setDelegate(character.getAvailableTempBonuses());
			setFilter(this);
			character.getTempBonuses().addListListener(listener);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex)
		{
			if (columnIndex == -1)
			{
				return Boolean.class;
			}
			return super.getColumnClass(columnIndex);
		}

		@Override
		protected Object getValueAt(TempBonusFacade element, int column)
		{
			switch (column)
			{
				case -1:
					return character.getTempBonuses().containsElement(element);
				case 0:
					return element;
				default:
					return null;
			}
		}

		public int getColumnCount()
		{
			return 1;
		}

		@Override
		public String getColumnName(int column)
		{
			if (column == 0)
			{
				return "Temporary Bonuses";
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
			if (columnIndex >= 0)
			{
				return false;
			}
			return true;
		}

		public boolean accept(CharacterFacade context, TempBonusFacade element)
		{
			return true;
		}

	}

	private class EquipSetTableModel extends FilteredListFacadeTableModel<EquipmentSetFacade>
			implements ReferenceListener<EquipmentSetFacade>, Filter<CharacterFacade, EquipmentSetFacade>
	{

		public EquipSetTableModel(CharacterFacade character)
		{
			super(character);
			character.getEquipmentSetRef().addReferenceListener(this);
			setDelegate(character.getEquipmentSets());
			setFilter(this);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex)
		{
			if (columnIndex == -1)
			{
				return Boolean.class;
			}
			return super.getColumnClass(columnIndex);
		}

		@Override
		protected Object getValueAt(EquipmentSetFacade element, int column)
		{
			switch (column)
			{
				case -1:
					return character.getEquipmentSetRef().getReference() == element;
				case 0:
					return element;
				default:
					return null;
			}
		}

		public int getColumnCount()
		{
			return 1;
		}

		@Override
		public String getColumnName(int column)
		{
			if (column == 0)
			{
				return "Equipment Sets";
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
			if (columnIndex >= 0)
			{
				return false;
			}
			return true;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex)
		{
			EquipmentSetFacade eqset = sortedList.getElementAt(rowIndex);
			character.setEquipmentSet(eqset);
		}

		public void referenceChanged(ReferenceEvent<EquipmentSetFacade> e)
		{
			fireTableRowsUpdated(0, character.getEquipmentSets().getSize() - 1);
		}

		public boolean accept(CharacterFacade context, EquipmentSetFacade element)
		{
			return true;
		}

	}

}
