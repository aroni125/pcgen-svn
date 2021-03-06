/*
 * AdvancedSourceSelectionPanel.java
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
 * Created on Feb 21, 2009, 7:15:03 PM
 */
package pcgen.gui2.sources;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import pcgen.core.facade.CampaignFacade;
import pcgen.core.facade.CharacterFacade;
import pcgen.core.facade.GameModeFacade;
import pcgen.core.facade.SourceSelectionFacade;
import pcgen.core.facade.event.ListEvent;
import pcgen.core.facade.event.ListListener;
import pcgen.core.facade.util.DefaultListFacade;
import pcgen.core.facade.util.ListFacade;
import pcgen.core.facade.util.ListFacades;
import pcgen.gui2.UIPropertyContext;
import pcgen.gui2.filter.Filter;
import pcgen.gui2.filter.FilterBar;
import pcgen.gui2.filter.FilterButton;
import pcgen.gui2.filter.FilterUtilities;
import pcgen.gui2.filter.FilteredTreeViewTable;
import pcgen.gui2.tools.InfoPane;
import pcgen.gui2.util.SortedListModel;
import pcgen.gui2.util.table.TableUtils;
import pcgen.gui2.util.treeview.DataView;
import pcgen.gui2.util.treeview.DataViewColumn;
import pcgen.gui2.util.treeview.TreeView;
import pcgen.gui2.util.treeview.TreeViewModel;
import pcgen.gui2.util.treeview.TreeViewPath;
import pcgen.gui2.util.treeview.TreeViewTableModel;
import pcgen.system.FacadeFactory;
import pcgen.util.Comparators;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
class AdvancedSourceSelectionPanel extends JPanel
		implements ListSelectionListener, ListListener<CampaignFacade>
{
	
	private FilteredTreeViewTable selectionTable;
	private SourceTreeViewModel treeViewModel;
	private InfoPane infoPane;
	private GameModeFacade gameMode;
	private JList gameModeList;
	private DefaultListFacade<CampaignFacade> selectedCampaigns;
	
	public AdvancedSourceSelectionPanel()
	{
		this.selectionTable = new FilteredTreeViewTable()
		{
			
			@Override
			protected TreeViewTableModel createDefaultTreeViewTableModel(DataView dataView)
			{
				return new SourceTreeViewTableModel(dataView);
			}
			
		};
		this.treeViewModel = new SourceTreeViewModel();
		this.gameModeList = new JList();
		this.infoPane = new InfoPane("Campaign Info");
		this.selectedCampaigns = new DefaultListFacade<CampaignFacade>();
		initComponents();
		initDefaults();
		selectedCampaigns.addListListener(this);
	}
	
	private void initComponents()
	{
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder(null, "GameModes",
														 TitledBorder.CENTER,
														 TitledBorder.DEFAULT_POSITION));
		//panel.add(new JLabel("GameModes"), BorderLayout.NORTH);

		ListModel gameModes = new SortedListModel<GameModeFacade>(FacadeFactory.getGameModes(),
																  Comparators.toStringIgnoreCaseCollator());
		gameModeList.setModel(gameModes);
		gameModeList.setCellRenderer(new DefaultListCellRenderer()
		{
			
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
			{
				value = ((GameModeFacade) value).getDisplayName();
				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}
			
		});
		gameModeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		gameModeList.addListSelectionListener(this);
		panel.add(new JScrollPane(gameModeList), BorderLayout.CENTER);
		
		add(panel, BorderLayout.WEST);
		
		FilterBar bar = FilterUtilities.createDefaultFilterBar();
		FilterButton gainedFilterButton = new FilterButton();
		gainedFilterButton.setText("Selected");
		gainedFilterButton.setEnabled(true);
		gainedFilterButton.setFilter(new Filter<CharacterFacade, CampaignFacade>()
		{

			public boolean accept(CharacterFacade context, CampaignFacade element)
			{
				CampaignFacade camp = (CampaignFacade) element;
				return selectedCampaigns.containsElement(camp);		
			}
		});
		bar.addDisplayableFilter(gainedFilterButton);
		panel = new JPanel(new BorderLayout());
		panel.add(bar, BorderLayout.NORTH);
		
		selectionTable.setDisplayableFilter(bar);
		selectionTable.setTreeViewModel(treeViewModel);
		selectionTable.getSelectionModel().addListSelectionListener(this);
		selectionTable.setTreeCellRenderer(new CampaignRenderer());
		
		JScrollPane pane = TableUtils.createCheckBoxSelectionPane(selectionTable, TableUtils.createDefaultTable());
		pane.setPreferredSize(new Dimension(400, 400));
		panel.add(pane, BorderLayout.CENTER);
		
		add(panel, BorderLayout.CENTER);

		//infoPane.setMinimumSize(new Dimension(250, 100));
		infoPane.setPreferredSize(new Dimension(300, 200));
		add(infoPane, BorderLayout.EAST);
	}
	
	private void initDefaults()
	{
		if (gameModeList.getModel().getSize() > 0)
		{
			gameModeList.setSelectedIndex(0);
		}
	}
	
	public GameModeFacade getSelectedGameMode()
	{
		return gameMode;
	}
	
	public List<CampaignFacade> getSelectedCampaigns()
	{
		return selectedCampaigns.getContents();
	}
	
	public void setSourceSelection(SourceSelectionFacade sources)
	{
		gameModeList.setSelectedValue(sources.getGameMode().getReference(), true);
		selectedCampaigns.setContents(ListFacades.wrap(sources.getCampaigns()));
		//selectionPanel.setSelectedObjects(ListFacades.wrap(sources.getCampaigns()));
	}
	
	private void setSelectedGameMode(GameModeFacade elementAt)
	{
		this.gameMode = elementAt;
		selectedCampaigns.clearContents();
		treeViewModel.setGameModel(elementAt);
	}
	
	private void setSelectedCampaign(CampaignFacade source)
	{
		infoPane.setText(FacadeFactory.getCampaignInfoFactory().getHTMLInfo(
			source, selectedCampaigns.getContents()));
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		if (!e.getValueIsAdjusting())
		{
			if (e.getSource() == selectionTable.getSelectionModel())
			{
				final Object data = selectionTable.getSelectedObject();
				if (data != null && data instanceof CampaignFacade)
				{
					setSelectedCampaign((CampaignFacade) data);
				}
			}
			else
			{
				setSelectedGameMode((GameModeFacade) gameModeList.getSelectedValue());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void elementAdded(ListEvent<CampaignFacade> e)
	{
		// Refresh displayed rows now that the selection has changed
		selectionTable.updateDisplay();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void elementRemoved(ListEvent<CampaignFacade> e)
	{
		// Refresh displayed rows now that the selection has changed
		selectionTable.updateDisplay();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void elementsChanged(ListEvent<CampaignFacade> e)
	{
		// Refresh displayed rows now that the selection has changed
		selectionTable.updateDisplay();
	}
	
	/**
	 * The Class <code>SourceTreeViewTableModel</code> is the model backing the 
	 * table showing the sources on the advanced source selection tab.
	 */
	
	private final class SourceTreeViewTableModel extends TreeViewTableModel
	{
		/**
		 * Create a new instance of SourceTreeViewTableModel
		 * @param dataView
		 */
		private SourceTreeViewTableModel(DataView dataView)
		{
			super(dataView);
		}

		@Override
		public Class getColumnClass(int column)
		{
			if (column == -1)
			{
				return Boolean.class;
			}
			return super.getColumnClass(column);
		}

		@Override
		public Object getValueAt(Object node, int column)
		{
			if (column != -1)
			{
				return super.getValueAt(node, column);
			}
			Object userObject = ((DefaultMutableTreeNode) node).getUserObject();
			if (userObject instanceof CampaignFacade)
			{
				CampaignFacade camp = (CampaignFacade) userObject;
				return selectedCampaigns.containsElement(camp);
			}
			return null;
		}

		@Override
		public boolean isCellEditable(Object node, int column)
		{
			if (column == -1)
			{
				return ((DefaultMutableTreeNode) node).getUserObject() instanceof CampaignFacade;
			}
			return super.isCellEditable(node, column);
		}

		@Override
		public void setValueAt(Object aValue, Object node, int column)
		{
			CampaignFacade camp = (CampaignFacade) ((DefaultMutableTreeNode) node).getUserObject();
			Boolean value = (Boolean) aValue;
			if (value)
			{
				selectedCampaigns.addElement(camp);
				if (!FacadeFactory.passesPrereqs(gameMode, selectedCampaigns.getContents()))
				{
					JOptionPane.showMessageDialog(AdvancedSourceSelectionPanel.this,
												  "Prereqs for this campaign have not fulfilled",
												  "Cannot Select Campaign",
												  JOptionPane.INFORMATION_MESSAGE);
					selectedCampaigns.removeElement(camp);
				}
			}
			else
			{
				selectedCampaigns.removeElement(camp);
			}
		}
	}

	private static class SourceTreeViewModel
			implements TreeViewModel<CampaignFacade>, DataView<CampaignFacade>, ListListener<CampaignFacade>
	{
		
		private static ListFacade<TreeView<CampaignFacade>> views =
				new DefaultListFacade<TreeView<CampaignFacade>>(Arrays.asList(SourceTreeView.values()));
		private DefaultListFacade<CampaignFacade> model;
		private ListFacade<CampaignFacade> baseModel = null;
		
		public SourceTreeViewModel()
		{
			this.model = new DefaultListFacade<CampaignFacade>();
		}
		
		public ListFacade<? extends TreeView<CampaignFacade>> getTreeViews()
		{
			return views;
		}
		
		public int getDefaultTreeViewIndex()
		{
			return 2;
		}
		
		public DataView<CampaignFacade> getDataView()
		{
			return this;
		}
		
		public ListFacade<CampaignFacade> getDataModel()
		{
			return model;
		}
		
		public List<?> getData(CampaignFacade obj)
		{
			return Collections.emptyList();
		}
		
		public List<? extends DataViewColumn> getDataColumns()
		{
			return Collections.emptyList();
		}
		
		public void setGameModel(GameModeFacade gameMode)
		{
			if (baseModel != null)
			{
				baseModel.removeListListener(this);
			}
			baseModel = FacadeFactory.getSupportedCampaigns(gameMode);
			model.setContents(ListFacades.wrap(baseModel));
			baseModel.addListListener(this);
		}
		
		public void elementAdded(ListEvent<CampaignFacade> e)
		{
			model.addElement(e.getIndex(), e.getElement());
		}
		
		public void elementRemoved(ListEvent<CampaignFacade> e)
		{
			model.removeElement(e.getIndex());
		}
		
		public void elementsChanged(ListEvent<CampaignFacade> e)
		{
			model.setContents(ListFacades.wrap(baseModel));
		}
		
		private static enum SourceTreeView implements
				TreeView<CampaignFacade>
		{
			
			NAME("Name"),
			PUBLISHER_NAME("Publisher/Name"),
			PUBLISHER_SETTING_NAME("Publisher/Setting/Name"),
			PUBLISHER_FORMAT_SETTING_NAME("Publisher/Format/Setting/Name");
			private String name;
			
			private SourceTreeView(String name)
			{
				this.name = name;
			}
			
			public String getViewName()
			{
				return name;
			}
			
			public List<TreeViewPath<CampaignFacade>> getPaths(CampaignFacade pobj)
			{
				String publisher = pobj.getPublisher();
				if (publisher == null)
				{
					publisher = "Other";
				}
				String setting = pobj.getSetting();
				String format = pobj.getFormat();
				switch (this)
				{
					case NAME:
						return Collections.singletonList(new TreeViewPath<CampaignFacade>(
								pobj));
					case PUBLISHER_FORMAT_SETTING_NAME:
						if (format != null && setting != null)
						{
							return Collections.singletonList(new TreeViewPath<CampaignFacade>(
									pobj, publisher, format, setting));
						}
						if (format != null)
						{
							return Collections.singletonList(new TreeViewPath<CampaignFacade>(
									pobj, publisher, format));
						}
					case PUBLISHER_SETTING_NAME:
						if (setting != null)
						{
							return Collections.singletonList(new TreeViewPath<CampaignFacade>(
									pobj, publisher, setting));
						}
					case PUBLISHER_NAME:
						return Collections.singletonList(new TreeViewPath<CampaignFacade>(
								pobj, publisher));
					default:
						throw new InternalError();
				}
			}
			
		}
		
	}

	/**
	 * The Class <code>CampaignRenderer</code> displays the tree cells of the
	 * source table.  
	 */
	private class CampaignRenderer extends DefaultTreeCellRenderer
	{

		/**
		 * Create a new renderer for the campaign names for a game mode. The 
		 * names will be coloured to show if they are qualified or not.
		 */
		public CampaignRenderer()
		{
			setTextNonSelectionColor(UIPropertyContext.getQualifiedColor());
			setClosedIcon(null);
			setLeafIcon(null);
			setOpenIcon(null);
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
													  boolean sel, boolean expanded, boolean leaf, int row,
													  boolean hasFocus)
		{

			super.getTreeCellRendererComponent(tree, value, sel, expanded,
											   leaf, row, hasFocus);
			Object campaignObj = ((DefaultMutableTreeNode) value).getUserObject();
			if (campaignObj instanceof CampaignFacade)
			{
				CampaignFacade campaign = (CampaignFacade) campaignObj;
				List<CampaignFacade> testCampaigns = selectedCampaigns.getContents();
				testCampaigns.add(campaign);
				if (!FacadeFactory.passesPrereqs(gameMode, testCampaigns))
				{
					setForeground(UIPropertyContext.getNotQualifiedColor());
				}
			}
			return this;
		}

	}
	
}
