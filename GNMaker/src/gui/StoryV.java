/**
 * 
 */
package gui;


import gui.PersoListV.PersoPanel;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.miginfocom.swing.MigLayout;

import data.Event;
import data.Perso;
import data.Story;

/**
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class StoryV extends JPanel implements Scrollable, Observer {
	/** Un Story comme Model */
	Story _story;
	
	/* In order to Log */
	private static Logger logger = LogManager.getLogger(StoryV.class.getName());

	
	/**
	 * Création avec une Story comme Model.
	 */
	public StoryV(Story story) {
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
		for (Entry<Integer, Event> entry : _story._story.entrySet()) {
			if (entry.getKey() >= 0) {
				this.add( new EventV(entry.getValue()));
			}
		}
	}


	@Override
	public void update(Observable o, Object arg) {
		// Log
		logger.debug("o is a "+o.getClass().getName()+ " arg="+arg);
		
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
					int id_event = comp._evt.getId();
					if (_story._story.indexOf( comp._evt) == id_event ) {
					//if (_story._story.contains(comp._evt) == false ) {
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
