/*
 * Class.java
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
 * Created on Nov 14, 2011, 5:25:57 PM
 */
package pcgen.gui2.tabs.spells;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import pcgen.core.facade.CharacterFacade;
import pcgen.core.facade.ClassFacade;
import pcgen.gui2.tools.InfoPane;
import pcgen.gui2.util.JTreeViewTable;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
class ClassInfoHandler implements ListSelectionListener
{

	private CharacterFacade character;
	private final JTreeViewTable availableTable;
	private final JTreeViewTable selectedTable;
	private final InfoPane classPane;

	public ClassInfoHandler(CharacterFacade character, JTreeViewTable table1, JTreeViewTable table2,
							InfoPane classPane)
	{
		this.character = character;
		this.classPane = classPane;
		this.availableTable = table1;
		this.selectedTable = table2;
	}

	public void install()
	{
		availableTable.getSelectionModel().addListSelectionListener(this);
		selectedTable.getSelectionModel().addListSelectionListener(this);
	}

	public void uninstall()
	{
		availableTable.getSelectionModel().removeListSelectionListener(this);
		selectedTable.getSelectionModel().removeListSelectionListener(this);
	}

	public void valueChanged(ListSelectionEvent e)
	{
		if (!e.getValueIsAdjusting())
		{
			TreePath path;
			if (e.getSource() == availableTable.getSelectionModel())
			{
				path = availableTable.getTree().getSelectionPath();
			}
			else
			{
				path = selectedTable.getTree().getSelectionPath();
			}
			if (path == null)
			{
				return;
			}
			ClassFacade c = null;
			DefaultMutableTreeNode treenode = (DefaultMutableTreeNode) path.getLastPathComponent();
			Object[] objs = treenode.getUserObjectPath();
			for (Object object : objs)
			{
				if (object instanceof ClassFacade)
				{
					c = (ClassFacade) object;
					break;
				}
			}
			if (c != null)
			{
				String text = character.getSpellSupport().getClassInfo(c);
				classPane.setText(text);
			}
		}
	}

}
