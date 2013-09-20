/**
 * 
 */
package gui;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import utils.GraphicHelper;

import net.miginfocom.swing.MigLayout;

import data.Event;
import data.Perso;
import data.Story;

/**
 * Display an Event qui peut être "expanded". On s'appuie sur MigLayout.
 * <li>bouton pour expander + JTextField pour le titre</li>
 * <li>JPersoEventList pour les différents Perso  x Event</li>
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class JEvent extends JPanel implements Observer {
	/** Un Event comme Model */
	public Event _evt;
	
	/** JPanel comme Component */
	public JPanel _component;
	
	/** Class for helping in designing GUI */
	ImageIcon _iconClosed = GraphicHelper.createImageIcon(this,"book-closed_32x32.png", "");
	ImageIcon _iconOpen = GraphicHelper.createImageIcon(this,"book-open_32x32.png", "");
	ImageIcon _iconAdd = GraphicHelper.createImageIcon(this,"user-group-new.png", "");
	
	JButton _expanderBtn;
	JTextField _title;
	JTextArea _body;
	JPersoEventList _persoList;
	
	/** Est-ce que le JEven est expanded ? */
	boolean _expandFlag;

	/**
	 * Création avec un Event comme Model
	 */
	public JEvent( Event evt ) {
		super(); // new JPanel
		_evt = evt;
		_expandFlag = true;
		
		buildGUI();
		
		// add observers
		_evt.addObserver(this);
	}

	/** 
	 * Crée les différents éléments SWING en utilisant un MigLayout
	 */
	void buildGUI() {
		MigLayout compLayout = new MigLayout(
				"debug, hidemode 3", // Layout Constraints
				"[grow,fill]", // Column constraints
				""); // Row constraints);
		this.setLayout(compLayout);
		
		
		// Default expander
		_expanderBtn = new JButton();
		_expanderBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				expandAction();
			}
		});
		this.add(_expanderBtn);
		// Add Perso
		JButton addBtn = new JButton(new AddPersoAction(_evt._story, this));
		addBtn.setText("");
		this.add(addBtn,
				"cell 0 0, grow 0");
		// Remove
		JButton removeBtn = new JButton(new RemoveEventAction(_evt));
		this.add(removeBtn,
				"cell 0 0, grow 0");
		
		_title = new JTextField( _evt.getTitle() );
		// Action Listener and Document Listener : when ENTER pressed.
//		_title.addActionListener( new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("ACTION _title : "+e.getActionCommand());
//			}
//		});
		_title.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
//				System.out.println("DOCUMENT REMOVE _title : "+e.toString());
				_evt.setTitle(_title.getText());
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
//				System.out.println("DOCUMENT INSERT _title : "+e.toString());
				_evt.setTitle(_title.getText());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
//				System.out.println("DOCUMENT CHANGED _title : "+e.toString());
				// When properties change
			}
		});
		this.add( _title,
				"cell 0 0, grow 100"); // go to next line after this
		_body = new JTextArea(_evt.getBody());
		_body.setLineWrap(true);
		_body.setWrapStyleWord(true);
		_body.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
//				System.out.println("DOCUMENT REMOVE _body : "+e.toString());
				_evt.setBody(_body.getText());
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
//				System.out.println("DOCUMENT INSERT _body : "+e.toString());
				_evt.setBody(_body.getText());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
//				System.out.println("DOCUMENT CHANGED _body : "+e.toString());
				// When properties change
			}
		});
		this.add( _body,
				"cell 0 1, gapx 2*indent, wmin 10");
		_persoList = new JPersoEventList(_evt);
		this.add( _persoList._component,
				"cell 0 2");
		
		update();
	}
	/**
	 * Mise à jour de ce qui est visible quand on expand.
	 */
	void update() {
		if (_expandFlag == true) {
			_expanderBtn.setIcon(_iconOpen);
			_persoList._component.setVisible(true);
			_persoList._component.revalidate();
			_body.setVisible(true);
			_body.revalidate();
		}
		else {
			_expanderBtn.setIcon(_iconClosed);
			_persoList._component.setVisible(false);
			_persoList._component.revalidate();
			_body.setVisible(false);
			_body.revalidate();
		}
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
		update(); // What is Visible ?
	}
	/**
	 * Action pour expander.
	 */
	void expandAction() {
		_expandFlag = !_expandFlag;
		update();
	}

	/**
	 * Ajoute un Perso à un Event.
	 */
	public class AddPersoAction extends AbstractAction {
 		Story _story;
 		Component _comp;
 	    
		public AddPersoAction(Story story, Component component) {
			super("Ajout Perso", _iconAdd);
			putValue(SHORT_DESCRIPTION, "Ajoute un Perso à un Evénement.");
			putValue(MNEMONIC_KEY, null);
			
			_story = story;
			_comp = component;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Perso choice = (Perso) JOptionPane.showInputDialog(_comp, 
					"Choisissez un Personnage à ajouter",
					"Ajout Perso", JOptionPane.PLAIN_MESSAGE, null,
					_story._persoList.toArray(), null);
			System.out.println("Choice is a "+choice.getClass().getName());
			System.out.println("Choice : "+choice.toString());
			
			// Ajoute seulemen si un nouveau
			if (_evt._persoMap.containsKey(choice) == false) {
				_evt.addPerso(choice);
			}
			else {
				JOptionPane.showMessageDialog(_comp,
					    choice.toString()+" est déjà impliqué.",
					    "Attention !",
					    JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	/**
	 * Crée un nouvel Event qu'on ajoute à une Story.
	 */
	public class RemoveEventAction extends AbstractAction {
		Event _evt;
		public RemoveEventAction(Event evt) {
			super("D", null);
			putValue(SHORT_DESCRIPTION, "Détruit cet Evénement de la Story");
			putValue(MNEMONIC_KEY, null);
			
			_evt = evt;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			_evt._story.remove(_evt);
		}
	}
	// http://stackoverflow.com/questions/2475787/miglayout-jtextarea-is-not-shrinking-when-used-with-linewrap-true
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
}
