/**
 * 
 */
package data;

/**
 * @author nowgoon88@gmail.com
 */
public class Perso {
	
	/** Nom du Perso */
	public String _name;
	/** Nom du Joueur */
	public String _player;
	/** Nom de l'orga */
	public String _zorga;
	

	
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
}
