/**
 * 
 */
package data;

import java.util.Observable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Gère un String dont on a 2 versions : locale et remote.
 * On ne peut que modifier locale.
 * 
 * Notify Observers:
 * <li>set</li>
 * 
 * @author snowgoon88@gmail.com
 */
public class VersionText extends Observable {
	
	/** version locale */
	String _local;
	/** version remote */
	String _remote;
	
	/** Conflict résolu ? */
	boolean _isSolved;
	
	/* In order to Log */
	private static Logger logger = LogManager.getLogger(VersionText.class.getName());
	
	/**
	 * Create an empty VersionText, solved.
	 */
	public VersionText() {
		setLocal("-");
		setRemote("-");
		setSolved(true);
	}
	/**
	 * Create a VersionText with only a local version.
	 * @param local The local version. 
	 */
	public VersionText(String local) {
		setLocal(local);
		setRemote("-");
		setSolved(true);
	}
	/**
	 * @return _local
	 */
	public String getLocal() {
		return _local;
	}
	/**
	 * set _local
	 */
	public void setLocal(String local) {
		this._local = local;
		setChanged();
		notifyObservers("set");
	}


	/**
	 * @return _remote
	 */
	public String getRemote() {
		return _remote;
	}
	/**
	 * set _remote
	 */
	public void setRemote(String remote) {
		this._remote = remote;
		setChanged();
		notifyObservers("set");
	}



	/**
	 * @return _isSolved
	 */
	public boolean isSolved() {
		return _isSolved;
	}
	/**
	 * @param Set solved status
	 */
	public void setSolved(boolean solved) {
		this._isSolved = solved;
	}



	/** 
	 * Dump all Versions as a String.
	 * @return String
	 */
	public String sDump() {
		StringBuffer str = new StringBuffer();
		str.append( "Version (solved="+_isSolved+")\n");
		str.append( "Remote : "+_remote+"\n");
		str.append( "Local  : "+_local+"\n");
		return str.toString();
	}
	
}
