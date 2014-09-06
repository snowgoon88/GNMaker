/**
 * 
 */
package gui;


import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import net.miginfocom.swing.MigLayout;

import data.Event;
import data.Story;

/**
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class JStory extends JPanel implements Scrollable, Observer {
	/** Un Story comme Model */
	Story _story;
	
	/**
	 * Création avec une Story comme Model.
	 */
	public JStory(Story story) {
		//super(); // new Panel
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
			this.add( new EventV(evt));
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
			this.add( new EventV( (Event)arg ));
			this.revalidate();
		}
		else if (arg instanceof String) {
			String command = (String) arg;
			if (command.equals("removed")) {
				System.out.println("Find the one to remove");
				// Find removed one.
				for (int i = 0; i < this.getComponentCount(); i++) {
					EventV comp = (EventV) this.getComponent(i);
					if (_story._story.contains(comp._evt) == false ) {
						System.out.println("REMOVE "+i+" "+comp._evt.getTitle());
						this.remove(i);
						
						this.revalidate();
						this.repaint();
						return;
					}
				}
			}
		}
	}
	// http://stackoverflow.com/questions/2475787/miglayout-jtextarea-is-not-shrinking-when-used-with-linewrap-true
	/**
	 * L'idée est que le Panel ne soit pas Scrollable Horizontalement.
	 */

	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 0;
	}
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 0;
	}
}
