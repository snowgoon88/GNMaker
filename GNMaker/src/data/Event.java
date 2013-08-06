package data;

import java.util.HashMap;
import java.util.Map;

/**
 * Un événement composé de :
 * <li>String: Un Titre</li>
 * <li>??? : Une Date</li>
 * <li>HashMap(Perso,boolean) : une liste de PersoxBoolean (ok, todo) (</li>
 * 
 * @author snowgoon88@gmail.com
 */
public class Event {
	/** Titre de l'événement */
	public String _title;
	/** Corps de l'événement */
	public String _body;
	/** Date de l'événement */
	// Date _date;
	/** Liste de Perso impliqués : Le boolean indique si l'événement a été pris en compte
	 * pour le perso : ok=true, todo=false.
	 */
	public HashMap<Perso, Boolean> _perso;

	
	
	/**
	 * Creation sans Personnage
	 * @param _title Titre de l'événement
	 * @param _body Descripion de l'événement
	 */
	public Event(String _title, String _body) {
		this._title = _title;
		this._body = _body;
		_perso = new HashMap<Perso,Boolean>();
	}

	/** Add a Perso, by default with "status"=false. (todo)
	 * 
	 * @param pers Perso to add
	 */
	public void addPerso( Perso pers) {
		_perso.put(pers, false);
	}
	/** 
	 * Change 'status (todo/ok) of Perso. Add if not exists.
	 * @param pers Perso to change
	 * @param status false(todo) or true (ok).
	 */
	public void setStatusPerso( Perso pers, boolean status ) {
		_perso.put(pers,status);
	}
	/**
	 * Get the status of the Perso
	 * @param pers
	 * @return true (ok) or false (todo or no status)
	 */
	public Boolean getStatusPerso( Perso pers ) {
		if (_perso.containsKey(pers)) {
			return _perso.get(pers);
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
		for (Map.Entry<Perso, Boolean> e : _perso.entrySet()) {
			if (e.getValue()==false) {
				str.append( "-"+e.getKey().SDump()+";");
			}
			else {
				str.append( "+"+e.getKey().SDump()+";");
			}
		}
		str.append( "\n" );
		return str.toString();
	}
}
