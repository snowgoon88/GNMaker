/**
 * 
 */
package gui;


import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

import data.Event;
import data.Story;

/**
 * @author snowgoon88@gmail.com
 */
public class StoryC {
	/** Story to control */
	Story _story;
	
	public JPanel _component;
	
	public StoryC(Story story) {
		this._story = story;
		buildGUI();
	}
	public void buildGUI() {
		_component = new JPanel();
		
		JButton newEvtBtn = new JButton( new NewEventAction() );
		_component.add(newEvtBtn);
	}
	
	/**
	 * Crée un nouvel Event qu'on ajoute à une Story.
	 */
	@SuppressWarnings("serial")
	public class NewEventAction extends AbstractAction {

		public NewEventAction() {
			super("Nouvel Evt", null);
			putValue(SHORT_DESCRIPTION, "Ajoute un Evénement à une Story.");
			putValue(MNEMONIC_KEY, null);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Event evt = new Event(_story, "Nouvel Evénement", "-A définir-");
			_story.add(evt);
		}
	}
}
