/**
 * 
 */
package data;

/**
 * @author nowgoon88@gmail.com
 */
public class Perso {
	
	/** Nom du Perso */
	String _name;
	/** Nom du Joueur */
	String _player;
	/** Nom de l'orga */
	String _zorga;
	
	/** Perso has been modified ? */
	boolean _fgModified;

	
	/**
	 * Creation
	 * @param _name Nom du Personnage
	 * @param _player Nom du Joueur
	 * @param _zorga Nom du Zorga
	 */
	public Perso(String _name, String _player, String _zorga) {
		this._name = _name;
		this._player = _player;
		this._zorga = _zorga;
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
	/**
	 * @return the _zorga
	 */
	public String getZorga() {
		return _zorga;
	}
	/**
	 * @param zorga the _zorga to set
	 */
	public void setZorga(String zorga) {
		this._zorga = zorga;
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
}
