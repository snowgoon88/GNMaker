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

/**
 * Convert a Perso to my "custom" XML using xStream.
 * 
 * @author snowgoon88@gmail.com
 */
public class PersoConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class arg0) {
		// can only convert Perso.
		return arg0.equals(Perso.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		
		Perso perso = (Perso) obj;
		
		// _name
		writer.startNode("name");
		writer.setValue(perso._name);
		writer.endNode();
		
		// _player
		writer.startNode("player");
		writer.setValue(perso._player);
		writer.endNode();
		
		// _zorga
		writer.startNode("zorga");
		writer.setValue(perso._zorga);
		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		
		Perso perso = new Perso("","","");
        
		// _name
		reader.moveDown();
        perso._name = reader.getValue();
        reader.moveUp();
        
        // _player
        reader.moveDown();
        perso._player = reader.getValue();
        reader.moveUp();
        
        // _zorga
        reader.moveDown();
        perso._zorga = reader.getValue();
        reader.moveUp();
        
        return perso;
	}

}
