/*
 * SkillPointTableModel.java
 * Copyright 2010 Connor Petty <cpmeister@users.sourceforge.net>
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
 * Created on Jul 6, 2010, 1:30:26 PM
 */
package pcgen.gui2.tabs.skill;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import pcgen.core.facade.CharacterFacade;
import pcgen.core.facade.CharacterLevelFacade;
import pcgen.core.facade.CharacterLevelsFacade;
import pcgen.core.facade.CharacterLevelsFacade.CharacterLevelEvent;
import pcgen.core.facade.CharacterLevelsFacade.ClassListener;
import pcgen.core.facade.CharacterLevelsFacade.SkillPointListener;
import pcgen.core.facade.event.ListEvent;
import pcgen.core.facade.event.ListListener;
import pcgen.gui2.tabs.Utilities;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public class SkillPointTableModel extends AbstractTableModel
		implements ListListener, ClassListener, SkillPointListener
{

	private CharacterLevelsFacade levels;

	public SkillPointTableModel(CharacterFacade character)
	{
		this.levels = character.getCharacterLevelsFacade();
		levels.addListListener(this);
		levels.addClassListener(this);
		levels.addSkillPointListener(this);
	}

	public static void initializeTable(JTable table)
	{
		table.setAutoCreateColumnsFromModel(false);
		JTableHeader header = table.getTableHeader();
		TableColumnModel columns = new DefaultTableColumnModel();
		TableCellRenderer headerRenderer = header.getDefaultRenderer();
		columns.addColumn(Utilities.createTableColumn(0, "in_level", headerRenderer, false));
		columns.addColumn(Utilities.createTableColumn(1, "in_class", headerRenderer, true));
		TableColumn remainCol = Utilities.createTableColumn(2, "in_iskRemain", headerRenderer, false);
		remainCol.setCellRenderer(new BoldNumberRenderer());
		columns.addColumn(remainCol);
		columns.addColumn(Utilities.createTableColumn(3, "in_gained", headerRenderer, false));
		table.setColumnModel(columns);
		table.setFocusable(false);
		header.setReorderingAllowed(false);
		header.setResizingAllowed(false);
	}

	public int getRowCount()
	{
		return levels.getSize();
	}

	public int getColumnCount()
	{
		return 4;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		switch (columnIndex)
		{
			case 1:
				return Object.class;
			case 0:
			case 2:
			case 3:
				return Integer.class;
			default:
				return Object.class;
		}
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if (columnIndex == 0)
		{
			return rowIndex + 1;
		}
		CharacterLevelFacade level = levels.getElementAt(rowIndex);
		switch (columnIndex)
		{
			case 1:
				return levels.getClassTaken(level);
			case 2:
				return levels.getRemainingSkillPoints(level);
			case 3:
				return levels.getGainedSkillPoints(level);
			default:
				throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		if (columnIndex == 3)
		{
			return true;
		}
		return false;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		CharacterLevelFacade level = levels.getElementAt(rowIndex);
		levels.setGainedSkillPoints(level, (Integer) aValue);
	}

	public void elementAdded(ListEvent e)
	{
		fireTableRowsInserted(e.getIndex(), e.getIndex());
	}

	public void elementRemoved(ListEvent e)
	{
		fireTableRowsDeleted(e.getIndex(), e.getIndex());
	}

	public void elementsChanged(ListEvent e)
	{
		fireTableDataChanged();
	}

	public void skillPointsChanged(CharacterLevelEvent e)
	{
		levelChanged(e);
	}

	public void classChanged(CharacterLevelEvent e)
	{
		levelChanged(e);
	}

	private void levelChanged(CharacterLevelEvent e)
	{
		int firstRow = e.getBaseLevelIndex();
		int lastRow = e.affectsHigherLevels() ? levels.getSize() - 1 : firstRow;
		fireTableRowsUpdated(firstRow, lastRow);
	}

	/**
	 * The Class <code>BoldNumberRenderer</code> displays a right aligned
	 * read-only column containing a bolded number.
	 */
	private static class BoldNumberRenderer extends DefaultTableCellRenderer
	{

		/**
		 *  Create a new BoldNumberRenderer instance.
		 */
		public BoldNumberRenderer()
		{
			setHorizontalAlignment(RIGHT);
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
		 */
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			comp.setFont(table.getFont().deriveFont(Font.BOLD));
			return this;
		}

	}

}
