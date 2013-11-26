/**
 * 
 */
package data.converter;

import java.util.Map.Entry;


import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import data.Event;
import data.Perso;
import data.Story;
import data.VersionText;
import data.Zorga;

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
		
//		System.err.println(StoryConverter.class.getName()+" START");
		Story story = (Story) obj;
		
		// Write name
		writer.startNode("name");
		writer.setValue(story.getName());
		writer.endNode();
		
		// Write the Zorgas
		writer.startNode("zorga_list");
		for (Entry<Integer, Zorga> entry : story._zorgaList.entrySet()) {
			if (entry.getKey() >= 0) {
				writer.startNode("zorga");
				writer.addAttribute("id", Integer.toString(entry.getKey()));
				context.convertAnother(entry.getValue());
				writer.endNode();
			}
		}
		writer.endNode();
		
		// Write the PersoList
		writer.startNode("perso_list");
		for (Entry<Integer, Perso> entry : story._persoList.entrySet()) {
			if (entry.getKey() >= 0) {
				writer.startNode("perso");
				writer.addAttribute("id", Integer.toString(entry.getKey()));
				context.convertAnother(entry.getValue());
				writer.endNode();
			}
		}
		writer.endNode();
		
		// Write the ArrayList<Event>
		// On ne délègue pas à un éventuel EventConverter car il faut faire le lien
		// Avec les perso
		for (int i = 0; i < story._story.size(); i++) {
			Event evt = story._story.get(i);
			writer.startNode("event");
			// Write title
			writer.startNode("title");
			writer.setValue(evt.getTitle());
			writer.endNode();
			// Write body
			writer.startNode("body");
			writer.setValue(evt.getBody());
			writer.endNode();
			// PersoEvent
			for (Entry<Integer, Event.PersoEvent> entry : evt._listPE.entrySet()) {
				if (entry.getKey() >= 0) {
					writer.startNode("perso_event");
					writer.addAttribute("id", Integer.toString(entry.getKey()));
					writer.startNode("status");
					writer.setValue(Boolean.toString(entry.getValue().getStatus()));
					writer.endNode();
					writer.startNode("desc");
//					writer.setValue(entry.getValue().getDesc());
					context.convertAnother(entry.getValue().getDesc());
					writer.endNode();
					writer.endNode();
				}
			}
//			// PersoEvent
//			for (Event.PersoEvent pe : evt._persoMap.values()) {
//				// Trouver l'id de pe._perso
//				int id = pe._perso.getId();
//				writer.startNode("perso_event");
//				writer.startNode("perso_id");
//				writer.setValue(Integer.toString(id));
//				writer.endNode();
//				writer.startNode("status");
//				writer.setValue(Boolean.toString(pe._status));
//				writer.endNode();
//				writer.startNode("desc");
//				writer.setValue(pe.getDesc());
//				writer.endNode();
//				writer.endNode();
//			}
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
		
		// ListZorga
		reader.moveDown();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			int id = Integer.parseInt(reader.getAttribute("id"));
			Zorga zorga = (Zorga)context.convertAnother(story, Zorga.class);
			story._zorgaList.put(id, zorga);
			reader.moveUp();
		}
		reader.moveUp();
		
		// PersoList
		context.put("story", story);
		reader.moveDown();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			int id = Integer.parseInt(reader.getAttribute("id"));
//			System.out.println("StoryConverter.unmarshal() : "+reader.getNodeName()+" id="+id);
			Perso pers = (Perso)context.convertAnother(story, Perso.class);
			story._persoList.put(id, pers);
			reader.moveUp();
		}
		reader.moveUp();
		
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("perso".equals(reader.getNodeName())) {
				System.err.println("reader=>perso : should not be here");
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
					int id = Integer.parseInt(reader.getAttribute("id"));
//					// id
//					reader.moveDown();
//					int id = Integer.parseInt(reader.getValue());
//					reader.moveUp();
					// status
					reader.moveDown();
					boolean status = Boolean.parseBoolean(reader.getValue());
					reader.moveUp();
					// desc
					reader.moveDown();
//					String desc =reader.getValue();
					VersionText desc = (VersionText) context.convertAnother(story, VersionText.class);
					reader.moveUp();
					reader.moveUp();
					
					evt.addPerso( story._persoList.get(id), status, desc );
				}
				story.add(evt);
			}
			reader.moveUp();
		}
		
		
		return story;
	}

}
