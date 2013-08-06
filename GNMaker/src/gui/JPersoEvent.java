/**
 * 
 */
package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


import data.Event;
import data.Perso;

/**
 * Essai à base de JButton
 * <li>Button1 (Gauche) : change état (status)</li>
 * <li>Button3 (Droit) : popupMenu</li>
 * 
 * @author snowgoon88@gmail.com
 */
public class JPersoEvent {
	public JButton _btn;
	Event _evt;
	Perso _pers;
	MyJPopupMenu _popup;
	
	/**
	 * Création avec un PersoEvent
	 * @param evt
	 * @param pers
	 */
	public JPersoEvent(Perso pers, Event evt) {
		_evt = evt;
		_pers = pers;
		
		buildGUI();
	}
	
	/**
	 * Un JButton et un JPopup pour le context.
	 */
	void buildGUI() {
		// Build the associated PopupMenu
		_popup = new MyJPopupMenu();
		
		// Button
		_btn = new JButton(_pers._name);
		_btn.addMouseListener( new MyMouseListener());
		
		update();
	}
	
	/**
	 * Met à jour la couleur du JButton et l'info du Popup.
	 * A appeler après chaque changement.
	 */
	void update() {
		_popup.update();
		if (_evt.getStatusPerso(_pers) == true) {
			_btn.setBackground( Color.GREEN );
		}
		else {
			_btn.setBackground( Color.RED );
		}
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
				switchStateAction();
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
	@SuppressWarnings("serial")
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
			item = new JMenuItem("Enlève Perso");
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
	
}
