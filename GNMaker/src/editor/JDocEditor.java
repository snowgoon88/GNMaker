/**
 * 
 */
package editor;

import gui.DocHighlighter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument.AbstractElement;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import data.MyStyledDocument;
import data.converter.DocumentConverter;

/**
 * Essai d'implémentation d'un Editeur WYSIWYG.
 * Inspiration de http://da2i.univ-lille1.fr/doc/tutorial-java/uiswing/components/generaltext.html
 * Sur Elements http://www.comp.nus.edu.sg/~cs3283/ftp/Java/swingConnect/text/element_interface/element_interface.html#insertDefaultStyledDocument
 * 
 * Le Caret du JTextPane est responsable du comportement de sélection.
 * 
 * Boutons : 
 *  - New : efface le contenu du document
 *  - Bold, Italique, 
 *  - Dump : dump en utilisant la méthode de Elements, 
 *  - Elements : dump tous les éléments des Elements du Document, 
 *  - XML : DocumentConverter pour faire de l'XML et sauve dans tmp/document.xml
 *  - Load : relis à partir de tmp/document.xml
 *  
 * Ligne de notificatioun (JLabel) : écoute le Carret, position et sélection
 *           par le biais de CaretListenerLabel.      
 * 
 * @todo : définir class MyStyledDocument avec styles et character format
 * @todo : définir des styles nommés de paragraphes (Title, h1, h2, h3, body)
 *   ==> QUE pour paragraphe (pas emphasis ou highlight !)
 * @todo : highlight ??
 *   ==> pas d'action de base dans EditorKit
 *   ==> essayer avec ChangeCharacterAttributes : ca marche
 *   ==> pas détecter avec StyleConstants.isXXX
 *   ==> mais peut être nommé avec un Style.
 *   ==> A NE PAS PROPOSER ??
 *   ==> Faire comme Bold et Italic Action
 * @todo : list avec niveau d'indentation
 *   ==> StyleConstants.leftMargin
 *   ==> Comment ajouter un icon en début de ligne
 *   ==> Niveau d'indentations
 * @todo : compacter et pretty/nettoyer un document
 *   ==> merger les contenus qui ont le même characterAttrSet
 *   OUI ==> c'est peut-être fait automatiquement lors de la relecture 
 * @todo : action undoables
 *   ==> s'inspirer de TextComponentDemo
 *   ==> Faire UNDO / REDO Action
 * @todo : sauver et relire un document en XML ??
 *  ====>  écrit en HTML les accents et les cédilles, mais relit sans problème.
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class JDocEditor extends JPanel {
	
	JTextPane _textPane;
	MyStyledDocument _styledDoc;
	// Toutes les actions définie dans le EditorKit
	HashMap<Object, Action> actions;
	
	// Highlight helpers
	//DocHighlighter _highlighter = new DocHighlighter();
	
	//undo helpers
    UndoAction _undoAction;
    RedoAction _redoAction;
    UndoManager _undo = new UndoManager();
	
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
        _styledDoc = new MyStyledDocument();
        _textPane.setStyledDocument( _styledDoc );
//        _styledDoc = _textPane.getStyledDocument();
        System.out.println("_styleDoc is a "+_styledDoc.getClass().getCanonicalName());
//        if (_styledDoc instanceof AbstractDocument) {
//            doc = (AbstractDocument) _styledDoc;
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
        
        // Create our own actions
        _undoAction = new UndoAction();
        _redoAction = new RedoAction();
        
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
        strongBtn.setText("<html><strong>B</strong></html>");
        // Button pour Italique
        JButton emBtn = new JButton( getActionByName("font-italic"));
        emBtn.setText( "<html><em>I</em></html>" );
        // Button pour Highlight
        JButton highBtn = new JButton( "High" );
        highBtn.addActionListener( new ActionListener() {
			
			@Override
			/** Change ou Unset la couleur de Highlight */
			public void actionPerformed(ActionEvent e) {
				// Les attributs actuel de l'élément de texte (cf StyledEditorKit)
				StyledEditorKit editor = (StyledEditorKit) _textPane.getEditorKit();
				MutableAttributeSet attr = editor.getInputAttributes();
				//System.out.println("High : prev_highlight = " + DocHighlighter.getHighlight(attr));

				// Déjà Highlight -> Unset
				if( DocHighlighter.getHighlight(attr) != null ) {
					// On remplace(true) en enlevant les attribut d'Highlight
					DocHighlighter.unsetHighlight(attr);
					_styledDoc.setCharacterAttributes(_textPane.getSelectionStart(),
							_textPane.getSelectionEnd() - _textPane.getSelectionStart(),
							attr, true /*remplace*/);
				}
				else {
					// On ajoute (false) les attributs highlight
					DocHighlighter.setHighlight(attr, "cyan");
					_styledDoc.setCharacterAttributes(_textPane.getSelectionStart(),
							_textPane.getSelectionEnd() - _textPane.getSelectionStart(),
							attr, false /*ajoute*/);
				}
			}
		});
        // Bouton pour XML
        JButton xmlBtn = new JButton("XML");
        xmlBtn.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dumpXML();
			}
		});
        //
        // Button pour effacer
        JButton newBtn = new JButton( "New" );
        newBtn.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					_styledDoc.remove(0, _styledDoc.getLength());
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
		});
        //
        // JButton pour relire
        JButton loadBtn = new JButton( "Load" );
        loadBtn.addActionListener( new ActionListener(	) {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				loadXML();	
			}
		});
        //
        // Label et JComboBox pour changer de style
        String stylesList[]  = { "base", "title", "sec1", "sec2" };	
        JLabel styleLabel = new JLabel( "Style:");
        //Create the combo box, select item at index 4 (index starts at 0.	
        JComboBox<String> stylesCBox = new JComboBox<String>( stylesList );
        stylesCBox.setSelectedIndex(0);
        stylesCBox.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				_styledDoc.setLogicalStyle(_textPane.getCaretPosition(), 
						_styledDoc.getStyle((String) cb.getSelectedItem()) );
			}
		});
        // Undo / Redo
        JButton undoBtn = new JButton( _undoAction );
        JButton redoBtn = new JButton( _redoAction );
        //
        buttonPanel.add( newBtn );
        buttonPanel.add( styleLabel );
        buttonPanel.add( stylesCBox );
        buttonPanel.add( strongBtn );
        buttonPanel.add( emBtn );
        buttonPanel.add( highBtn );
        buttonPanel.add( undoBtn );
        buttonPanel.add( redoBtn );
        buttonPanel.add( dumpBtn );
        buttonPanel.add( elementBtn );
        buttonPanel.add( xmlBtn );
        buttonPanel.add( loadBtn );
        //Add the components.
        add( buttonPanel, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
        add(statusPane, BorderLayout.PAGE_END);
        
        // Et écoute les événements
        _textPane.addCaretListener(caretListenerLabel);
        _styledDoc.addUndoableEditListener(new MyUndoableEditListener());
	}
	
	public void initDocument() {
        String initString[] = { 
        		"Gros Titre"+newline,
        		"Hop, on commence"+newline,
        		"Un exemple d'un gros paragraphe qui, je l'espère, va faire plusieurs lignes.",
        		"C'est pour tester le comportement par défaut du curseur, encore appelé 'Caret'.",
        		"En fait, j'aimerais comprendre comment opère le principe de sélection entre mot, phrase, paragraphe ? On verra bien."+newline,
        		"Puis, on continue"+newline,
        		"Et ça c'est juste une ligne."+newline,
                };
        String styleString[] = { "title", "sec1", "base", "base", "base", "sec1", "base" };
        try {
            for (int i = 0; i < initString.length; i ++) {
            	_styledDoc.setLogicalStyle(_styledDoc.getLength(),
            				_styledDoc.getStyle(styleString[i]) );
            	_styledDoc.insertString(_styledDoc.getLength(), initString[i], null );
            	
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
	
    /** Listens for actions that can be undone */
    protected class MyUndoableEditListener
    implements UndoableEditListener {
    	public void undoableEditHappened(UndoableEditEvent e) {
    		//Remember the edit and update the menus.
    		_undo.addEdit(e.getEdit());
    		_undoAction.updateUndoState();
    		_redoAction.updateRedoState();
    		System.out.println("_undo : "+ _undo.getPresentationName());
    	}
    }
    
    class UndoAction extends AbstractAction {
        public UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                _undo.undo();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            updateUndoState();
            _redoAction.updateRedoState();
        }

        protected void updateUndoState() {
            if (_undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, _undo.getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }

    class RedoAction extends AbstractAction {
        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                _undo.redo();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            updateRedoState();
            _undoAction.updateUndoState();
        }

        protected void updateRedoState() {
            if (_undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, _undo.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
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
        xStream.alias("doc", MyStyledDocument.class);
        System.out.println(xStream.toXML(_styledDoc));
        
        File outfile = new File("tmp/document.xml");
        try {
			FileOutputStream writer = new FileOutputStream(outfile);
			xStream.toXML(_styledDoc, writer);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    /** Load Model en utilisant un DocumentConverter */
    void loadXML() {
    	System.out.println("** XML to Document **");
		XStream xStream = new XStream(new DomDriver());
		xStream.registerConverter(new DocumentConverter());
        xStream.alias("doc", MyStyledDocument.class);
        
        _styledDoc = (MyStyledDocument) xStream.fromXML(new File("tmp/document.xml"));
        _textPane.setStyledDocument(_styledDoc);
    }
}
