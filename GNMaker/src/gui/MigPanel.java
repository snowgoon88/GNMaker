package gui;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;


/**
 * L'idée est que le Panel ne soit pas Scrollable Horizontalement.
 * 
 * Dans les MigLayout, les JTextArea ne se réduisent pas quand la largeur de la 
 * fenêtre change. D'où l'astuce.
 * 
 * Voir : http://stackoverflow.com/questions/2475787/miglayout-jtextarea-is-not-shrinking-when-used-with-linewrap-true
 */
@SuppressWarnings("serial")
public class MigPanel extends JPanel implements Scrollable
{
	public MigPanel() {
		super();
	}
	public MigPanel(LayoutManager layout) {
		super(layout);
	}
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 0;
	}
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 0;
	}
}
