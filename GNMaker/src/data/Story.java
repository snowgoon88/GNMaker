/**
 * 
 */
package data;

import java.util.ArrayList;
import java.util.Observable;

/**
 * A Story is made of:
 * <li>List of Zorga</li>
 * <li>List of Perso</li>
 * <li>List of Event</li>
 * 
 * Add Event
 * Remove Event
 * Dump All
 * 
 * @author snowgoon88@gmail.com.
 */
public class Story extends Observable {
	
	/** Name of the Story */
	String _name;
	/** Story is a list of Event */
	public ArrayList<Event> _story;
	/** Story is about a list of People */
	public PersoList _perso;
	/** Story is made by a List of Zorga */
	public Zorgas _zorgas;
	
	/** Story has been modified ? */
	boolean _fgModified;
	
	
	
	/**
	 * Creation sans nom ni Event. 
	 */
	public Story() {
		_name = "A Story with no Name";
		_story = new ArrayList<Event>();
		_zorgas = new Zorgas();
		_perso = new PersoList(_zorgas);
		_fgModified = false;
	}

	/**
	 * Ajout d'un Event à cette Story.
	 * @param evt
	 * @return result of ArrayList.add (should be true).
	 * @toObserver : new Event
	 */
	public boolean add(Event evt) {
		boolean res=_story.add(evt);
		_fgModified = true;
		
		setChanged();
		notifyObservers(evt);
		return res;
	}
	/**
	 * Enlever un Event à cette Story.
	 * @param evt
	 * @return true if really removed
	 * @toObserver : String "removed".
	 */
	public boolean remove(Event evt) {
		boolean res=_story.remove(evt);
		if (res) {
			_fgModified = true;
			setChanged();
			notifyObservers("removed");
		}
		return res;
	}
	
	/** 
	 * Dump all Story as a String.
	 * @return String
	 */
	public String SDump() {
		StringBuffer str = new StringBuffer();
		str.append( "Story : "+_name+"\n");
		str.append( "Zorgas : "+_zorgas.SDump()+"\n");
		str.append( "Persos : "+ _perso.SDump()+ "\n");
		for (Event e : _story) {
			str.append( e.SDump()+"\n");
		}
		return str.toString();
	}
	
	/**
	 * @return the _name
	 */
	public String getName() {
		return _name;
	}
	/**
	 * @param _name the _name to set
	 */
	public void setName(String name) {
		
		setChanged();
		notifyObservers();
		this._name = name;
	}
	
	/**
	 * Est-ce que cette Story (ou ses Elements) on été modifiés (besoin de save).
	 * @return recursive true or false.
	 */
	public boolean isModified() {
		boolean res = _fgModified;
		res = res || _zorgas.isModified();
		res = res || _perso.isModified();
		for (Event evt : _story) {
			res = res || evt.isModified();
		}
		return res;
	}
	/**
	 * Indique si cette Story a été modifiée.
	 * @param flag
	 */
	public void setModified( boolean flag ) {
		_fgModified = flag;
		_zorgas.setModified(flag);
		_perso.setModified(flag);
		for (Event evt : _story) {
			evt.setModified(flag);
		}
	}
}
