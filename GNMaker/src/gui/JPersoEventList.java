/**
 * 
 */
package gui;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

import data.Event;
import data.Perso;
import data.Event.PersoEvent;

/**
 * Un JPanel pour lister les différents PersoEvent. Viewer de (MVC).
 * 
 * Display
 * TODO Add a new PersoEvent
 * TODO Remove a given PersoEvent
 * Expand a PersoEvent (LeftClick on JPersoEvent)
 * TODO Expand All
 * TODO Dump all PersoEvent
 * 
 * @author snowgoon88@gmail.com
 */
public class JPersoEventList {
	/** Un Event comme Model */
	Event _evt;
	
	/** JPanel comme Component */
	public JPanel _component;
	
	/** Map de description */
	HashMap<Perso,JTextArea> _descArea;
	/** Map de name */
	HashMap<Perso,JLabel> _nameLabel;

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
		_nameLabel = new HashMap<Perso,JLabel>();
		_descArea = new HashMap<Perso,JTextArea>();
		
		// Main Panel avec un BoxLayout
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
			_nameLabel.put(p._perso, nameLabel);
			descPanel.add(nameLabel, "wrap"); // next est sur une autre ligne
			
			JTextArea descArea = new JTextArea(p._desc);
			_descArea.put(p._perso, descArea);
			descPanel.add(descArea, "wrap, growx, growy"); // prend place, prochain sur autre ligne
			
			// Attache la bonne action 
			persoBtn._leftClickAction = new ExpandDescAction("Détaille", null, "Détaille "+p._perso._name, null,
					nameLabel, descArea);
		}
		_component.add(persoPanel);
		_component.add(descPanel);
	}
	

//	/**
//	 * Reset Arm.
//	 */
//	@SuppressWarnings("serial")
//	class ResetAction extends AbstractAction {
//		public ResetAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
//			super(text, icon);
//			putValue(SHORT_DESCRIPTION, desc);
//			putValue(MNEMONIC_KEY, mnemonic);
//		}
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			reset( _ang0SpinModel.getNumber().doubleValue(),
//					_ang1SpinModel.getNumber().doubleValue());
//		}
//	}
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
}
