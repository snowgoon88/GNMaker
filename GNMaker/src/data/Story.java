/**
 * 
 */
package data;

import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;

/**
 * A Story is Basically a list of Event.
 * 
 * TODO creation
 * TODO adding event
 * TODO save to file (XML ?)
 * TODO read from file (XML ?)
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

	public boolean add(Event evt) {
		boolean res=_story.add(evt);
		return res;
	}
	
	public String toXML() {
		// XML parser and reader
		XStream _xstream = new XStream();
		// Des alias pour alléger le xml
		_xstream.alias("story", Story.class);
		_xstream.alias("event", Event.class);
		_xstream.alias("perso", Perso.class);
		
		String xml = _xstream.toXML(this);
		return xml;
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
