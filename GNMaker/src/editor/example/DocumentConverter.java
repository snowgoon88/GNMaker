/**
 * 
 */
package editor.example;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


/**
 * Convert a Document to my "custom" XML using xStream.
 * 
 * @author snowgoon88@gmail.com
 */
public class DocumentConverter implements Converter {

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class arg0) {
		// can only convert DefaultStyledDocument.
		return arg0.equals(DefaultStyledDocument.class);
	}

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		
		DefaultStyledDocument doc = (DefaultStyledDocument) obj;
		
		// Start root
		Element root = doc.getDefaultRootElement();
		writer.startNode( root.getName() );
		
		// Chacun des Child
		for (int i = 0; i < root.getElementCount(); i++) {
			writeElement( root.getElement(i), writer, context);
		}
		
		// End root
		writer.endNode();
	}
	protected void writeElement( Element elem, 
			HierarchicalStreamWriter writer,
			MarshallingContext context) {
		// Si Leaf, on regarde si bold et/ou italic
		if( elem.isLeaf() ) {
			AttributeSet attr =  elem.getAttributes();
			String charStyle = "";
	    	if( StyleConstants.isBold(attr)) {
				charStyle += "bold_";
			}
			if( StyleConstants.isItalic(attr) ) {
				charStyle += "italic_";
			}
			writer.startNode( "content");
			writer.addAttribute("charStyle", charStyle);
			try {
				writer.setValue( elem.getDocument().getText(elem.getStartOffset(), 
						elem.getEndOffset()-elem.getStartOffset()));
				System.out.println("LU ["+elem.getDocument().getText(elem.getStartOffset(), 
						elem.getEndOffset()-elem.getStartOffset())+"]");
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			writer.endNode();
		}
		else {
			writer.startNode( elem.getName() );
			for (int i = 0; i < elem.getElementCount(); i++) {
				writeElement( elem.getElement(i), writer, context);
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
		
		DefaultStyledDocument doc = new DefaultStyledDocument();
		
		// Prépare quelques effet de police de caractère
		AttributeSet attrs = doc.getCharacterElement(0).getAttributes();
		
		SimpleAttributeSet baseFace = new SimpleAttributeSet(attrs);
		StyleConstants.setFontFamily( baseFace, "SansSerif");
		StyleConstants.setFontSize( baseFace, 16);
		
		SimpleAttributeSet boldFace = new SimpleAttributeSet(baseFace);
		StyleConstants.setBold(boldFace, true);
		SimpleAttributeSet italicFace = new SimpleAttributeSet( baseFace );
		StyleConstants.setItalic(italicFace, true);
		SimpleAttributeSet boldItalicFace = new SimpleAttributeSet( baseFace );
		StyleConstants.setBold(boldItalicFace, true);
		StyleConstants.setItalic(boldItalicFace, true);
		
		// section
		System.out.println("DocumentConverter.unmarshal() : "+reader.getNodeName());
		reader.moveDown();
		System.out.println("DocumentConverter.unmarshal() : "+reader.getNodeName());
		
		// <paragraph>
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			System.out.println("DocumentConverter.unmarshal() : "+reader.getNodeName());
			
			// <content>
			while (reader.hasMoreChildren()) {
				reader.moveDown();
				System.out.println("DocumentConverter.unmarshal() : "+reader.getNodeName());
				String face = reader.getAttribute( "charStyle" );
			
				String text = reader.getValue();
				try {
					if (face.compareTo("") == 0) {
						doc.insertString(doc.getLength(), text, baseFace );
					}
					else if ( face.compareTo("italic_") == 0) {
						doc.insertString(doc.getLength(), text, italicFace);
					}
					else if (face.compareTo("bold_") == 0 ) {
						doc.insertString(doc.getLength(), text, boldFace);
					}
					else if (face.compareTo("bold_italic_") == 0) {
						doc.insertString(doc.getLength(), text, boldItalicFace);
					}
					else {
						System.out.println("content="+text);
						System.out.println("face="+face);
						doc.insertString(doc.getLength(), "NOT_RECOGNISED", null);
					}
				} catch (BadLocationException e) {
					System.err.println("Couldn't insert loaded text.");
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
