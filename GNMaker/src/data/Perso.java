/**
 * 
 */
package data;

import java.util.Observable;
import java.util.Observer;

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
 * <li>del</li>
 * 
 * @author nowgoon88@gmail.com
 */
public class Perso extends Observable implements Observer, IElement {
	
	static public Perso persoNull = new Perso("---", "---", Zorga.zorgaNull);
	
	/** Id du Perso */
	int _id = -1;
	/** Nom du Perso */
	String _name;
	/** Nom du Joueur */
	String _player;
	/** Zorga */
	Zorga _zorga;
	
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
	public Perso(String name, String player, Zorga zorga) {
		this._name = name;
		this._player = player;
		this._zorga = zorga;
		
		// Listen to Zorgas
		_zorga.addObserver(this);
	}
	
	/** 
	 * Dump all Perso as a String.
	 * @return String
	 */
	public String sDump() {
		StringBuffer str = new StringBuffer();
		str.append( "Perso : "+_name);
		str.append( " ("+_player+" - "+_zorga.getName()+")");
		return str.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append( _name);
		str.append( " ("+_player+" - "+_zorga.getName()+")");
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
	public Zorga getZorga() {
		return _zorga;
	}
	public void setZorga(Zorga zorga) {
		_zorga = zorga;
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
		logger.debug(getName()+" "+_zorga.getName()+" o is a "+o.getClass().getName()+ " arg="+arg);
	
		// Observe un Zorga
		if (arg.equals("del")) {
			// Zorga devient zorgaNull
			setZorga(Zorga.zorgaNull);
		}
	}
	
	@Override
	public int getId() {
		return _id;
	}

	@Override
	public void setId(int id) {
		_id = id;
	}

	@Override
	public void elementRemoved() {
		logger.debug(getName()+" del");
		setChanged();
		notifyObservers("del");
		
	}
}