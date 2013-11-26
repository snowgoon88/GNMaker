/**
 * 
 */
package data.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import data.VersionText;

/**
 * Convert a VersionText to my "custom" XML using xStream.
 * 
 * @author snowgoon88@gmail.com
 */
public class VersionTextConverter implements Converter {

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class arg0) {
		// can only convert Story.
				return arg0.equals(VersionText.class);
	}

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		
		VersionText ver = (VersionText) obj;
		// With version ?
		if (ver.isSolved()) {
			// Write local without 'block'
			writer.setValue(ver.getLocal());
		}
		else {
			// Write local
			writer.startNode("version");
			writer.addAttribute("id", "local");
			writer.setValue(ver.getLocal());
			writer.endNode();
			// Write Remote
			writer.startNode("version");
			writer.addAttribute("id", "remote");
			writer.setValue(ver.getLocal());
			writer.endNode();
		}

	}

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		
		VersionText ver = new VersionText();
		
		// With versions ??
		if (reader.hasMoreChildren()) {
			// Read both versions
			// Local
			reader.moveDown();
			String id = reader.getAttribute("id");
			if (!id.equalsIgnoreCase("local")) {
				System.err.println("should read 'local' but id_read="+id);
				return null;
			}
			ver.setLocal(reader.getValue());
			reader.moveUp();
			
			// Remote
			reader.moveDown();
			id = reader.getAttribute("id");
			if (!id.equalsIgnoreCase("remote")) {
				System.err.println("should read 'remote' but id_read="+id);
				return null;
			}
			ver.setRemote(reader.getValue());
			reader.moveUp();
			
			ver.setSolved(false);
		}
		// No version
		else {
			ver.setLocal(reader.getValue());
			ver.setSolved(true);
		}
		
		return ver;
	}

}
