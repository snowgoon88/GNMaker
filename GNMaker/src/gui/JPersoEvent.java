/**
 * 
 */
package gui;

import javax.swing.JButton;


import data.Event;
import data.Perso;

/**
 * Essai de JButton avec comportements différents pour bouton gauche et 
 * droit.
 * VERSION DE TEST
 * @author snowgoon88@gmail.com
 */
public class JPersoEvent {
	public JButton _btn;
	Event _evt;
	Perso _pers;
	
	public JPersoEvent(Event evt, Perso pers) {
		_evt = evt;
		_pers = pers;
		_btn = new JButton(_pers._name);
		_btn.setVisible(true);
	}
	
	
}
