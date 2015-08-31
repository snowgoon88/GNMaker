/**
 * 
 */
package data.converter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import gui.DocHighlighter;
import data.MyStyledDocument;

/**
 * Convert a Document to my "custom" XML using xStream.
 * 
 * @author snowgoon88@gmail.com
 */
public class DocumentConverter implements Converter {
	
	/* In order to Log */
	private static Logger logger = LogManager.getLogger(DocumentConverter.class.getName());

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class arg0) {
		// can only convert DefaultStyledDocument.
		return arg0.equals(MyStyledDocument.class);
	}

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		
		MyStyledDocument doc = (MyStyledDocument) obj;
		
		// Start root
		Element root = doc.getDefaultRootElement();
		logger.trace("sNODE root : "+root.getName());
		writer.startNode( root.getName() );
		
		// Chacun des Child
		for (int i = 0; i < root.getElementCount(); i++) {
			writeElement( root.getElement(i), writer, context, doc);
		}
		
		// End root
		logger.trace("eNODE root : "+root.getName());
		writer.endNode();
	}
	protected void writeElement( Element elem, 
			HierarchicalStreamWriter writer,
			MarshallingContext context,
			DefaultStyledDocument doc) {
		// Si Leaf (=> contenu), on regarde si bold et/ou italic
		if( elem.isLeaf() ) {
			AttributeSet attr =  elem.getAttributes();

			// Les différents styles de character
			logger.trace("sNODE content");
			writer.startNode( "content");
			if( StyleConstants.isBold(attr)) {
				logger.trace("ATTR bold");
	    		writer.addAttribute("bold", "true");
			}
			if( StyleConstants.isItalic(attr)) {
				logger.trace("ATTR italic");
	    		writer.addAttribute("italic", "true");
			}
			String colorStr = DocHighlighter.getHighlight(attr);
			if( colorStr != null ) {
				logger.trace("ATTR high");
				writer.addAttribute("highlight", colorStr);
			}

			try {
				logger.trace(">>value");
				writer.setValue( elem.getDocument().getText(elem.getStartOffset(), 
						elem.getEndOffset()-elem.getStartOffset()));
				System.out.println("LU ["+elem.getDocument().getText(elem.getStartOffset(), 
						elem.getEndOffset()-elem.getStartOffset())+"]");
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			logger.trace("eNODE content");
			writer.endNode();
		}
		else {
			logger.trace("sNODE : "+elem.getName());
			writer.startNode( elem.getName() );
			// Si paragraphe, on note le style
			if( elem.getName().equalsIgnoreCase("paragraph") ) {
				// style
				logger.trace("ATTR style : "+doc.getLogicalStyle( elem.getStartOffset() ).getName());
				writer.addAttribute("style",
						doc.getLogicalStyle( elem.getStartOffset() ).getName());
			}
			for (int i = 0; i < elem.getElementCount(); i++) {
				writeElement( elem.getElement(i), writer, context, doc);
			}
			logger.trace("eNODE : "+elem.getName());
			writer.endNode();
		}
	}

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		
		MyStyledDocument doc = new MyStyledDocument();
		
		// section
		System.out.println("DocumentConverter.unmarshal() : "+reader.getNodeName());
		reader.moveDown();
		System.out.println("DocumentConverter.unmarshal() : "+reader.getNodeName());
		
		// <paragraph>
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			System.out.println("DocumentConverter.unmarshal() : "+reader.getNodeName());
			
			// Style du paragraphe
			String styleStr = reader.getAttribute( "style" );
			doc.setLogicalStyle(doc.getLength(), doc.getStyle(styleStr));
			
			// <content>
			while (reader.hasMoreChildren()) {
				reader.moveDown();
				System.out.println("DocumentConverter.unmarshal() : "+reader.getNodeName());
				//String face = 
				//System.out.println( "bold="+face);
				String text = reader.getValue();
				AttributeSet attrElem = doc.getCharacterElement(doc.getLength()).getAttributes();
				MutableAttributeSet attr = new SimpleAttributeSet(attrElem);
				if( reader.getAttribute( "bold" ) != null ) 
					StyleConstants.setBold(attr, true);
				if( reader.getAttribute( "italic" ) != null ) 
					StyleConstants.setItalic(attr, true);
				String colorStr = reader.getAttribute( "highlight" );
				if( colorStr != null ) {
					DocHighlighter.setHighlight(attr, colorStr);
				}
				try {
					doc.insertString(doc.getLength(), text, attr );
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				
				reader.moveUp();
			}
			
			reader.moveUp();
		}
		reader.moveUp();
		
		return doc;
	}

}
