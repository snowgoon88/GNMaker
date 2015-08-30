/**
 * 
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Style;

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
 * </ul>
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class DocEditorV extends JPanel {

	/** Un MyDocument comme modèle */
	MyStyledDocument _doc;
	
	/** Un JTextPane pour le document */
	JTextPane _textPane;
	/** Un JComboBox pour les styles */
	JComboBox<String> _styleCBox;
	
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
//		_textPane.setCaretPosition(0);
        _textPane.setMargin(new Insets(5,5,5,5));
        // Dans une fenêtre avec Scroll
        // @todo : dimension, scroll optionnel
        JScrollPane scrollPane = new JScrollPane(_textPane);
        scrollPane.setPreferredSize(new Dimension(200, 200));
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
				@SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				_doc.setLogicalStyle(_textPane.getCaretPosition(), 
						_doc.getStyle((String) cb.getSelectedItem()) );
			}
		});
        
        actionPanel.add( _styleCBox );
        add( actionPanel, BorderLayout.NORTH );
        
        // Un Listener pour mettre à jour le _styleCBox
        _textPane.addCaretListener( new StyleListener() );
        
        _textPane.setCaretPosition(0);
	}

	/** Une Classe qui met à jour _styleCBox en fonction de 
	 * la position du Carret
	 */
	protected class StyleListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			Style style =_doc.getLogicalStyle( _textPane.getCaretPosition() );
			_styleCBox.setSelectedItem( style.getName() );
		}
	}
}
