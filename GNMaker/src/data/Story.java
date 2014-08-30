/**
 * 
 */
package data;

import java.util.ArrayList;
import java.util.Observable;

/**
 * A Story is made of:
 * <ul>
 * <li>List of Zorga</li>
 * <li>List of Perso</li>
 * <li>List of Event</li>
 * </ul>
 * 
 * Add Event
 * Remove Event
 * Dump All
 * 
 * Notify Observers:
 * <ul>
 * <li>Event : when added</li>
 * <li>"removed"</li>
 * <li>'' when setName</li>
 * </ul>
 * 
 * SHOULD be armonized with other Observable
 * 
 * @author snowgoon88@gmail.com.
 */
public class Story extends Observable {
	
	/** Name of the Story */
	String _name;
	/** Story is a list of Event */
	public ArrayList<Event> _story;
	/** Story is about a list of People */
	public ListOf<Perso> _persoList;
	/** Story is made by a List of Zorga */
	public ListOf<Zorga> _zorgaList;
	
	
	/** Story has been modified ? */
	boolean _fgModified;
	
	
	
	/**
	 * Creation sans nom ni Event. 
	 */
	public Story() {
		_name = "A Story with no Name";
		_story = new ArrayList<Event>();
		_zorgaList = new ListOf<Zorga>(Zorga.zorgaNull);
		_persoList = new ListOf<Perso>();
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
	public String sDump() {
		StringBuffer str = new StringBuffer();
		str.append( "Story : "+_name+"\n");
		str.append( "Zorgas : "+_zorgaList.sDump()+"\n");
		str.append( "Persos : "+ _persoList.sDump()+ "\n");
		for (Event e : _story) {
			str.append( e.sDump()+"\n");
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
	 * @param name the _name to set
	 */
	public void setName(String name) {
		
		setChanged();
		notifyObservers();
		this._name = name;
	}

}
