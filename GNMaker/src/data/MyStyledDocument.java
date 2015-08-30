/**
 * 
 */
package data;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

/**
 * @todo : verifier XML save/load !!!
 * @todo : Commenter la Classe
 * Styles créés :
 * <ul>
 * <li>base</li>
 * <li>title = base + centered + size22 + bold</li>
 * <li>sec1 = base + size20 + bold</li>
 * <li>sec2 = base + size18</li>
 * </ul>
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
	protected final String _stylesList[] = 
		{ "base", "title", "sec1", "sec2" };
	
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
	public String[] getStyleList() {
		return _stylesList;
	}
}
