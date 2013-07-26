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
	/** Date de l'événement */
	// Date _date;
	/** Liste de Perso impliqués */
	ArrayList<Perso> _perso;

}
