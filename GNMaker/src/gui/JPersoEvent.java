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
	Event _evt;
	Perso _pers;
	MyJPopupMenu _popup;
	
	ImageIcon _iconRemove = GraphicHelper.createImageIcon(this,"user-group-delete.png", "");
	
	public AbstractAction _leftClickAction = null;
	
	/**
	 * Création avec un PersoEvent
	 * @param evt
	 * @param pers
	 */
	public JPersoEvent(Perso pers, Event evt) {
		super(pers.getName());
		_evt = evt;
		_pers = pers;
		
		buildGUI();
		
		_pers.addObserver(this);
	}
	
	/**
	 * Un JButton et un JPopup pour le context.
	 */
	void buildGUI() {
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
		if (_evt.getStatusPerso(_pers) == true) {
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
		_evt.setStatusPerso(_pers, !_evt.getStatusPerso(_pers));
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
			item = new JMenuItem(new RemovePersoAction(_pers));
			add(item);
			// Separator
			add( new Separator());
			item = new JMenuItem("Info");
			add(item);
			_statusItem = new JMenuItem("-");
			add(_statusItem);
		}
		public void update() {
			if (_pers != null) {
				_statusItem.setText(_pers.SDump());
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
 		Perso _perso;
 	    
		public RemovePersoAction(Perso perso) {
			super("Enlève Perso", _iconRemove);
			putValue(SHORT_DESCRIPTION, "Enlève un Perso à un Evénement.");
			putValue(MNEMONIC_KEY, null);
			
			_perso = perso;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			_evt.removePerso(_perso);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// Observe un Perso
		// arg = "set"
		this.setName(_pers.getName());
		_popup.update();
	}
}
