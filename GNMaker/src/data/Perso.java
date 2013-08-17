/**
 * 
 */
package data;

import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

/**
 * Un Perso c'est:
 * <li>un nom : _name</li>
 * <li>un joueur : _player </li>
 * <li>un zorga : Zorgas+id => Observer de Zorgas</li>
 * 
 * @author nowgoon88@gmail.com
 */
public class Perso implements Observer {
	
	/** Nom du Perso */
	String _name;
	/** Nom du Joueur */
	String _player;
	/** Nom de l'orga */
	String _zorga;
	/** Pour connaître le Nom du Zorga */
	Zorgas _zorgaList;
	int _zorgaId;
	
	/** Perso has been modified ? */
	boolean _fgModified;

	
	/**
	 * Creation
	 * @param name Nom du Personnage
	 * @param player Nom du Joueur
	 * @param zorgaList Liste de Zorga
	 * @param id du Zorga dans la Liste
	 */
	public Perso(String name, String player, Zorgas zorgaList, int id) {
		this._name = name;
		this._player = player;
		this._zorgaList = zorgaList;
		this._zorgaId = id;
		updateZorga();
		
		// Listen to Zorgas
		_zorgaList.addObserver(this);
	}
	/**
	 * Creation sans ZorgasList : pas d'update ou d'attachement à un Observable.
	 * @param _name Nom du Personnage
	 * @param _player Nom du Joueur
	 * @param _zorgaId Id du Zorga
	 */
	public Perso(String name, String player, int zorgaId) {
		this._name = name;
		this._player = player;
		this._zorgaId = zorgaId;
	}
	

	/** 
	 * Dump all Perso as a String.
	 * @return String
	 */
	public String SDump() {
		StringBuffer str = new StringBuffer();
		str.append( "Perso : "+_name);
		str.append( " ("+_player+" - "+_zorga+")");
		return str.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append( _name);
		str.append( " ("+_player+" - "+_zorga+")");
		return str.toString();
	}
	
	/**
	 * @return the _name
	 */
	public String getName() {
		return _name;
	}
	/**
	 * @param name the _name to set
	 */
	public void setName(String name) {
		this._name = name;
		_fgModified = true;
	}
	/**
	 * @return the _player
	 */
	public String getPlayer() {
		return _player;
	}
	/**
	 * @param player the _player to set
	 */
	public void setPlayer(String player) {
		this._player = player;
		_fgModified = true;
	}
	public int getZorgaId() {
		return _zorgaId;
	}
	public void setZorgaList( Zorgas zorgaList ) {
		this._zorgaList = zorgaList;
		updateZorga();
	}
	/**
	 * @return the _zorga
	 */
	public String getZorga() {
		return _zorga;
	}
	/**
	 * @param zorga the _zorga to set
	 */
	void updateZorga() {
		if (_zorgaId >= 0) {
			_zorga = _zorgaList.get(_zorgaId);
		}
		else {
			_zorga = "---";
		}
		_fgModified = true;
	}


	/**
	 * Est-ce que ce Perso a été modifié?
	 * @return recursive true or false.
	 */
	public boolean isModified() {
		boolean res = _fgModified;
		return res;
	}
	/**
	 * Indique si ce Perso a été modifiée.
	 * @param flag
	 */
	public void setModified( boolean flag ) {
		_fgModified = flag;
	}

	/**
	 * Observe Zorgas.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (arg != null) {
			if (arg instanceof String) {
				StringTokenizer sTok = new StringTokenizer((String)arg, "_");
				int id = Integer.parseInt(sTok.nextToken());
				// Intéressant si c'est _zorgaId
				if (id == _zorgaId) {
					String command = sTok.nextToken();
					switch (command) {
					case "set":
						updateZorga();
						break;
					case "del":
						_zorgaId = -1;
						updateZorga();
						break;
					default:
						break;
					}
				}
			}

		}
	}
}