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
 * TODO set name
 * TODO save to file (XML ?)
 * TODO read from file (XML ?)
 * 
 * @author snowgoon88@gmail.com.
 */
public class Story {
	
	/** Name of the Story */
	String _name;
	/** Story is a list of Event */
	ArrayList<Event> _story;
	/** Story is about a list of People */
	ArrayList<Perso> _perso;
	
	
	
	/**
	 * Creation sans nom ni Event. 
	 */
	public Story() {
		_name = "A Story with no Name";
		_story = new ArrayList<Event>();
		
		
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
