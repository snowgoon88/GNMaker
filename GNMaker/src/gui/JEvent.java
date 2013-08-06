/**
 * 
 */
package gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import data.Event;

/**
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class JEvent extends JPanel {

	JButton _expander;
	JTextField _title;
	JTextArea _body;
	//JPanel _persoList;
	
	boolean _expandFlag;
	Event _evt;
	/**
	 * 
	 */
	public JEvent( Event evt ) {
		_evt = evt;
		_expandFlag = false;
		
		buildGUI();
	}

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
						)
				);
		update();
	}
	void update() {
		if (_expandFlag == true) {
			_expander.setText("P");
			_body.setVisible(true);
			_body.revalidate();
		}
		else {
			_expander.setText("m");
			_body.setVisible(false);
			_body.revalidate();
		}
	}
	
	void expandAction() {
		_expandFlag = !_expandFlag;
		update();
	}
	

}
