/**
 * 
 */
package data.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import data.Zorga;

/**
 * Convert a Perso to my "custom" XML using xStream.
 * 
 * @author snowgoon88@gmail.com
 */
public class ZorgaConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class arg0) {
		// can only convert Zorga.
		return arg0.equals(Zorga.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		
		Zorga zorga = (Zorga) obj;
		
		// _name
		writer.startNode("name");
		writer.setValue(zorga.getName());
		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		
		// _name
		reader.moveDown();
        String name = reader.getValue();
        reader.moveUp();
        
        
        return new Zorga(name);
	}

}
