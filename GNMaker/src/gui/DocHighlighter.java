/**
 * 
 */
package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Methodes statiques et Action pour 'Highlighter' un StyledDocument.
 * (Inspiré de <code>StyleConstants</code>
 * 
 * @author snowgoon88@gmail.com
 */
public class DocHighlighter {

	private static final int DEFAULT_ICON_SIZE = 15;
	
	/** Il faut un Object qui est une constante, alors je crée la 
	 * classe de HighlightConstants.
	 */
	public static class HighlightConstants
	implements AttributeSet.CharacterAttribute {
		String representation;
		private HighlightConstants() {
			this.representation = new String("high");
		}
	    public String toString() {
	        return representation;
	    }
	}
	public static final Object Highlight = new HighlightConstants();
	
	/** Highlight avec un nom de couleur */
	public static void setHighlight(MutableAttributeSet attr, String colorName) {
		// La méthode Color.getColor() ne marche pas vraiment alors...
		Color color;
		try {
		    color = (Color)Color.class.getField(colorName).get(null);
		} catch (Exception e) {
		    color = null; // Not defined
		}
		attr.addAttribute( StyleConstants.Background, color);
		attr.addAttribute( Highlight, colorName );
	}
	/** Enlève Highlight des attributs */
	public static void unsetHighlight(MutableAttributeSet attr )
	{
		attr.removeAttribute(StyleConstants.Background);
		attr.removeAttribute( Highlight );
	}
	/**
	 * Quel est le nom de la couleur de Highlight ?
	 * 
	 * @param attr : les attributs de Characters
	 * @return String ou null si pas Highlight.
	 */
	public static String getHighlight(AttributeSet attr) {
		return (String) attr.getAttribute(Highlight);
	}
	
	/** Icones : carré avec couleur à l'intérieur */
	public static Image createHighlighIcon( String colorName ) {
		Image image = new BufferedImage(DEFAULT_ICON_SIZE,
				DEFAULT_ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = image.getGraphics();
		
		// La méthode Color.getColor() ne marche pas vraiment alors...
		Color color;
		try {
			color = (Color)Color.class.getField(colorName).get(null);
		} catch (Exception e) {
			color = null; // Not defined
		}
		// intérieur
		g.setColor(color);
		g.fillRect(0, 0, DEFAULT_ICON_SIZE-1, DEFAULT_ICON_SIZE-1);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, DEFAULT_ICON_SIZE-1, DEFAULT_ICON_SIZE-1);
		
		g.dispose();
		return image;
	}
	public static Image createNormalIcon() {
		Image image = new BufferedImage(DEFAULT_ICON_SIZE,
				DEFAULT_ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = image.getGraphics();
		
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, DEFAULT_ICON_SIZE-1, DEFAULT_ICON_SIZE-1);
		g.setColor(Color.RED);
		g.drawLine(0, 0, DEFAULT_ICON_SIZE-1, DEFAULT_ICON_SIZE-1);
		
		g.dispose();
		return image;
	}
}
