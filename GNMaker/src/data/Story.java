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
 * TODO Revoir le syst. de notification (Observer directelemt _story par exemple)
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
	//DEL public ArrayList<Event> _story;
	public ListOf<Event> _story;
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
		//DEL _story = new ArrayList<Event>();
		_story = new ListOf<Event>();
		_zorgaList = new ListOf<Zorga>(Zorga.zorgaNull);
		_persoList = new ListOf<Perso>();
		_fgModified = false;
	}

	/**
	 * Ajout d'un Event à cette Story.
	 * @param evt
	 * @return true if succes
	 * @toObserver : new Event
	 */
	public boolean add(Event evt) {
		int res=_story.add(evt);
		_fgModified = true;
		
		setChanged();
		notifyObservers(evt);
		return res >= 0;
	}
	/**
	 * Enlever un Event à cette Story.
	 * @param evt
	 * @return true if really removed
	 * @toObserver : String "removed".
	 */
	public boolean remove(Event evt) {
		Event res=_story.remove(evt.getId());
		if (res != null) {
			_fgModified = true;
			setChanged();
			notifyObservers("removed");
		}
		return res != null;
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
		str.append( "Story : " + _story.sDump() + "\n");
//		for (Event e : _story) {
//			str.append( e.sDump()+"\n");
//		}
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
