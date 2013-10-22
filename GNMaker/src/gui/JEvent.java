/**
 * 
 */
package gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import utils.GraphicHelper;

import net.miginfocom.swing.MigLayout;

import data.Event;

/**
 * Display an Event qui peut être "expanded". On s'appuie sur MigLayout.
 * <li>bouton pour expander + JTextField pour le titre</li>
 * <li>JPersoEventList pour les différents Perso  x Event</li>
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class JEvent extends JPanel {
	/** Un Event comme Model */
	Event _evt;
	
	/** JPanel comme Component */
	public JPanel _component;
	
	/** Class for helping in designing GUI */
	ImageIcon _iconClosed = GraphicHelper.createImageIcon(this,"book-closed_32x32.png", "");
	ImageIcon _iconOpen = GraphicHelper.createImageIcon(this,"book-open_32x32.png", "");
	
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
	}

	/** 
	 * Crée les différents éléments SWING en utilisant un MigLayout
	 */
	void buildGUI() {
		MigLayout compLayout = new MigLayout(
				"hidemode 3", // Layout Constraints
				"[][grow,fill]", // Column constraints
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

		_title = new JTextField( _evt._title );
		this.add( _title, "wrap"); // go to next line after this
		_body = new JTextArea(_evt._body);
		this.add( _body, "skip, wrap");
		_persoList = new JPersoEventList(_evt);
		this.add( _persoList._component, "spanx 2");
		
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
	/**
	 * Action pour expander.
	 */
	void expandAction() {
		_expandFlag = !_expandFlag;
		update();
	}
	

}
