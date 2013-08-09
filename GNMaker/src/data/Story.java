/**
 * 
 */
package data;

import java.util.ArrayList;

/**
 * A Story is made of:
 * <li>List of Perso</li>
 * <li>List of Event</li>
 * 
 * @author snowgoon88@gmail.com.
 */
public class Story {
	
	/** Name of the Story */
	String _name;
	/** Story is a list of Event */
	public ArrayList<Event> _story;
	/** Story is about a list of People */
	public ArrayList<Perso> _perso;
	
	
	
	/**
	 * Creation sans nom ni Event. 
	 */
	public Story() {
		_name = "A Story with no Name";
		_story = new ArrayList<Event>();
		_perso = new ArrayList<Perso>();
	}

	/**
	 * Ajout d'un Event à cette Story.
	 * @param evt
	 * @return result of ArrayList.add (should be true).
	 */
	public boolean add(Event evt) {
		boolean res=_story.add(evt);
		return res;
	}
	
	/** 
	 * Dump all Story as a String.
	 * @return String
	 */
	public String SDump() {
		StringBuffer str = new StringBuffer();
		str.append( "Story : "+_name+"\n");
		for (Perso p : _perso) {
			str.append( p.SDump()+"\n");
		}
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
		this._name = name;
	}
}
