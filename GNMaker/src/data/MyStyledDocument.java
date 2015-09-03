/**
 * 
 */
package data;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

/**
 * Un MyStyledDocument est une extension de DefaultStyledDocument avec quelques
 * styles pour les paragraphes définis par défaut.
 * 
 * Styles créés :
 * <ul>
 * <li>base</li>
 * <li>title = base + centered + size22 + bold</li>
 * <li>sec1 = base + size20 + bold</li>
 * <li>sec2 = base + size18</li>
 * </ul>
 * 
 * Ce qu'il faut aussi retenir sur les ***Documents.
 * Un document est en fait un arbre d'Elements
 * Inspiration de http://da2i.univ-lille1.fr/doc/tutorial-java/uiswing/components/generaltext.html
 * Sur Elements http://www.comp.nus.edu.sg/~cs3283/ftp/Java/swingConnect/text/element_interface/element_interface.html#insertDefaultStyledDocument
 *
 * Le Caret du JTextPane souvent associé est responsable du comportement de sélection.
 * 
 * Les stymes de paragraphes ne s'applique pas aux caractères. Ces derniers ne peuvent pas avoir
 * de styles nommés. 
 * 
 * Ce n'est pas utile de compacter le document quand on le sauve (pour faire une sorte de
 * ramasse miette des Elements contigus qui aurait, en fait, les mêmes effets de caractère)
 * car à la lecture, la succession des <code>insertString()</string> fait finalement
 * automatiquement ce travail.
 * 
 * @todo : list avec niveau d'indentation
 *   ==> StyleConstants.leftMargin
 *   ==> Comment ajouter un icon en début de ligne
 *   ==> Niveau d'indentations
 * @todo : copier/coller
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class MyStyledDocument extends DefaultStyledDocument {

	
	/**
	 * Création avec initialisation des Styles.
	 */
	public MyStyledDocument() {
		initStyles();
	}
	/**
	 * Récupère la liste des styles de paragraphe.
	 */
	public String[] getStyleList() {
		return _stylesList;
	}
	/**
	 * Efface tout le document et y met le 'text' avec le style "base".
	 * @param text
	 */
	public void setText( String text ) {
		try {
			remove(0, getLength());
			setLogicalStyle(0, getStyle("base"));
			insertString(0, text, null);
		} catch (BadLocationException e) {
			System.err.println("MyDocument.setText() : "+e.getMessage());
		}
	}
	
	/**
	 * Liste des nom des styles de paragraphes.
	 */
	protected final String _stylesList[] = 
		{ "base", "title", "sec1", "sec2" };
	/**
	 * Initialise les différents styles.
	 */
	protected void initStyles() {
		// base
		Style baseStyle = addStyle( "base", null);
		StyleConstants.setFontFamily( baseStyle, "SansSerif");
		StyleConstants.setFontSize( baseStyle, 12);
		// Title
		Style titleStyle = addStyle( "title", baseStyle);
		StyleConstants.setFontSize( titleStyle, 22);
		StyleConstants.setAlignment( titleStyle, StyleConstants.ALIGN_CENTER );
		StyleConstants.setBold( titleStyle, true);
		// Section1
		Style sec1Style = addStyle( "sec1", baseStyle);
		StyleConstants.setFontSize( sec1Style, 20);
		StyleConstants.setBold( sec1Style, true);
		// Section1
		Style sec2Style = addStyle( "sec2", baseStyle);
		StyleConstants.setFontSize( sec2Style, 18);
	}
	
}
