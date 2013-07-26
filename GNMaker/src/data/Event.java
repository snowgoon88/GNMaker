package data;

import java.util.ArrayList;

/**
 * Un événement composé de :
 * <li>String: Un Titre</li>
 * <li>??? : Une Date</li>
 * <li>[Perso] : une liste de Perso</li>
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
	/** Liste de Perso impliqués */
	public ArrayList<Perso> _perso;

	
	
	/**
	 * Creation sans Personnage
	 * @param _title Titre de l'événement
	 * @param _body Descripion de l'événement
	 */
	public Event(String _title, String _body) {
		this._title = _title;
		this._body = _body;
		_perso = new ArrayList<Perso>();
	}

	/** 
	 * Dump all Perso as a String.
	 * @return String
	 */
	public String SDump() {
		StringBuffer str = new StringBuffer();
		str.append( "Event : "+_title+"\n");
		str.append( _body+"\n");
		for (Perso p : _perso) {
			str.append( p.SDump()+"; ");
		}
		str.append( "\n" );
		return str.toString();
	}
}
