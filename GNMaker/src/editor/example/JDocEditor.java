/**
 * 
 */
package editor.example;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AbstractDocument.AbstractElement;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Essai d'implémentation d'un Editeur WYSIWYG.
 * Inspiration de http://da2i.univ-lille1.fr/doc/tutorial-java/uiswing/components/generaltext.html
 * 
 * Le Caret du JTextPane est responsable du comportement de sélection.
 * 
 * Boutons : Bold, Italique, 
 *           Dump :, 
 *           Elements :, 
 *           XML : DocumentConverter pour faire de l'XML
 * Ligne de notificatioun (JLabel) : écoute le Carret, position et sélection
 *           par le biais de CaretListenerLabel.      
 * 
 * @todo : sauver et relire un document en XML ??
 *  =PB=>  écrit en HTML les accents et les cédilles.
 *  ====>  vérifier dans le master normal
 * @todo : regarder quel classe de Document, et comment le Modèle (en arbre) évolue quand on ajoute des
 * choses...
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class JDocEditor extends JPanel {
	
	JTextPane _textPane;
	StyledDocument _styledDoc;
	// Toutes les actions définie dans le EditorKit
	HashMap<Object, Action> actions;
	
	String newline = "\n";
	
	/**
	 * 
	 */
	public JDocEditor() {
		super();
		this.setLayout( new BorderLayout());
		
		buildGUI();
		initDocument();
	}

	void buildGUI() {
		// Text Panel et son Doc
		_textPane = new JTextPane();
		createActionTable(_textPane);
		
        _textPane.setCaretPosition(0);
        _textPane.setMargin(new Insets(5,5,5,5));
        _styledDoc = _textPane.getStyledDocument();
        System.out.println("_styleDoc is a "+_styledDoc.getClass().getCanonicalName());
//        if (styledDoc instanceof AbstractDocument) {
//            doc = (AbstractDocument)styledDoc;
//            doc.setDocumentFilter(new DocumentSizeFilter(MAX_CHARACTERS));
//        } else {
//            System.err.println("Text pane's document isn't an AbstractDocument!");
//            System.exit(-1);
//        }
        
        // Dans une fenêtre avec Scroll
        JScrollPane scrollPane = new JScrollPane(_textPane);
        scrollPane.setPreferredSize(new Dimension(200, 200));
		
        //Create the status area.
        JPanel statusPane = new JPanel(new GridLayout(1, 1));
        CaretListenerLabel caretListenerLabel =
                new CaretListenerLabel("Caret Status");
        statusPane.add(caretListenerLabel);
        
        
        // Frame pour tous les Boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout( new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        // JButton pour Dumper en utilisant la fonction de Document
        JButton dumpBtn = new JButton("Dump");
        dumpBtn.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dumpModel();
			}
		});
        // JButton pour afficher les Elements
        JButton elementBtn = new JButton("Elements");
        elementBtn.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dumpAllElements();
			}
		});
        // Button pour Bold
        JButton strongBtn = new JButton( getActionByName("font-bold"));
        // Button pour Italique
        JButton emBtn = new JButton( getActionByName("font-italic"));
        // Bouton pour XML
        JButton xmlBtn = new JButton("XML");
        xmlBtn.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dumpXML();
			}
		});
        //
        buttonPanel.add( strongBtn );
        buttonPanel.add( emBtn );
        buttonPanel.add( dumpBtn );
        buttonPanel.add( elementBtn );
        buttonPanel.add( xmlBtn );
        //Add the components.
        add( buttonPanel, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
        add(statusPane, BorderLayout.PAGE_END);
        
        // Et écoute les événements
        _textPane.addCaretListener(caretListenerLabel);
	}
	
	protected void initDocument() {
        String initString[] =
                { "Un exemple d'un gros paragraphe qui, je l'espère, va faire plusieurs lignes.",
                  "C'est pour tester le comportement par défaut du curseur, encore appelé 'Caret'.",
                  "En fait, j'aimerais comprendre comment opère le principe de sélection entre mot, phrase, paragrape ?",
                  "On verra bien."+newline,
                  "Et ça c'est juste une ligne.",
                };

        SimpleAttributeSet attr16 = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attr16, "SansSerif");
        StyleConstants.setFontSize(attr16, 16);
        SimpleAttributeSet attr20 = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attr20, "SansSerif");
        StyleConstants.setFontSize(attr20, 20);
        SimpleAttributeSet attr[] = {attr16, attr16, attr16, attr16, attr20};
        
        
        //SimpleAttributeSet[] attrs = initAttributes(initString.length);

        try {
            for (int i = 0; i < initString.length; i ++) {
                _styledDoc.insertString(_styledDoc.getLength(), initString[i],
                        attr[i]);
            }
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text.");
        }
    }
	
	//The following two methods allow us to find an
    //action provided by the editor kit by its name.
    private void createActionTable(JTextComponent textComponent) {
        actions = new HashMap<Object, Action>();
        Action[] actionsArray = textComponent.getActions();
        for (int i = 0; i < actionsArray.length; i++) {
            Action a = actionsArray[i];
            actions.put(a.getValue(Action.NAME), a);
//            System.out.println("Action class="+a.getClass().getCanonicalName());
//            System.out.println("       name="+a.getValue(Action.NAME));
        }
    }
    private Action getActionByName(String name) {
        return actions.get(name);
    }
	
    /**
     * Un JLabel qui écoute le Caret et qui donne sa positon
     * et/ou sa sélection.
     */
    protected class CaretListenerLabel extends JLabel
                                       implements CaretListener {
        public CaretListenerLabel(String label) {
            super(label);
        }

        //Might not be invoked from the event dispatching thread.
        public void caretUpdate(CaretEvent e) {
            displaySelectionInfo(e.getDot(), e.getMark());
        }

        //This method can be invoked from any thread.  It 
        //invokes the setText and modelToView methods, which 
        //must run in the event dispatching thread. We use
        //invokeLater to schedule the code for execution
        //in the event dispatching thread.
        protected void displaySelectionInfo(final int dot,
                                            final int mark) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (dot == mark) {  // no selection
                        try {
                            Rectangle caretCoords = _textPane.modelToView(dot);
                            //Convert it to view coordinates.
                            setText("caret: text position: " + dot
                                    + ", view location = ["
                                    + caretCoords.x + ", "
                                    + caretCoords.y + "]"
                                    + newline);
                        } catch (BadLocationException ble) {
                            setText("caret: text position: " + dot + newline);
                        }
                    } else if (dot < mark) {
                        setText("selection from: " + dot
                                + " to " + mark + newline);
                    } else {
                        setText("selection from: " + mark
                                + " to " + dot + newline);
                    }
                }
            });
        }
    }
    /** Dump Model en utilisant la méthode AbstractElement::dump */
    void dumpModel() {
    	Element root = _styledDoc.getDefaultRootElement();
    	System.out.println("*** DUMP ***");
    	if( root instanceof AbstractElement) {
    		((AbstractElement) root).dump(System.out, 0);
    	}
    }
    /** Dump Model en passant en revue tous les Elements -> dumpElements */
    void dumpAllElements() {
    	Element root = _styledDoc.getDefaultRootElement();
    	System.out.println("*** ROOT ***");
    	dumpElements(root);
    }
    void dumpElements(Element elem) {
    	System.out.println("Elem : "+elem.getName() + " with " + elem.getElementCount() + " childs");
    	System.out.println("=> "+elem.toString());
    	System.out.println("  class : "+elem.getClass().getCanonicalName());
    	System.out.println("  start at " + elem.getStartOffset() + " -> " + elem.getEndOffset());
    	// Attributes
    	System.out.print("  attributes : ");
    	for (Enumeration<?> e = elem.getAttributes().getAttributeNames(); e.hasMoreElements();)
    	       System.out.print("- " +e.nextElement()+"; ");
    	System.out.println(""+newline);
    	if( elem.isLeaf() ) {
    		try {
				System.out.println( "  ["+
						_styledDoc.getText(elem.getStartOffset(), elem.getEndOffset()-elem.getStartOffset())+
						"]");
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
    	}
    	System.out.println("---"+newline);
    	// childs
    	for (int i = 0; i < elem.getElementCount(); i++) {
			dumpElements( elem.getElement(i));
		}
    }
	
    /** Dump Model en utilisant un DocumentConverter */
    void dumpXML() {
    	System.out.println("** Document to XML **");
		XStream xStream = new XStream(new DomDriver());
		xStream.registerConverter(new DocumentConverter());
        xStream.alias("doc", javax.swing.text.DefaultStyledDocument.class);
        System.out.println(xStream.toXML(_styledDoc));
//        
//        File outfile = new File("tmp/story.xml");
//        try {
//			FileOutputStream writer = new FileOutputStream(outfile);
//			xStream.toXML(story, writer);
//			writer.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
    }
    
}
