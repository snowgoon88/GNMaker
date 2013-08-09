/**
 * 
 */
package gui;


import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import data.Event;
import data.Story;

/**
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class JStory extends JPanel {
	/** Un Story comme Model */
	Story _story;

	/**
	 * Création avec une Story comme Model.
	 */
	public JStory(Story story) {
		super(); // new Panel
		_story = story;

		buildGUI();
	}

	
	/** 
	 * Crée les différents éléments SWING en utilisant un MigLayout
	 */
	void buildGUI() {
		MigLayout compLayout = new MigLayout(
				"flowy, hidemode 3", // Layout Constraints
				"[grow,fill]", // Column constraints
				""); // Row constraints);
		this.setLayout(compLayout);
		
		// Liste des Event
		for (Event evt : _story._story) {
			this.add( new JEvent(evt));
		}
	}
}
