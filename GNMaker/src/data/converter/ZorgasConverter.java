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

import data.Zorgas;

/**
 * Convert a Zorgas to my "custom" XML using xStream.
 * 
 * @author snowgoon88@gmail.com
 */
public class ZorgasConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class arg0) {
		// can only convert Zorgas.
		return arg0.equals(Zorgas.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		
		Zorgas zorgas = (Zorgas) obj;
		
		for (Entry<Integer, String> entry : zorgas.entrySet()) {
			writer.startNode("zorga");
			writer.addAttribute("id", Integer.toString(entry.getKey()));
			writer.setValue(entry.getValue());
			writer.endNode();
		}

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {

		Zorgas zorgas = new Zorgas();
		
		while (reader.hasMoreChildren()) {
			// id
			reader.moveDown();
			int id = Integer.parseInt(reader.getAttribute("id"));
			String orga =reader.getValue();
			reader.moveUp();
			
			zorgas.set(id, orga);
		}
               
        return zorgas;
	}

}
