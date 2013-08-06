/**
 * 
 */
package gui;


import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import data.Event;
import data.Perso;

/**
 * Display an Event qui peut être "expanded", utilise un GroupLayout.
 * <li>bouton pour expander</li>
 * <li>JTextField pour le titre</li>
 * <li>JPanel avec FlowLayout pour les JPersoEvent</li>
 * <li>JTextArea pour les body</li>
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class JEvent extends JPanel {

	JButton _expander;
	JTextField _title;
	JTextArea _body;
	JPanel _persoList;
	
	/** Est-ce que le JEven est expanded ? */
	boolean _expandFlag;
	/** Model Event */
	Event _evt;
	/**
	 * Création avec un Event comme Model
	 */
	public JEvent( Event evt ) {
		_evt = evt;
		_expandFlag = false;
		
		buildGUI();
	}

	/** 
	 * Crée les différents éléments SWING en utilisant un GroupLayout
	 */
	void buildGUI() {
		// Default expander
		_expander = new JButton("m");
		_expander.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				expandAction();
			}
		});

		_title = new JTextField( _evt._title );
		_persoList = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPersoEvent pe;
		for (Map.Entry<Perso, Event.PersoEvent> e : _evt._perso.entrySet()) {
			pe = new JPersoEvent(e.getKey(), _evt);
			_persoList.add(pe._btn);
		}
		_persoList.revalidate();
		_body = new JTextArea(_evt._body);
		
		// Group Layout
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		// Horizontal
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addComponent(_expander)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(_title)
						.addComponent(_persoList)
						.addComponent(_body)
						)
				);
						
		// Vertical
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(_expander)
				.addGroup(layout.createSequentialGroup()
						// Fixed vertical size pour _title
						.addComponent(_title, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						          GroupLayout.PREFERRED_SIZE)
						.addComponent(_body)
						.addComponent(_persoList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						          GroupLayout.PREFERRED_SIZE)
						)
				);
		update();
	}
	/**
	 * Mise à jour de ce qui est visible quand on expand.
	 */
	void update() {
		if (_expandFlag == true) {
			_expander.setText("P");
			_persoList.setVisible(true);
			_persoList.revalidate();
			_body.setVisible(true);
			_body.revalidate();
		}
		else {
			_expander.setText("m");
			_persoList.setVisible(false);
			_persoList.revalidate();
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
