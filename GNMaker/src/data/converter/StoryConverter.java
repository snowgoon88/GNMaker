/**
 * 
 */
package data.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import data.Event;
import data.Perso;
import data.Story;

/**
 * Convert a Story to my "custom" XML using xStream.
 * 
 * @author snowgoon88@gmail.com
 */
public class StoryConverter implements Converter {

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class arg0) {
		// can only convert Story.
		return arg0.equals(Story.class);
	}

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		
		Story story = (Story) obj;
		
		// Write name
		writer.startNode("name");
		writer.setValue(story.getName());
		writer.endNode();
		
		// Write the ArrayList<People>
		for (int i = 0; i < story._perso.size(); i++) {
			writer.startNode("perso");
			writer.addAttribute("id", Integer.toString(i));
			context.convertAnother(story._perso.get(i));
			writer.endNode();
		}
		
		// Write the ArrayList<Event>
		// On ne délègue pas à un éventuel EventConverter car il faut faire le lien
		// Avec les perso
		for (int i = 0; i < story._story.size(); i++) {
			Event evt = story._story.get(i);
			writer.startNode("event");
			// Write title
			writer.startNode("title");
			writer.setValue(evt._title);
			writer.endNode();
			// Write body
			writer.startNode("body");
			writer.setValue(evt._body);
			writer.endNode();
			// PersoEvent
			for (Event.PersoEvent pe : evt._perso.values()) {
				// Trouver l'id de pe._perso
				int id = story._perso.indexOf(pe._perso);
				writer.startNode("perso_event");
				writer.startNode("perso_id");
				writer.setValue(Integer.toString(id));
				writer.endNode();
				writer.startNode("status");
				writer.setValue(Boolean.toString(pe._status));
				writer.endNode();
				writer.startNode("desc");
				writer.setValue(pe._desc);
				writer.endNode();
				writer.endNode();
			}
			writer.endNode();
		}
		
	}

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		
		Story story = new Story();
		
		// _name
		reader.moveDown();
		story.setName(reader.getValue());
		reader.moveUp();
		
		// Perso
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("perso".equals(reader.getNodeName())) {
				@SuppressWarnings("unused")
				int id = Integer.parseInt(reader.getAttribute("id"));
				Perso pers = (Perso)context.convertAnother(story, Perso.class);
				story._perso.add( pers );
			}
			else {
				// event
				// title
				reader.moveDown();
				String title = reader.getValue();
				reader.moveUp();
				// body
				reader.moveDown();
				String body = reader.getValue();
				reader.moveUp();
				Event evt = new Event( story, title, body );
				// PersoEvent
				while (reader.hasMoreChildren()) {
					reader.moveDown();
					// id
					reader.moveDown();
					int id = Integer.parseInt(reader.getValue());
					reader.moveUp();
					// status
					reader.moveDown();
					boolean status = Boolean.parseBoolean(reader.getValue());
					reader.moveUp();
					// desc
					reader.moveDown();
					String desc =reader.getValue();
					reader.moveUp();
					reader.moveUp();
					
					evt.addPerso( story._perso.get(id), status, desc );
				}
				story.add(evt);
			}
			reader.moveUp();
		}
		
		// Evt
		
		
		return story;
	}

}
