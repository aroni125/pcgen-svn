/*
 * LayOnHandsPane.java
 *
 * Created on February 9, 2004, 4:03 PM
 */

package plugin.charactersheet.gui;

import pcgen.core.PlayerCharacter;
import pcgen.io.exporttoken.VarToken;

import javax.swing.JCheckBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author  ddjone3
 */
public class PerDayPane extends javax.swing.JPanel
{
	private PlayerCharacter pc;
	private String var = "";
	private String var2 = "";
	private List<JCheckBox> checkList = new ArrayList<JCheckBox>();
	private Properties pcProperties;
	private boolean updateProperties = false;

	/** Creates new form LayOnHandsPane */
	public PerDayPane()
	{
		initComponents();
		setLocalColor();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents()
	{//GEN-BEGIN:initComponents
		jCheckBox2 = new javax.swing.JCheckBox();
		jPanel1 = new javax.swing.JPanel();
		title = new javax.swing.JLabel();
		jPanel2 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		jPanel5 = new javax.swing.JPanel();
		summaryLabel = new javax.swing.JLabel();
		jPanel6 = new javax.swing.JPanel();
		numDayLabel = new javax.swing.JLabel();
		checkPane = new javax.swing.JPanel();

		jCheckBox2.setText("jCheckBox2");

		setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

		jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		title.setFont(new java.awt.Font("Dialog", 1, 14));
		title.setText(" ");
		jPanel1.add(title);

		add(jPanel1);

		jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2,
			javax.swing.BoxLayout.X_AXIS));

		jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3,
			javax.swing.BoxLayout.Y_AXIS));

		jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		summaryLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		summaryLabel.setText(" ");
		jPanel5.add(summaryLabel);

		jPanel3.add(jPanel5);

		jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		numDayLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		jPanel6.add(numDayLabel);

		jPanel3.add(jPanel6);

		jPanel2.add(jPanel3);

		checkPane.setLayout(new javax.swing.BoxLayout(checkPane,
			javax.swing.BoxLayout.Y_AXIS));

		jPanel2.add(checkPane);

		add(jPanel2);

	}//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel checkPane;
	private javax.swing.JCheckBox jCheckBox2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JLabel numDayLabel;
	private javax.swing.JLabel summaryLabel;
	private javax.swing.JLabel title;

	// End of variables declaration//GEN-END:variables

	public void setColor()
	{
		setLocalColor();
		refresh();
	}

	public void setLocalColor()
	{
		jPanel1.setBackground(CharacterPanel.header);
		jPanel1.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
		jPanel2.setBackground(CharacterPanel.border);
		jPanel3.setBackground(CharacterPanel.header);
		jPanel3.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
		jPanel5.setBackground(CharacterPanel.header);
		jPanel6.setBackground(CharacterPanel.header);
		checkPane.setBackground(CharacterPanel.border);
		checkPane.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
	}

	public void setPc(PlayerCharacter pc, Properties pcProperties, String var,
		String var2, String title, String summary)
	{
		this.pc = pc;
		this.pcProperties = pcProperties;
		this.var = var;
		this.var2 = var2;
		this.title.setText(title);
		this.summaryLabel.setText(summary);
	}

	public void setPc(PlayerCharacter pc, Properties pcProperties, String var,
		String title, String summary)
	{
		setPc(pc, pcProperties, var, "", title, summary);
	}

	public void refresh()
	{
		int numDay = VarToken.getIntVarToken(pc, var, false);
		if (numDay > 0)
		{
			setVisible(true);
			StringBuffer sb = new StringBuffer();
			if (!var2.equals(""))
			{
				int var2text = VarToken.getIntVarToken(pc, var2, false);
				numDayLabel.setText(sb.append('(').append(var2text).append(')')
					.toString());
			}
			else
			{
				numDayLabel.setText(sb.append('(').append(numDay).append(')')
					.toString());
			}
			addCheckBoxes(numDay);
		}
		else
		{
			setVisible(false);
		}
		updatePane();
	}

	private void addCheckBoxes(int numDay)
	{
		if (checkList.size() != numDay)
		{
			checkList.clear();
			checkPane.removeAll();
			javax.swing.JPanel panel = new javax.swing.JPanel();
			for (int i = 0; i < numDay; i++)
			{
				if (i % 25 == 0)
				{
					panel = new javax.swing.JPanel();
					panel.setLayout(new java.awt.FlowLayout(
						java.awt.FlowLayout.LEFT, 0, 1));
					panel.setBackground(CharacterPanel.white);
					checkPane.add(panel);
				}
				if (i % 5 == 0)
				{
					javax.swing.JLabel bufLabel = new javax.swing.JLabel();
					bufLabel.setFont(new java.awt.Font("Dialog", 0, 10));
					bufLabel.setText(" ");
					panel.add(bufLabel);
				}
				JCheckBox checkBox = new JCheckBox();
				checkBox.setBackground(CharacterPanel.white);
				checkBox.setBorder(null);
				checkList.add(checkBox);
				panel.add(checkBox);
				checkBox.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						pc.setDirty(true);
						updateProperties();
					}
				});
			}
		}
	}

	public void updateProperties()
	{
		if (updateProperties)
		{
			int counter = 0;
			for (JCheckBox checkBox : checkList)
			{
				if (checkBox.isSelected())
				{
					counter++;
				}
			}
			pcProperties.put("cs.PerDayPane." + getPropTitle(), Integer
				.toString(counter));
		}
	}

	public void updatePane()
	{
		try
		{
			int counter =
					Integer.parseInt((String) pcProperties.get("cs.PerDayPane."
						+ getPropTitle()));
			for (JCheckBox checkBox : checkList)
			{
				if (counter > 0)
				{
					checkBox.setSelected(true);
					counter--;
				}
				else
				{
					checkBox.setSelected(false);
				}
			}
			updateProperties = true;
		}
		catch (NumberFormatException e)
		{
			//Do nothing
		}
	}

	private String getPropTitle()
	{
		String titleStr = this.title.getText();
		return titleStr.replaceAll(" ", "");
	}

	public void destruct()
	{
		//Put any code here that is needed to prevent memory leaks when this panel is destroyed
	}
}
