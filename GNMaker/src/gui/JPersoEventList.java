/**
 * 
 */
package gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import utils.GraphicHelper;

import net.miginfocom.swing.MigLayout;

import data.Event;
import data.Event.PersoEvent;

/**
 * Un JPanel pour lister les différents PersoEvent. Viewer de (MVC).
 * 
 * Display
 * TODO Add a new PersoEvent
 * TODO Remove a given PersoEvent
 * Expand a PersoEvent (LeftClick on JPersoEvent)
 * Expand All
 * TODO Dump all PersoEvent
 * 
 * @author snowgoon88@gmail.com
 */
public class JPersoEventList {
	/** Un Event comme Model */
	Event _evt;
	
	/** JPanel comme Component */
	public JPanel _component;
	
	/** ExpandAll Button */
	JButton _expandAllBtn;
	
	/** Class for helping in designing GUI */
	ImageIcon _iconClosed = GraphicHelper.createImageIcon(this,"book-closed_32x32.png", "");
	ImageIcon _iconOpen = GraphicHelper.createImageIcon(this,"book-open_32x32.png", "");
	
	/** Map de description */
	ArrayList<JTextArea> _descArea;
	/** Map de name */
	ArrayList<JLabel> _nameLabel;
	
	/** Status Flag pour AllDescExpand */
	boolean _allDescVisible = true;

	/**
	 * Création avec un Event comme modèle (MVC).
	 * @param _evt
	 */
	public JPersoEventList(Event _evt) {
		this._evt = _evt;
		buildGUI();
	}
	
	void buildGUI() {
		// Internal components list
		_nameLabel = new ArrayList<JLabel>();
		_descArea = new ArrayList<JTextArea>();
		
		// Main Panel avec un MigLayout
		_component = new JPanel();
		MigLayout compLayout = new MigLayout(
				"flowy", // Layout Constraints
				"2*indent[grow,fill]", // Column constraints
				""); // Row constraints);
		_component.setLayout(compLayout);
		
		// Liste des JPerso
		JPanel persoPanel = new JPanel();
		MigLayout persoLayout = new MigLayout(
				"", // Layout Constraints
				"2*indent", // Column constraints
				""); // Row constraints);
		persoPanel.setLayout(persoLayout);
		_expandAllBtn = new JButton();
		_expandAllBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setAllDescVisible(!_allDescVisible);
			}
		});
		persoPanel.add(_expandAllBtn);
		
		// Description/Body pour chaque perso.
		JPanel descPanel = new JPanel();
		MigLayout descLayout = new MigLayout(
				"hidemode 3", // Layout Constraints
				"2*indent[grow,fill]", // Column constraints
				""); // Row constraints);
		descPanel.setLayout(descLayout);
		
		for (PersoEvent p : _evt._perso.values()) {
			JPersoEvent persoBtn = new JPersoEvent(p._perso, _evt);
			persoPanel.add( persoBtn._btn );
			JLabel nameLabel = new JLabel(p._perso.SDump());
			_nameLabel.add(nameLabel);
			descPanel.add(nameLabel, "wrap"); // next est sur une autre ligne
			
			JTextArea descArea = new JTextArea(p._desc);
			_descArea.add(descArea);
			descPanel.add(descArea, "wrap, growx, growy"); // prend place, prochain sur autre ligne
			
			// Attache la bonne action 
			persoBtn._leftClickAction = new ExpandDescAction("Détaille", null, "Détaille "+p._perso._name, null,
					nameLabel, descArea);
		}
		setAllDescVisible(true);
		
		_component.add(persoPanel);
		_component.add(descPanel);
	}
	

	@SuppressWarnings("serial")
	/**
	 * Change l'état visible du descriptif d'un PersoEvent (label+desc).
	 */
	class ExpandDescAction extends AbstractAction {
		JLabel _label;
		JTextArea _desc;
		public ExpandDescAction(String text, ImageIcon icon, String help, Integer mnemonic,
				JLabel label, JTextArea desc) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, help);
			putValue(MNEMONIC_KEY, mnemonic);
			_label = label;
			_desc = desc;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("ExpandDescAction pour "+_label.getText());
			_label.setVisible( !_label.isVisible());
			_desc.setVisible( !_desc.isVisible());
			_label.revalidate();
			_desc.revalidate();
		}
	}
	
	/**
	 * Rend tous les Descriptifs de PersoEvent (label+Desc) visibles ou non.
	 * @param aFlag true pour allVisible.
	 */
	void setAllDescVisible( boolean aFlag) {
		_allDescVisible = aFlag;
		for (JLabel l : _nameLabel) {
			l.setVisible(_allDescVisible);
		}
		for (JTextArea a : _descArea) {
			a.setVisible(_allDescVisible);
		}
		if (_allDescVisible) {
			_expandAllBtn.setIcon(_iconOpen);
		}
		else {
			_expandAllBtn.setIcon(_iconClosed);
		}
		_component.revalidate();
	}
}
