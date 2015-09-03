/**
 * 
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pushingpixels.flamingo.api.common.CommandButtonDisplayState;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonPopupOrientationKind;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.icon.ImageWrapperResizableIcon;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;

import data.MyStyledDocument;

/**
 * Edite un 'MyStyledDocument' dans un JPanel.
 * 
 * Les actions possibles sont :
 * <ul>
 * <li>Paragraph : Voir et définir le 'Style' dans un JComboBox : _styleCBox</li>
 * <li>Character : On/Off 'bold' dans un Button</li>
 * <li>Character : On/Off 'italic' dans un Button</li>
 * <li>Character : Choisi couleur 'hightligh' ou pas dans un JCommandButton qui fait office de "SpliButton"</li>
 * <li>Global : Undo/Redo les changements</li>
 * </ul>
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class DocEditorV extends JPanel {

	/** Un MyDocument comme modèle */
	MyStyledDocument _doc;
	
	/** Toutes les actions définie dans le EditorKit */
	HashMap<Object, Action> _actions;
	
	/** Un JTextPane pour le document */
	JTextPane _textPane;
	/** Un JComboBox pour les styles */
	JComboBox<String> _styleCBox;
	/** Un JToggleButton pour le bold */
	JToggleButton _strongBtn;
	/** Un JToggleButton pour le italic */
	JToggleButton _emBtn;
	/** JCommandButton pour Highlight */
	JCommandButton _highBtn;
	
	//undo helpers
    UndoAction _undoAction;
    RedoAction _redoAction;
    UndoManager _undo = new UndoManager();
    
    /** Boolean qui dit si on est en update des Bouton ou en action */
    boolean _updateStyle = false;
    
    private static String COL_HIGHLIGHT[] = {
    	"yellow", "cyan", "red", "green", "magenta", "grey" 
    };
	
    /* In order to Log */
	private static Logger logger = LogManager.getLogger(EventV.class.getName());
	
    
	/**
	 * 
	 */
	public DocEditorV( MyStyledDocument doc) {
		super(); // new JPanel
		this._doc = doc;
		
		buildGUI();
	}

	void buildGUI() {
		// BorderLayout
		setLayout( new BorderLayout() );
		
		// TextPanel pour le document
		_textPane = new JTextPane( _doc );
		_textPane.setCaretPosition(0);
        _textPane.setMargin(new Insets(5,5,5,5));
        
        createActionTable(_textPane); // la liste des actions par défaut
        // Create our own actions
        _undoAction = new UndoAction();
        _redoAction = new RedoAction();
        
        // Dans une fenêtre avec Scroll
        // @todo : dimension, scroll optionnel
        JScrollPane scrollPane = new JScrollPane(_textPane);
        scrollPane.setPreferredSize(new Dimension(600, 50));
        add(scrollPane, BorderLayout.CENTER);
		
		// Action Panel
		JPanel actionPanel = new JPanel();
		
		// JComboBox pour changer de style
		_styleCBox = new JComboBox<String>(_doc.getStyleList());
		//_styleCBox.setSelectedIndex(0);
		_styleCBox.setSelectedItem( _doc.getStyleList()[0] );
        _styleCBox.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.debug("_style : "+e.paramString());
				if( _updateStyle == false ) {
					@SuppressWarnings("unchecked")
					JComboBox<String> cb = (JComboBox<String>) e.getSource();
					_doc.setLogicalStyle(_textPane.getCaretPosition(), 
							_doc.getStyle((String) cb.getSelectedItem()) );
				}
			}
		});
        actionPanel.add( _styleCBox );
        // JToggleButton pour passer en bold
        _strongBtn = new JToggleButton( getActionByName("font-bold") );
        _strongBtn.setText("<html><strong>B</strong></html>");
        actionPanel.add( _strongBtn );
        // JToggleButton pour passer en italic
        _emBtn = new JToggleButton( getActionByName("font-italic") );
        _emBtn.setText("<html><em>I</em></html>");
        actionPanel.add( _emBtn );
        
        // Highlighter
        _highBtn = new JCommandButton("-rien", ImageWrapperResizableIcon.getIcon(DocHighlighter.createNormalIcon(),
									new Dimension(10, 10)));
        _highBtn.setCommandButtonKind(CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        _highBtn.setPopupOrientationKind(CommandButtonPopupOrientationKind.DOWNWARD);
        _highBtn.setFlat(false);
        _highBtn.setDisplayState(CommandButtonDisplayState.SMALL); // Seul Icon
        _highBtn.setPopupCallback(new HighlightPopupCallback());
        _highBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// Les attributs actuel de l'élément de texte (cf StyledEditorKit)
				StyledEditorKit editor = (StyledEditorKit) _textPane.getEditorKit();
				MutableAttributeSet attr = editor.getInputAttributes();
        		if( _highBtn.getText().equalsIgnoreCase("-rien-")) {
        			DocHighlighter.unsetHighlight(attr);
					_doc.setCharacterAttributes(_textPane.getSelectionStart(),
							_textPane.getSelectionEnd() - _textPane.getSelectionStart(),
							attr, true /*remplace*/);
        		}
        		else {
        			String colName = _highBtn.getText();
        			DocHighlighter.setHighlight(attr, colName);
					_doc.setCharacterAttributes(_textPane.getSelectionStart(),
							_textPane.getSelectionEnd() - _textPane.getSelectionStart(),
							attr, false /*ajoute*/);
        		}
				System.out.println("high ACTION = "+_highBtn.getText());
			}
		});
		actionPanel.add( _highBtn );
		
		actionPanel.add( new JSeparator(SwingConstants.VERTICAL) );
		
        // Undo/Redo buttons
        JButton undoBtn = new JButton( _undoAction );
        JButton redoBtn = new JButton( _redoAction );
        actionPanel.add( undoBtn );
        actionPanel.add( redoBtn );
        
        add( actionPanel, BorderLayout.NORTH );
        
        // Un Listener pour mettre à jour le _styleCBox
        _textPane.addCaretListener( new StyleListener() );
        // Un Listener pour Undo/Redo
        _doc.addUndoableEditListener(new MyUndoableEditListener());
        
        _textPane.setCaretPosition(0);
        updateStyle();
	}
	
	void updateStyle() {
		// sauvegarde état de update
		boolean updateStyleSave = _updateStyle;
		_updateStyle = true;
		// Style de paragraphe
		Style style =_doc.getLogicalStyle( _textPane.getCaretPosition() );
		_styleCBox.setSelectedItem( style.getName() );
		// Style de caractères
		AttributeSet attr = _doc.getCharacterElement(_textPane.getCaretPosition()).getAttributes();
		_strongBtn.setSelected( StyleConstants.isBold( attr ) );
		_emBtn.setSelected( StyleConstants.isItalic( attr ) );
		
		// Restore
		_updateStyle = updateStyleSave;
	}

	/** Une Classe qui met à jour _styleCBox en fonction de 
	 * la position du Carret
	 */
	protected class StyleListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			updateStyle();
		}
	}
	
	/** 
	 * Fait la liste des Actions proposée par l'EditorKit par défaut
	 */
    private void createActionTable(JTextComponent textComponent) {
        _actions = new HashMap<Object, Action>();
        Action[] actionsArray = textComponent.getActions();
        for (int i = 0; i < actionsArray.length; i++) {
            Action a = actionsArray[i];
            _actions.put(a.getValue(Action.NAME), a);
//            System.out.println("Action class="+a.getClass().getCanonicalName());
//            System.out.println("       name="+a.getValue(Action.NAME));
        }
    }
    /** Trouve une action de l'EditorKit par son nom */
    private Action getActionByName(String name) {
        return _actions.get(name);
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
    
    /**
     * Action undo. 
     */
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
    /**
     * Action redo. 
     */
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
     * Popup Menu pour le bouton Higlight.
     */
    class HighlightPopupCallback implements PopupPanelCallback {
		@Override
		public JPopupPanel getPopupPanel(JCommandButton commandButton) {

			
			JCommandPopupMenu simpleMenu = new JCommandPopupMenu();

			ActionListener itemAction = new ActionListener() {	
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JCommandMenuButton item = (JCommandMenuButton) e.getSource();
					_highBtn.setIcon( item.getIcon() );
					_highBtn.setText( item.getText() );
				}
			};
			
			JCommandMenuButton item = new 
					JCommandMenuButton("-rien-", 
							ImageWrapperResizableIcon.getIcon(DocHighlighter.createNormalIcon(),
									new Dimension(10, 10)));
			item.addActionListener( itemAction );
			simpleMenu.addMenuButton( item );
			
			for (int i = 0; i < COL_HIGHLIGHT.length; i++) {
				item = new JCommandMenuButton(COL_HIGHLIGHT[i], 
								ImageWrapperResizableIcon.getIcon(DocHighlighter.createHighlighIcon(COL_HIGHLIGHT[i]),
										new Dimension(10, 10)));
				item.addActionListener( itemAction );
				simpleMenu.addMenuButton( item );
			}
			
			return simpleMenu;
		}
	};
}
