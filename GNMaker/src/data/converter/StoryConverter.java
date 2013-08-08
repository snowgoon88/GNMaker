/**
 * 
 */
package data.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

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
		
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			@SuppressWarnings("unused")
			int id = Integer.parseInt(reader.getAttribute("id"));
			Perso pers = (Perso)context.convertAnother(story, Perso.class);
			story._perso.add( pers );
			reader.moveUp();
		}
		
		return story;
	}

}
