/**
 * 
 */
package data;

import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Un Perso c'est:
 * <li>un nom : _name</li>
 * <li>un joueur : _player </li>
 * <li>un zorga : Zorgas+id => Observer de Zorgas</li>
 * 
 * Notify Observers:
 * <li>set</li>
 * 
 * @author nowgoon88@gmail.com
 */
public class Perso extends Observable implements Observer {
	
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

	/* In order to Log */
	private static Logger logger = LogManager.getLogger(Perso.class.getName());
	
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
		logger.trace(getName()+" _zorgaId="+_zorgaId);
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
		logger.trace(getName()+" _zorgaId="+_zorgaId);
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
	 * @toObserver : "set"
	 */
	public void setName(String name) {
		this._name = name;
		setChanged();
		notifyObservers("set");
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
	 * @toObserver : "set"
	 */
	public void setPlayer(String player) {
		this._player = player;
		setChanged();
		notifyObservers("set");
		_fgModified = true;
	}
	public int getZorgaId() {
		return _zorgaId;
	}
	public void setZorga(String zorgName ) {
		_zorgaId = _zorgaList.indexOf(zorgName);
		logger.trace(getName()+" _zorgaId="+_zorgaId);
		updateZorga();
	}
	public void setZorgaList( Zorgas zorgaList ) {
		this._zorgaList = zorgaList;
		updateZorga();
		// Listen to Zorgas
		_zorgaList.addObserver(this);
	}
	/**
	 * @return the _zorga
	 */
	public String getZorga() {
		return _zorga;
	}
	/**
	 * @param zorga the _zorga to set
	 * @toObserver : "set"
	 */
	void updateZorga() {
		//System.out.println("Perso.updateZorga() : _zorgaId="+_zorgaId);
		if (_zorgaId >= 0) {
			//System.out.println("Perso.updateZorga() : "+_zorgaList.get(_zorgaId));
			_zorga = _zorgaList.get(_zorgaId);
		}
		else {
			_zorga = "---";
		}
		setChanged();
		notifyObservers("set");
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
		// Log
		logger.debug(getName()+" _zorgaId="+_zorgaId+" o is a "+o.getClass().getName()+ " arg="+arg);
		
		// Observe seulement un Zorgas => arg is String
		StringTokenizer sTok = new StringTokenizer((String)arg, "_");
		int id = Integer.parseInt(sTok.nextToken());
		// Intéressant si c'est _zorgaId
		if (id == _zorgaId) {
			String command = sTok.nextToken();
			switch (command) {
			case "set":
				logger.debug("SET with id="+id);
				updateZorga();
				break;
			case "del":
				logger.debug("DEL with id="+id);
				_zorgaId = -1;
				updateZorga();
				break;
			default:
				logger.debug(command+" with id="+id);
				break;
			}
		}
	}
}