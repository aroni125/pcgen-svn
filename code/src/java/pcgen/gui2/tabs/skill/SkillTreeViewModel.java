/*
 * SkillTreeViewModel.java
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
 * Created on Jul 6, 2010, 3:53:51 PM
 */
package pcgen.gui2.tabs.skill;

import java.util.Arrays;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import pcgen.cdom.enumeration.SkillCost;
import pcgen.core.facade.CharacterFacade;
import pcgen.core.facade.CharacterLevelFacade;
import pcgen.core.facade.CharacterLevelsFacade;
import pcgen.core.facade.SkillFacade;
import pcgen.core.facade.util.DefaultListFacade;
import pcgen.core.facade.util.ListFacade;
import pcgen.gui2.filter.FilteredTreeViewTable;
import pcgen.gui2.util.treeview.DataView;
import pcgen.gui2.util.treeview.DataViewColumn;
import pcgen.gui2.util.treeview.DefaultDataViewColumn;
import pcgen.gui2.util.treeview.TreeView;
import pcgen.gui2.util.treeview.TreeViewModel;
import pcgen.gui2.util.treeview.TreeViewPath;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public class SkillTreeViewModel implements TreeViewModel<SkillFacade>,
		DataView<SkillFacade>, ListSelectionListener
{

	private static final List<? extends DataViewColumn> columns = Arrays.asList(
			new DefaultDataViewColumn("Total", Integer.class, true),
			new DefaultDataViewColumn("Modifier", Integer.class, true),
			new DefaultDataViewColumn("Ranks", Float.class, true, true),
			new DefaultDataViewColumn("Skill Cost", SkillCost.class, true),
			new DefaultDataViewColumn("Source", String.class));
	private final DefaultListFacade<TreeView<SkillFacade>> treeviews;
	private final CharacterFacade character;
	private final CharacterLevelsFacade levels;
	private final ListSelectionModel selectionModel;
	private FilteredTreeViewTable table;
	private boolean displayCostTrees = false;

	public SkillTreeViewModel(CharacterFacade character, ListSelectionModel selectionModel)
	{
		this.character = character;
		this.levels = character.getCharacterLevelsFacade();
		this.selectionModel = selectionModel;

		List<? extends TreeView<SkillFacade>> views = Arrays.asList(SkillTreeView.NAME,
																	SkillTreeView.TYPE_NAME,
																	SkillTreeView.KEYSTAT_NAME,
																	SkillTreeView.KEYSTAT_TYPE_NAME);
		treeviews = new DefaultListFacade<TreeView<SkillFacade>>(views);
		selectionModel.addListSelectionListener(this);
	}

	public void install(FilteredTreeViewTable table)
	{
		this.table = table;
		table.setTreeViewModel(this);
	}

	public void uninstall()
	{
		table = null;
	}

	public ListFacade<? extends TreeView<SkillFacade>> getTreeViews()
	{
		return treeviews;
	}

	public int getDefaultTreeViewIndex()
	{
		return 0;
	}

	public DataView<SkillFacade> getDataView()
	{
		return this;
	}

	public ListFacade<SkillFacade> getDataModel()
	{
		return character.getDataSet().getSkills();
	}

	public List<?> getData(SkillFacade obj)
	{
		if (selectionModel.isSelectionEmpty())
		{
			return Arrays.asList(0, 0, 0.0, null, obj.getSource());
		}
		else
		{
			int index = selectionModel.getMinSelectionIndex();
			CharacterLevelFacade level = levels.getElementAt(index);
			return Arrays.asList(
					levels.getSkillTotal(level, obj),
					levels.getSkillModifier(level, obj),
					levels.getSkillRanks(level, obj),
					levels.getSkillCost(level, obj),
					obj.getSource());
		}
	}

	public List<? extends DataViewColumn> getDataColumns()
	{
		return columns;
	}

	public void valueChanged(ListSelectionEvent e)
	{
		if (!e.getValueIsAdjusting() && table != null)
		{
			if (selectionModel.getMinSelectionIndex() != -1 && !displayCostTrees)
			{
				treeviews.addElement(COST_NAME);
				treeviews.addElement(COST_TYPE_NAME);
			}
			else if (selectionModel.getMinSelectionIndex() == -1 && displayCostTrees)
			{
				treeviews.removeElement(COST_NAME);
				treeviews.removeElement(COST_TYPE_NAME);
			}
			table.refreshModelData();
		}
	}

	/**
	 * Create a TreeViewPath for the skill and paths but trimming off the final 
	 * path if it is empty. 
	 * @param pobj The skill
	 * @param path The paths under which the skills should be shown.
	 * @return The TreeViewPath.
	 */
	protected static TreeViewPath<SkillFacade> createTreeViewPath(SkillFacade pobj,
		Object... path)
	{
		Object displayPath[];
		if (StringUtils.isEmpty(String.valueOf(path[path.length - 1])))
		{
			displayPath = new Object[path.length - 1];
			for (int i = 0; i < displayPath.length; i++)
			{
				displayPath[i] = path[i];
			}
		}
		else
		{
			displayPath = path;
		}
		if (displayPath.length == 0)
		{
			return new TreeViewPath<SkillFacade>(pobj);
		}
		return new TreeViewPath<SkillFacade>(pobj, displayPath);
	}

	private enum SkillTreeView implements TreeView<SkillFacade>
	{

		NAME("Name"),
		TYPE_NAME("Type/Name"),
		KEYSTAT_NAME("Key Stat/Name"),
		KEYSTAT_TYPE_NAME("Key Stat/Type/Name");
		private String name;

		private SkillTreeView(String name)
		{
			this.name = name;
		}

		public String getViewName()
		{
			return name;
		}

		@SuppressWarnings("unchecked")
		public List<TreeViewPath<SkillFacade>> getPaths(SkillFacade pobj)
		{
			TreeViewPath<SkillFacade> path;
			switch (this)
			{
				case NAME:
					path = new TreeViewPath<SkillFacade>(pobj);
					break;
				case TYPE_NAME:
					path = createTreeViewPath(pobj, pobj.getDisplayType());
					break;
				case KEYSTAT_NAME:
					path = new TreeViewPath<SkillFacade>(pobj,
														 pobj.getKeyStat());
					break;
				case KEYSTAT_TYPE_NAME:
					path =
							createTreeViewPath(pobj, pobj.getKeyStat(),
								pobj.getDisplayType());
					break;
				default:
					throw new InternalError();
			}
			return Arrays.asList(path);
		}

	}

	private final TreeView<SkillFacade> COST_NAME = new TreeView<SkillFacade>()
	{

		public String getViewName()
		{
			return "Cost/Name";
		}

		@SuppressWarnings("unchecked")
		public List<TreeViewPath<SkillFacade>> getPaths(SkillFacade pobj)
		{
			CharacterLevelFacade level = levels.getElementAt(selectionModel.getMinSelectionIndex());
			return Arrays.asList(new TreeViewPath<SkillFacade>(pobj,
															   levels.getSkillCost(level, pobj)));
		}

	};
	private final TreeView<SkillFacade> COST_TYPE_NAME = new TreeView<SkillFacade>()
	{

		public String getViewName()
		{
			return "Cost/Type/Name";
		}

		@SuppressWarnings("unchecked")
		public List<TreeViewPath<SkillFacade>> getPaths(SkillFacade pobj)
		{
			CharacterLevelFacade level = levels.getElementAt(selectionModel.getMinSelectionIndex());
			return Arrays.asList(createTreeViewPath(pobj,
													levels.getSkillCost(level, pobj),
													pobj.getDisplayType()));
//
//			return Arrays.asList(
//					new TreeViewPath<SkillFacade>(pobj,
//												  null,
//												  pobj.getType()));
		}

	};
}
