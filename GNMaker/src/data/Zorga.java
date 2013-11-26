/**
 * 
 */
package data;

import java.util.Observable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Un Zorga a un nom (_name)
 * 
 * Notify Observers:
 * <li>set</li>
 * <li>del</li>
 * 
 * @author snowgoon88@gmail.com
 */
public class Zorga extends Observable implements IElement {
	
	static public Zorga zorgaNull = new Zorga("---");
	
	
	/** Nom */
	String _name;
	/** Id */
	int _id = -1;
	/** has been modified ? */
	boolean _fgModified;
	/** status */
	State _status;
	
	/* In order to Log */
	private static Logger logger = LogManager.getLogger(Zorga.class.getName());
	
	/**
	 * Création d'un Zorga avec nom.
	 */
	public Zorga(String name) {
		_name = name;
		_fgModified = false;
		_status = State.BASIC;
	}

	/**
	 * @return _name
	 */
	public String getName() {
		return _name;
	}
	/**
	 * @param name change le Nom du Zorga.
	 */
	public void setName(String name) {
		this._name = name;
		
		logger.debug(getName()+" set");
		setChanged();
		notifyObservers("set");
	}
	
	/** 
	 * Dump Zorga as a getName.
	 * @return String
	 */
	public String sDump() {
		return getName()+"("+getId()+", "+getStatus()+")";
	}
	


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
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

	@Override
	public void updateWith(IElement other) {
		Zorga z = (Zorga) other;
		// Update si 'name' est différent.
		if ( !z.getName().equals(getName()) ) {
			setName(z.getName());
			setStatus(State.UPDATED);
		}
	}
	
	@Override
	public State getStatus() {
		return _status;
	}
	@Override
	public void setStatus(State status) {
		_status = status;
	}
	

}
