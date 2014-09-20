/**
 * 
 */
package gui;



import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.miginfocom.swing.MigLayout;

import data.Event;
import data.ListOf;


/**
 * C'est un MigPanel qui contient des EventV.
 * 
 * Composé de :
 * <ul>
 * this : MigPanel avec liste de EventV.
 * </ul>
 * 
 * Traite les messages suivants :
 * <ul>
 * <li> ListOf<Event>.id_add : ajoute un nouveau Event.</li>
 * <li> ListOf<Event>.id_del : remove l'Event en question.</li>
 * </ul>
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class EventListV extends MigPanel implements Observer {
	/** Un Story comme Model */
	ListOf<Event> _eventList;
	
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
		
		MigLayout eventLayout = new MigLayout(
				"flowy, hidemode 3", // Layout Constraints
				"[grow,fill]", // Column constraints
				""); // Row constraints);
		this.setLayout( eventLayout );
		
		// Liste des Event
		for (Entry<Integer, Event> entry : _eventList.entrySet()) {
			if (entry.getKey() >= 0) {
				EventV evtV = new EventV(entry.getValue());
				this.add( evtV );
			}
		}
	}


	@Override
	public void update(Observable o, Object arg) {
		// Log
		logger.debug("o is a "+o.getClass().getName()+ " arg="+arg);

		if( o instanceof ListOf<?> ) {
			// Observe ListOf<Event>
			if (arg != null) {
				if (arg instanceof String) {
					StringTokenizer sTok = new StringTokenizer((String)arg, "_");
					int id = Integer.parseInt(sTok.nextToken());
					String command = sTok.nextToken();
					// "add" -> une nouvelle ligne dans _eventPanel.
					if (command.equals("add")) {
						EventV evtV = new EventV( _eventList.get(id));
						this.add( evtV );
						this.revalidate();
						this.repaint();
					}
					// "del" efface le composant incriminé
					else if (command.equals("del")){
						for (int i = 0; i < this.getComponentCount(); i++) {
							EventV eventPanel = (EventV) this.getComponent(i);
							if (eventPanel._evt.equals(_eventList.get(id))) {
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
	}
}
