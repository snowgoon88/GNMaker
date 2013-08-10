/**
 * 
 */
package gui;


import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import data.Event;
import data.Story;

/**
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class JStory extends JPanel implements Observer {
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


	@Override
	public void update(Observable o, Object arg) {
		if (arg != null ) {
			System.out.println("### JStory.Observable : arg is a "+arg.getClass().getName());
		}
		else {
			System.out.println("### JStory.Observable : arg is null");
		}
		// Ajout => arg est un Event qui a été ajouté.
		if (arg instanceof Event) {
			this.add( new JEvent( (Event)arg ));
			this.revalidate();
		}
		else if (arg instanceof String) {
			String command = (String) arg;
			if (command.equals("removed")) {
				System.out.println("Find the one to remove");
				// Find removed one.
				for (int i = 0; i < this.getComponentCount(); i++) {
					JEvent comp = (JEvent) this.getComponent(i);
					if (_story._story.contains(comp._evt) == false ) {
						System.out.println("REMOVE "+i+" "+comp._evt._title);
						this.remove(i);
						
						this.revalidate();
						this.repaint();
						return;
					}
				}
			}
		}
	}
}
