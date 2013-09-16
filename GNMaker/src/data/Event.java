package data;


import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;


/**
 * Un événement composé de :
 * <li>String: Un Titre</li>
 * <li>??? : Une Date</li>
 * <li>String: Un Body</li>
 * <li>HashMap(Perso) : une liste de PersoxPointDeVuexBoolean (ok, todo) (</li>
 * 
 * Notify Observers:
 * <li>Perso : when added</li>
 * <li>"removed"</li>
 * <li>'' when setStatusChanged</li>
 * SHOULD armonize with other Observable
 * 
 * @author snowgoon88@gmail.com
 */
public class Event extends Observable implements Observer {
	/** Appartient à une Story */
	public Story _story;
	/** Titre de l'événement */
	String _title;
	/** Corps de l'événement */
	String _body;
	/** Date de l'événement */
	// Date _date;
	/** Liste de Perso impliqués : Le boolean indique si l'événement a été pris en compte
	 * pour le perso : ok=true, todo=false.
	 */
	public HashMap<Perso,PersoEvent> _persoMap;
	
	/**
	 * Creation sans Personnage
	 * @param _title Titre de l'événement
	 * @param _body Descripion de l'événement
	 */
	public Event(Story story, String _title, String _body) {
		_story = story;
		this._title = _title;
		this._body = _body;
		_persoMap = new HashMap<Perso,PersoEvent>();
		
		_story._persoList.addObserver(this);
	}

	/** 
	 * Add a Perso, by default with "status"=false.
	 * 
	 * @param pers Perso to add
	 * @toObserver : new PersoEvent.
	 */
	public void addPerso( Perso pers) {
		addPerso( pers, false, "-");
	}
	public void addPerso( Perso pers, boolean status, String desc) {
		PersoEvent pe = new PersoEvent( pers, status, desc);
		_persoMap.put( pers, pe);
		
		// Notify Observers
		setChanged();
		notifyObservers(pe);
	}
	/**
	 * Remove a Perso.
	 * 
	 * @param pers to remove
	 * @toObserver : "removed"
	 */
	public void removePerso( Perso pers ) {
		if (_persoMap.containsKey(pers)) {
			_persoMap.remove(pers);
			
			// Notify Observers
			setChanged();
			notifyObservers("removed");
		}
	}
	/** 
	 * Change 'status (todo/ok) of Perso. Add if not exists.
	 * @param pers Perso to change
	 * @param status false(todo) or true (ok).
	 */
	public void setStatusPerso( Perso pers, boolean status ) {
		PersoEvent data = _persoMap.get(pers);
		data._status = status;
		
		// Notify Observers
		setChanged();
		notifyObservers();
	}
	/**
	 * Get the status of the Perso
	 * @param pers
	 * @return true (ok) or false (todo or no status)
	 */
	public Boolean getStatusPerso( Perso pers ) {
		if (_persoMap.containsKey(pers)) {
			return _persoMap.get(pers)._status;
		}
		return false;
	}
	
	
	/**
	 * @return the _title
	 */
	public String getTitle() {
		return _title;
	}
	/**
	 * @param title the _title to set
	 */
	public void setTitle(String title) {
		this._title = title;
	}

	/**
	 * @return the _body
	 */
	public String getBody() {
		return _body;
	}
	/**
	 * @param body the _body to set
	 */
	public void setBody(String body) {
		this._body = body;
	}

	/** 
	 * Dump all Perso as a String.
	 * @return String
	 */
	public String sDump() {
		StringBuffer str = new StringBuffer();
		str.append( "Event : "+_title+"\n");
		str.append( _body);
		for (Map.Entry<Perso, PersoEvent> e : _persoMap.entrySet()) {
			if (e.getValue()._status==false) {
				str.append( "\n-"+e.getKey().sDump()+" => ");
			}
			else {
				str.append( "\n+"+e.getKey().sDump()+" => ");
			}
			str.append(e.getValue()._desc);
		}
		str.append( "\n" );
		return str.toString();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// Observe seulement un ListOf<Perso>
		// o est un ListOf<Perso>
		// arg est un string
		StringTokenizer sTok = new StringTokenizer((String)arg, "_");
		int id = Integer.parseInt(sTok.nextToken());
		String command = sTok.nextToken();

		// "add" -> une nouvelle ligne dans _listPanel.
		if (command.equals("del")) {
			removePerso(_story._persoList.get(id));
		}

	}
	
	/**
	 * Les données liant Perso à Event.
	 * <li> _status : à jour ou pas</li>
	 * <li> _desc : la description de l'évt du pt de vue du Perso.</li>
	 */
	public class PersoEvent {
		public Perso _perso;
		public boolean _status;
		String _desc;
		/**
		 * @param _status
		 * @param _desc
		 */
		public PersoEvent(Perso perso, boolean _status, String _desc) {
			this._perso = perso;
			this._status = _status;
			this._desc = _desc;
		}
		/**
		 * @return the _desc
		 */
		public String getDesc() {
			return _desc;
		}
		/**
		 * @param desc the _desc to set
		 */
		public void setDesc(String desc) {
			this._desc = desc;
		}
	}

	
}
