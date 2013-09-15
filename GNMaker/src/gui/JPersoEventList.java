/**
 * 
 */
package gui;


import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Scrollable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import utils.GraphicHelper;

import net.miginfocom.swing.MigLayout;

import data.Event;
import data.Event.PersoEvent;
import data.Perso;

/**
 * Un JPanel pour lister les différents PersoEvent. Viewer de (MVC).
 * 
 * Display
 * Add a new PersoEvent
 * Remove a given PersoEvent
 * Expand a PersoEvent (LeftClick on JPersoEvent)
 * Expand All
 * 
 * @author snowgoon88@gmail.com
 */
public class JPersoEventList implements Observer {
	/** Un Event comme Model */
	Event _evt;
	
	/** JPanel comme Component */
	public JPanel _component;
	
	/** Panel for JPersoEvent */
	JPanel _persoPanel;
	/** Panel for PersoEvent description */
	JPanel _descPanel;
	
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
		
		_evt.addObserver(this);
	}
	
	void buildGUI() {
		// Internal components list
		_nameLabel = new ArrayList<JLabel>();
		_descArea = new ArrayList<JTextArea>();
		
		// Main Panel avec un MigLayout
		_component = new JPanel();
		MigLayout compLayout = new MigLayout(
				"debug,flowy", // Layout Constraints
				"2*indent[grow,fill]", // Column constraints
				""); // Row constraints);
		_component.setLayout(compLayout);
		
		// Liste des JPerso
		_persoPanel = new JPanel();
		MigLayout persoLayout = new MigLayout(
				"", // Layout Constraints
				"2*indent", // Column constraints
				""); // Row constraints);
		_persoPanel.setLayout(persoLayout);
		_expandAllBtn = new JButton();
		_expandAllBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setAllDescVisible(!_allDescVisible);
			}
		});
		_persoPanel.add(_expandAllBtn);
		
		// Description/Body pour chaque perso.
		//_descPanel = new JPanel();
		MigLayout descLayout = new MigLayout(
				"hidemode 3", // Layout Constraints
				"2*indent[grow,fill]", // Column constraints
				""); // Row constraints);
		//_descPanel.setLayout(descLayout);
		_descPanel = new MyPanel(descLayout);
		
		for (PersoEvent p : _evt._perso.values()) {
			JPersoEvent persoBtn = new JPersoEvent(p._perso, _evt);
			_persoPanel.add( persoBtn );
			PersoLabel nameLabel = new PersoLabel(p._perso.sDump(), p._perso);
			_nameLabel.add(nameLabel);
			_descPanel.add(nameLabel, "wrap"); // next est sur une autre ligne
			
			JTextArea descArea = new JTextArea(p.getDesc());
			descArea.setLineWrap(true);
			descArea.setWrapStyleWord(true);
			descArea.getDocument().addDocumentListener(new MyTextAreaListener(descArea, p));
			_descArea.add(descArea);
			_descPanel.add(descArea, "wrap, wmin 10"); // prend place, prochain sur autre ligne
			
			// Attache la bonne action 
			persoBtn._leftClickAction = new ExpandDescAction("Détaille", null, "Détaille "+p._perso.getName(),
					null, nameLabel, descArea);
		}
		setAllDescVisible(true);
		
		_component.add(_persoPanel);
		_component.add(_descPanel);
	}
	
	@Override
	// Implement Observer
	public void update(Observable o, Object arg) {
		if (arg != null ) {
			System.out.println("### JEvent.Observable : arg is a "+arg.getClass().getName());
		}
		else {
			System.out.println("### JEvent.Observable : arg is null");
		}
		// Ajout => arg est un Perso
		if (arg instanceof Event.PersoEvent) {
			PersoEvent pe = (PersoEvent) arg;
			
			JPersoEvent persoBtn = new JPersoEvent(pe._perso, _evt);
			_persoPanel.add( persoBtn );
			
			PersoLabel nameLabel = new PersoLabel(pe._perso.sDump(), pe._perso);
			_nameLabel.add(nameLabel);
			_descPanel.add(nameLabel, "wrap"); // next est sur une autre ligne
			
			JTextArea descArea = new JTextArea(pe.getDesc());
			descArea.setLineWrap(true);
			descArea.setWrapStyleWord(true);
			descArea.getDocument().addDocumentListener(new MyTextAreaListener(descArea, pe));
			_descArea.add(descArea);
			_descPanel.add(descArea, "wrap, wmin 10"); // prend place, prochain sur autre ligne
			
			// Attache la bonne action 
			persoBtn._leftClickAction = new ExpandDescAction("Détaille", null, 
					"Détaille "+pe._perso.getName(), null,
					nameLabel, descArea);
			_component.revalidate();
		}
		else if (arg instanceof String) {
			String command = (String) arg;
			if (command.equals("removed")) {
				// Peut-être pas propre car je fais l'hypothèse que les
				// JPersoEvent et les élements de _nameLabel et _descArea
				// sont stockés dans le même ordre.
				for (int i = 1; i < _persoPanel.getComponentCount(); i++) {
					JPersoEvent jpe = (JPersoEvent) _persoPanel.getComponent(i);
					if (_evt._perso.containsKey(jpe._pers) == false ) {
						_persoPanel.remove(jpe);
						_nameLabel.remove(i-1);
						_descArea.remove(i-1);

						// Enlever 2 fois du _descPanel
						_descPanel.remove( 2*(i-1));
						_descPanel.remove( 2*(i-1));
						
						_component.revalidate();
						return;
					}
				}
			}
		}
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
	
	@SuppressWarnings("serial")
	class PersoLabel extends JLabel implements Observer {
		Perso _pers;
		/**
		 * Label avec un Texte
		 * @param text
		 */
		public PersoLabel(String text, Perso pers) {
			super(text);
			_pers = pers;
			_pers.addObserver(this);
		}

		/**
		 * Listen to Perso
		 */
		@Override
		public void update(Observable o, Object arg) {
			String command = (String) arg;
			if (command.equals("set")) {
				this.setText(this._pers.sDump());
			}
		}
		
	}
	
	// http://stackoverflow.com/questions/2475787/miglayout-jtextarea-is-not-shrinking-when-used-with-linewrap-true
	@SuppressWarnings("serial")
	/**
	 * L'idée est que le Panel ne soit pas Scrollable Horizontalement.
	 */
	static class MyPanel extends JPanel implements Scrollable
	{
		MyPanel(LayoutManager layout) {
			super(layout);
		}
		public Dimension getPreferredScrollableViewportSize() {
			return getPreferredSize();
		}
		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
			return 0;
		}
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}
		public boolean getScrollableTracksViewportWidth() {
			return true;
		}
		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
			return 0;
		}
	}
	
	/**
	 * Listen for changes and update PersoEvent.
	 */
	class MyTextAreaListener implements DocumentListener {
		JTextArea _area;
		PersoEvent _pe;
		
		public MyTextAreaListener(JTextArea area, PersoEvent pe) {
			_area = area;
			_pe = pe;
		}
		
		@Override
		public void removeUpdate(DocumentEvent e) {
//			System.out.println("DOCUMENT REMOVE _title : "+e.toString());
			_pe.setDesc(_area.getText());
		}
		@Override
		public void insertUpdate(DocumentEvent e) {
//			System.out.println("DOCUMENT INSERT _title : "+e.toString());
			_pe.setDesc(_area.getText());
		}
		@Override
		public void changedUpdate(DocumentEvent e) {
//			System.out.println("DOCUMENT CHANGED _title : "+e.toString());
			// When properties change
		}
	}
}
