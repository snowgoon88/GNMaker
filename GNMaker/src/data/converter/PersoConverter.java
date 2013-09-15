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
//		System.out.println("PersoConverter.marshal() perso="+perso.sDump());
//		System.out.println("PersoConverter.marshal() zorgaId="+perso.getZorga().getId());
//		
		// _name
		writer.startNode("name");
		writer.setValue(perso.getName());
		writer.endNode();
		
		// _player
		writer.startNode("player");
		writer.setValue(perso.getPlayer());
		writer.endNode();
		
		// _zorga
		writer.startNode("zorga");
		writer.setValue(Integer.toString(perso.getZorga().getId()));
		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		
		Story story = (Story) context.get("story");
//        System.out.println("PersoConverter.unmarshal() : "+story);
        
		// _name
		reader.moveDown();
        String name = reader.getValue();
        reader.moveUp();
        
        // _player
        reader.moveDown();
        String player = reader.getValue();
        reader.moveUp();
        
        // _zorga
        reader.moveDown();
        int zorgaId = Integer.parseInt(reader.getValue());
        reader.moveUp();
//        System.out.println("PersoConverter.unmarshal() "+name+" idZorga="+zorgaId);
        return new Perso(name, player, story._zorgaList.get(zorgaId));
	}

}
