/**
 * 
 */
package gui;


import gui.PersoListV.PersoPanel;
import gui.ZorgaListV.ZorgaPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.miginfocom.swing.MigLayout;

import data.Event;
import data.ListOf;
import data.Perso;
import data.Story;

/**
 * C'est un Panel qui contient des EventV.
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class EventListV extends JPanel implements Observer {
	/** Un Story comme Model */
	ListOf<Event> _eventList;
	
	/** Panel pour les EventV */
	MyPanel _listPanel;
	
	/* In order to Log */
	private static Logger logger = LogManager.getLogger(EventListV.class.getName());

	
	/**
	 * Création avec une Story comme Model.
	 */
	public EventListV(ListOf<Event> events) {
		super();
		this._eventList = events;
		_eventList.addObserver(this);

		buildGUI();
	}

	
	/** 
	 * Crée les différents éléments SWING en utilisant un MigLayout
	 */
	void buildGUI() {
		this.setLayout(new BorderLayout());
		
		MigLayout eventLayout = new MigLayout(
				"debug,flowy, hidemode 3", // Layout Constraints
				"[grow,fill]", // Column constraints
				""); // Row constraints);
		_listPanel = new MyPanel(eventLayout);
		this.add(_listPanel, BorderLayout.CENTER);
		
		// Liste des Event
		for (Entry<Integer, Event> entry : _eventList.entrySet()) {
			if (entry.getKey() >= 0) {
				_listPanel.add( new EventV(entry.getValue()));
			}
		}
	}


	@Override
	public void update(Observable o, Object arg) {
		// Log
		logger.debug("o is a "+o.getClass().getName()+ " arg="+arg);

		// Observe ListOf<Event>
		if (arg != null) {
			if (arg instanceof String) {
				StringTokenizer sTok = new StringTokenizer((String)arg, "_");
				int id = Integer.parseInt(sTok.nextToken());
				String command = sTok.nextToken();
				// "add" -> une nouvelle ligne dans _eventPanel.
				if (command.equals("add")) {
					_listPanel.add( new EventV( _eventList.get(id)) );
					this.revalidate();
					this.repaint();
				}
				// "del" efface le composant incriminé
				else if (command.equals("del")){
					for (int i = 0; i < _listPanel.getComponentCount(); i++) {
						EventV eventPanel = (EventV) _listPanel.getComponent(i);
						if (eventPanel._evt.equals(_eventList.get(id))) {
							_listPanel.remove(i);
							this.revalidate();
							this.repaint();
							return;
						}
						
					}
				}
			}
		}
	}
		
//		// Ajout => arg est un Event qui a été ajouté.
//		if (arg instanceof Event) {
//			this.add( new EventV( (Event)arg ));
//			this.revalidate();
//		}
//		else if (arg instanceof String) {
//			String command = (String) arg;
//			if (command.equals("removed")) {
//				System.out.println("Find the one to remove");
//				// Find removed one.
//				for (int i = 0; i < this.getComponentCount(); i++) {
//					EventV comp = (EventV) this.getComponent(i);
//					int id_event = comp._evt.getId();
//					if (_story._story.indexOf( comp._evt) == id_event ) {
//					//if (_story._story.contains(comp._evt) == false ) {
//						System.out.println("REMOVE "+i+" "+comp._evt.getTitle());
//						this.remove(i);
//						
//						this.revalidate();
//						this.repaint();
//						return;
//					}
//				}
//			}
//		}
//	}
	
	// http://stackoverflow.com/questions/2475787/miglayout-jtextarea-is-not-shrinking-when-used-with-linewrap-true
	/**
	 * L'idée est que le Panel ne soit pas Scrollable Horizontalement.
	 */
	static class MyPanel extends JPanel implements Scrollable
	{
		MyPanel(LayoutManager layout) {
			super(layout);
		}
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
}
