/**
 * 
 */
package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
 * Essai à base de JButton
 * <li>Button1 (Gauche) : change état (status)</li>
 * <li>Button3 (Droit) : popupMenu</li>
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class JPersoEvent extends JButton implements Observer {
	// Observes a PersoEvent, and thus a Perso
	public Event.PersoEvent _pe;
	// Need to know the Event to remove PersoEvent.
	Event _evt;
	MyJPopupMenu _popup;
	
	ImageIcon _iconRemove = GraphicHelper.createImageIcon(this,"user-group-delete.png", "");
	
	/* In order to Log */
	private static Logger logger = LogManager.getLogger(JPersoEvent.class.getName());
	
	public AbstractAction _leftClickAction = null;
	
	/**
	 * Création avec un PersoEvent
	 * @param evt
	 * @param perso
	 */
	public JPersoEvent(Event evt, Event.PersoEvent persoEvent) {
		super();
		_evt = evt;
		_pe = persoEvent;
		
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
	 * Action pour changer l'état (status) du PersoEvent.
	 */
	void switchStateAction() {
		_pe.setStatus(!_pe.getStatus());
		update();
	}
	
	/**
	 * MyMouseListener differentiate buttons.
	 * BUTTON1 : switch PersoEvent status
	 * BUTTON3 : PopupMenu
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
				// Switch status
				//switchStateAction();
				if (_leftClickAction != null ) {
					// Create ActionEvent and activate
					_leftClickAction.actionPerformed(new ActionEvent(e.getSource(), 0, "JPersoEvent"));
				}
			}
			else if (e.getButton() == MouseEvent.BUTTON2) {
				System.out.println("JPersoEvent : Button 2");
			}
			else if (e.getButton() == MouseEvent.BUTTON3) {
				System.out.println("JPersoEvent : Button 3");
				_popup.show(e.getComponent(), e.getX(), e.getY());
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
			_switchItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchStateAction();
				}
			});
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

	@Override
	public void update(Observable o, Object arg) {
		// Log
		logger.debug(_pe._perso.getName()+" o is a "+o.getClass().getName()+ " arg="+arg);
		
		// Observe un Perso
		if (o.getClass() == Perso.class) {
			// only "set" message
			if (arg.equals("set")) {
				this.setName(_pe._perso.getName());
				_popup.update();
			}
		}
		// Observe a PersoEvent
		else if (o.getClass() == Event.PersoEvent.class) {
			// only "status" message
			if (arg.equals("status")) {
				update();
			}
		}
	}
}
