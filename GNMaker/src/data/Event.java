package data;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Un événement composé de :
 * <li>String: Un Titre</li>
 * <li>??? : Une Date</li>
 * <li>String: Un Body</li>
 * <li>HashMap(Perso) : une liste de PersoxPointDeVuexBoolean (ok, todo) (</li>
 * 
 * 
 * @author snowgoon88@gmail.com
 */
public class Event extends Observable {
	/** Appartient à une Story */
	public Story _story;
	/** Titre de l'événement */
	public String _title;
	/** Corps de l'événement */
	public String _body;
	/** Date de l'événement */
	// Date _date;
	/** Liste de Perso impliqués : Le boolean indique si l'événement a été pris en compte
	 * pour le perso : ok=true, todo=false.
	 */
	public HashMap<Perso,PersoEvent> _perso;

	
	/**
	 * Creation sans Personnage
	 * @param _title Titre de l'événement
	 * @param _body Descripion de l'événement
	 */
	public Event(Story story, String _title, String _body) {
		_story = story;
		this._title = _title;
		this._body = _body;
		_perso = new HashMap<Perso,PersoEvent>();
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
		_perso.put( pers, pe);
		
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
		if (_perso.containsKey(pers)) {
			_perso.remove(pers);
			
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
		PersoEvent data = _perso.get(pers);
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
		if (_perso.containsKey(pers)) {
			return _perso.get(pers)._status;
		}
		return false;
	}
	/** 
	 * Dump all Perso as a String.
	 * @return String
	 */
	public String SDump() {
		StringBuffer str = new StringBuffer();
		str.append( "Event : "+_title+"\n");
		str.append( _body+"\n");
		for (Map.Entry<Perso, PersoEvent> e : _perso.entrySet()) {
			if (e.getValue()._status==false) {
				str.append( "-"+e.getKey().SDump()+";");
			}
			else {
				str.append( "+"+e.getKey().SDump()+";");
			}
		}
		str.append( "\n" );
		return str.toString();
	}
	
	/**
	 * Les données liant Perso à Event.
	 * <li> _status : à jour ou pas</li>
	 * <li> _desc : la description de l'évt du pt de vue du Perso.</li>
	 */
	public class PersoEvent {
		public Perso _perso;
		public boolean _status;
		public String _desc;
		/**
		 * @param _status
		 * @param _desc
		 */
		public PersoEvent(Perso perso, boolean _status, String _desc) {
			this._perso = perso;
			this._status = _status;
			this._desc = _desc;
		}
	}
}
