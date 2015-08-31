/**
 * 
 */
package gui;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
//import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import utils.GraphicHelper;

import net.miginfocom.swing.MigLayout;

import data.Event;
import data.Perso;
import data.Story;
//import editor.example.JDocEditor;

/**
 * Display an Event qui peut être "expanded". On s'appuie sur MigLayout.
 * 
 * Composé de:
 * <ul>
 * <li> _expanderBtn : JButton pour expander</li>
 * <li> deux JButton intern : ajouter un PersoEvent, Enlever ce Event</li>
 * <li> _title : JTextField pour le titre</li>
 * <li> _body : JTextArea pour le body </li>
 * <li> _persoList : PersoEventListV pour les différents Perso  x Event</li>
 * </ul>
 * 
 * Traite les messages suivants :
 * <ul>
 * <li> Event.set_title : maj _title => Ne marche pas</li>
 * <li> Event.set_body : maj _body => Ne marche pas </li>
 * </ul>
 * 
 * @todo _body peut pas être édité.
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class EventV extends MigPanel implements Observer {
	/** Un Event comme Model */
	public Event _evt;
	
	
	/** Class for helping in designing GUI */
	ImageIcon _iconClosed = GraphicHelper.createImageIcon(this,"book-closed_32x32.png", "");
	ImageIcon _iconOpen = GraphicHelper.createImageIcon(this,"book-open_32x32.png", "");
	ImageIcon _iconAdd = GraphicHelper.createImageIcon(this,"user-group-new.png", "");
	
	JButton _expanderBtn;
	JTextField _title;
	//JTextArea _body;
	DocEditorV _body;
	PersoEventListV _persoList;
	
	/* In order to Log */
	private static Logger logger = LogManager.getLogger(EventV.class.getName());
	
	/** Est-ce que le JEven est expanded ? */
	boolean _expandFlag;

	/**
	 * Création avec un Event comme Model
	 */
	public EventV( Event evt ) {
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
				logger.debug("DOCUMENT REMOVE _title : "+e.toString());
				//_evt.setTitle(_title.getText());
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				logger.debug("DOCUMENT INSERT _title : "+e.toString());
				//_evt.setTitle(_title.getText());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				logger.debug("DOCUMENT CHANGED _title : "+e.toString());
				// When properties change
			}
		});
		this.add( _title,
				"cell 0 0, grow 100"); // go to next line after this
//		_body = new JTextArea(_evt.getBody());
//		_body.setLineWrap(true);
//		_body.setWrapStyleWord(true);
		_body = new DocEditorV(_evt.getBody());
		_evt.getBody().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				logger.debug("DOCUMENT REMOVE _body : "+e.toString());
				//_evt.setBody(_body.getText());
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				logger.debug("DOCUMENT INSERT _body : "+e.toString());
//				_evt.setBody(_body.getText());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				logger.debug("DOCUMENT CHANGED _body : "+e.toString());
				// When properties change
			}
		});
		this.add( _body,
				"cell 0 1, gapx 2*indent, wmin 10");
		_persoList = new PersoEventListV(_evt);
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
		// Log
		logger.debug(_evt.getTitle()+" o is a "+o.getClass().getName()+ " arg="+arg);
		
		// Observe un Event
		if (o instanceof Event) {
			// "set_title" message
			if (arg.equals("set_title")) {
				// Ne marche pas
				_title.setText( _evt.getTitle());
			}
			// "set_body" message
			else if (arg.equals("set_body")) {
				logger.warn( "NO ANSWER to 'set_body'");
				// Ne marche pas
				//_body.setText(_evt.getBody());
				//update(); // What is Visible ?
			}
		}
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
			// An Array ot perso
			Perso[] persoArray = _story._persoList.toArray(new Perso[0]);
			Perso choice = (Perso) JOptionPane.showInputDialog(_comp, 
					"Choisissez un Personnage à ajouter",
					"Ajout Perso", JOptionPane.PLAIN_MESSAGE, null,
					persoArray, null);
			if (choice != null ) {
				System.out.println("Choice is a "+choice.getClass().getName());
				System.out.println("Choice : "+choice.toString());

				// Ajoute seulemen si un nouveau
//				if (_evt._persoMap.containsKey(choice) == false) {
				if (_evt._listPE.get(choice.getId()) == null) {	
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
}
