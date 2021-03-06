/**
 * 
 */
package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;


import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import utils.GraphicHelper;


import data.Event;
import data.Perso;

/**
 * Affiche un PersoEvent (principalement un Perso) en utilisant un JButton.
 * La couleur du JButton dépend PersoEvent.getStatus() (true=>vert, false=>rouge).
 * <ul>
 * <li>Button1 (Gauche) : _leftClickAction si pas null</li>
 * <li>Button3 (Droit) : popupMenu _popup</li>
 * <ul>
 * <li>Change Etat</li>
 * <li>RemovePersoAction()</li>
 * <li>Info</li>
 * </ul>
 * </ul>
 * 
 * Traite les messages suivants :
 * <ul>
 * <li> Perso.set : update _name et _popup.update()</li>
 * <li> PersoEvent.set_status : update() </li>
 * </ul>
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class PersoEventV extends JButton implements Observer {
	// Observes a PersoEvent, and thus a Perso
	public Event.PersoEvent _pe;
	// Need to know the Event to remove PersoEvent.
	Event _evt;
	MyJPopupMenu _popup;
	
	ImageIcon _iconRemove = GraphicHelper.createImageIcon(this,"user-group-delete.png", "");
	
	/* In order to Log */
	private static Logger logger = LogManager.getLogger(PersoEventV.class.getName());
	
	public AbstractAction _leftClickAction = null;
	
	/**
	 * Création avec un PersoEvent
	 * @param evt
	 * @param persoEvent
	 */
	public PersoEventV(Event evt, Event.PersoEvent persoEvent) {
		super();
		_evt = evt;
		_pe = persoEvent;
		_leftClickAction = new SwitchStatusAction();
		
		buildGUI();
		// Observes a PersoEvent, and thus a Perso
		_pe.addObserver(this);
		_pe._perso.addObserver(this);
	}
	
	/**
	 * Un JButton et un JPopup pour le context.
	 */
	void buildGUI() {
		this.setText(_pe._perso.getName());
		
		// Build the associated PopupMenu
		_popup = new MyJPopupMenu();
		
		// Button
		this.addMouseListener( new MyMouseListener());
		
		update();
	}
	
	/**
	 * Met à jour la couleur du JButton et l'info du Popup.
	 * A appeler après chaque changement.
	 */
	void update() {
		_popup.update();
		if (_pe.getStatus() == true) {
			this.setBackground( Color.GREEN );
		}
		else {
			this.setBackground( Color.RED );
		}
		this.revalidate();
	}
	
	/**
	 * MyMouseListener differentiate buttons.
	 * <ul>
	 * <li>BUTTON1 : _leftClickAction si pas null</li>
	 * <li>BUTTON2 : rien</li>
	 * <li>BUTTON3 : PopupMenu</li>
	 * </ul>
	 */
	class MyMouseListener extends MouseAdapter {

		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			if (e.getButton() == MouseEvent.BUTTON1) {
				System.out.println("JPersoEvent : Button 1");
				// use _leftClickAction if any
				if (_leftClickAction != null ) {
					_leftClickAction.actionPerformed(new ActionEvent(e.getSource(), 0, "PersoEventV"));
				}
			}
			else if (e.getButton() == MouseEvent.BUTTON2) {
				System.out.println("JPersoEvent : Button 2");
			}
			else if (e.getButton() == MouseEvent.BUTTON3) {
				System.out.println("JPersoEvent : Button 3");
				_popup.show(e.getComponent(), e.getX(), e.getY());
				System.out.println(_pe.sDump());
			}
		}
	}
	/**
	 * Un Popup avec action et info.
	 */
	class MyJPopupMenu extends JPopupMenu {
		JMenuItem _statusItem; // display info
		JMenuItem _switchItem; // switch status action
		public MyJPopupMenu() {
			JMenuItem item;
			// Switch status
			_switchItem = new JMenuItem("Change état");
			_switchItem.addActionListener(_leftClickAction);
			add(_switchItem);
			// Delete 
			item = new JMenuItem(new RemovePersoAction());
			add(item);
			// Separator
			add( new Separator());
			item = new JMenuItem("Info");
			add(item);
			_statusItem = new JMenuItem("-");
			add(_statusItem);
		}
		/**
		 * Met à jour les infos sur le Perso.
		 */
		public void update() {
			if (_pe._perso != null) {
				_statusItem.setText(_pe._perso.sDump());
			}
			else {
				_statusItem.setText("-");
			}
		}
	}
	
	/**
	 * Enlève un Perso à un Event.
	 */
	public class RemovePersoAction extends AbstractAction {
 	    
		public RemovePersoAction() {
			super("Enlève Perso", _iconRemove);
			putValue(SHORT_DESCRIPTION, "Enlève un Perso à un Evénement.");
			putValue(MNEMONIC_KEY, null);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			_evt.removePerso(_pe._perso);
		}
	}
	public class SwitchStatusAction extends AbstractAction {
		public SwitchStatusAction() {
			super("Switch status");
			putValue(SHORT_DESCRIPTION, "Change le status du Perso de l'événement");
			putValue(MNEMONIC_KEY, null);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			_pe.setStatus(!_pe.getStatus());
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// Log
		logger.debug(_pe._perso.getName()+" o is a "+o.getClass().getName()+ " arg="+arg);
		
		// Observe un Perso
		if (o instanceof Perso) {
			// only "set" message
			if (arg.equals("set")) {
				this.setText(_pe._perso.getName());
				_popup.update();
			}
		}
		// Observe a PersoEvent
		else if (o instanceof Event.PersoEvent) {
			// only "status" message
			if (arg.equals("set_status")) {
				update();
			}
		}
	}
}
