/*
 * UnarmedPane.java
 *
 * Created on February 3, 2004, 3:23 PM
 */

package plugin.charactersheet.gui;

import gmgen.gui.GridBoxLayout;
import pcgen.core.Equipment;
import pcgen.core.PlayerCharacter;
import pcgen.io.exporttoken.WeaponToken;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author  ddjone3
 */
public class NaturalWeaponPane extends JPanel
{
	private PlayerCharacter pc;
	private Equipment eq;
	private JPanel bonusLPanel;
	private JPanel damageLPanel;
	private JPanel bonusPanel;
	private JPanel damagePanel;
	private JLabel totalAttackBonus;
	private JLabel damage;

	/** Creates new form UnarmedPane */
	public NaturalWeaponPane()
	{
		initComponents();
		setColor();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents()
	{
		setLayout(new GridBoxLayout(2, 2));

		bonusLPanel = new JPanel();
		damageLPanel = new JPanel();
		bonusPanel = new JPanel();
		damagePanel = new JPanel();

		JLabel tabLabel = new JLabel();
		bonusLPanel.setLayout(new java.awt.FlowLayout(
			java.awt.FlowLayout.CENTER, 1, 0));
		tabLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		tabLabel.setText("Total Attack Bonus");
		bonusLPanel.add(tabLabel);
		add(bonusLPanel);

		JLabel damLabel = new JLabel();
		damageLPanel.setLayout(new java.awt.FlowLayout(
			java.awt.FlowLayout.CENTER, 1, 0));
		damLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		damLabel.setText("Damage");
		damageLPanel.add(damLabel);
		add(damageLPanel);

		totalAttackBonus = new JLabel();
		bonusPanel.setLayout(new java.awt.FlowLayout(
			java.awt.FlowLayout.CENTER, 1, 0));
		totalAttackBonus.setFont(new java.awt.Font("Dialog", 0, 10));
		totalAttackBonus.setText("Total Attack Bonus");
		bonusPanel.add(totalAttackBonus);
		add(bonusPanel);

		damage = new JLabel();
		damagePanel.setLayout(new java.awt.FlowLayout(
			java.awt.FlowLayout.CENTER, 1, 0));
		damage.setFont(new java.awt.Font("Dialog", 0, 10));
		damage.setText("Damage");
		damagePanel.add(damage);
		add(damagePanel);
	}

	public void setColor()
	{
		setBackground(CharacterPanel.border);
		setBorder(new javax.swing.border.LineBorder(CharacterPanel.border));
		bonusLPanel.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
		bonusLPanel.setBackground(CharacterPanel.header);
		damageLPanel.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
		damageLPanel.setBackground(CharacterPanel.header);
		bonusPanel.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
		bonusPanel.setBackground(CharacterPanel.bodyLight);
		damagePanel.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
		damagePanel.setBackground(CharacterPanel.bodyLight);
	}

	public void setWeapon(PlayerCharacter pc, Equipment eq)
	{
		this.pc = pc;
		this.eq = eq;
		refresh();
	}

	public void refresh()
	{
		if (eq.isNatural())
		{
			totalAttackBonus.setText(WeaponToken.getTotalHitToken(pc, eq));
			damage.setText(WeaponToken.getDamageToken(pc, eq, false, false));

			this.setVisible(true);
		}
		else
		{
			this.setVisible(false);
		}
	}
}
