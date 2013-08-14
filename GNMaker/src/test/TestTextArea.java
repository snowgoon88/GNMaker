/**
 * 
 */
package test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

/**
 * Le but est de voir comment on peut gérer et utiliser les JTextArea.
 * 1) dans un JDialog, JTextArea grows and fills.
 * 2) dans un JPanel avec FlowLayout => reste à taille constante.
 * 3) dans un JPanel avec BorderLayout => grows and fills
 * 4) dans un MigLayout basic => constant
 * 5) dans un MigLayout avec "grow, fill", // Column constraints => growX
 * 6) dans un MigLayout avec "grow, fill", // Column constraints
				             "grow, fill"); // Row constraints); => grow both
 * 7) mais dans un scrollable, ça ne rétrécit plus.				             
 * 
 * @author snowgoon88@gmail.com
 */
public class TestTextArea {
	JDialog _testDialog;
	
	JPanel _main;
	JScrollPane _scrollMain;
	JTextArea _area1;
	JTextArea _area2;
	
	public TestTextArea() {
		buildGUI();
	}
	public void run() {
		testComponent("TextArea", _scrollMain);
	}
	
	void buildGUI() {
		//_main = new JPanel();
		BorderLayout bLayout = new BorderLayout();
//		_main.setLayout(bLayout);
		
		// Ne grossit que les colonnes marquées grow.
		// Les contraintes dans le add() ne concerne que les élements d'une même Cellule. !!!!
		MigLayout migLayout = new MigLayout(
				"debug", // Layout Constraints
				"[][grow,fill][]", // Column constraints
				""); // Row constraints);
		//_main.setLayout(migLayout);
		_main = new MyPanel(migLayout);
		
		_area1 = new JTextArea("Pour voir ce que ça donne sur des grandes lignes.");
		_area1.setWrapStyleWord(true);
		_area1.setLineWrap(true);
		JPanel area1Panel = new MyPanel(new MigLayout("wrap", "[grow,fill]", "[]"));
		area1Panel.add(_area1);
		JLabel avantLab1 = new JLabel("Avant");
		JLabel apresLab1 = new JLabel("Apres");
		
		_main.add(avantLab1, "");
		//_main.add(area1Panel,    "wmin 100");
		_main.add(_area1,    "wmin 100");
		_main.add(apresLab1, "wrap");
		
		_area2 = new JTextArea("Pour voir");
		JPanel area2Panel = new MyPanel(new MigLayout("wrap", "[grow,fill]", "[]"));
		area2Panel.add(_area2);
		JLabel avantLab2 = new JLabel("Avant");
		JLabel apresLab2 = new JLabel("Apres");
		
		_main.add(avantLab2, "");
		//_main.add(area2Panel,    "wmin 100");
		_main.add(_area2,    "wmin 100");
		_main.add(apresLab2, "");
		
		_scrollMain = new JScrollPane(_main);
	}
	
	
	/**
	 * Utilise un JDialog modal (freeze until all event are processed) 
	 * pour afficher et tester un Component
	 * 
	 * @param title
	 * @param thing
	 * @return
	 */
	boolean testComponent(String title, Component thing) {
		_testDialog = new JDialog();
		_testDialog.setModal(true);
		_testDialog.setTitle(title);
		_testDialog.add(thing);
		_testDialog.pack();
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					_testDialog.setVisible(true);
					
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestTextArea app = new TestTextArea();
		app.run();

	}

	// Pour embedder les JTextArea dans des scrollPane :o)
	// http://stackoverflow.com/questions/2475787/miglayout-jtextarea-is-not-shrinking-when-used-with-linewrap-true
	@SuppressWarnings("serial")
	static class MyPanel extends JPanel implements Scrollable
	{
		MyPanel(LayoutManager layout)
		{
			super(layout);
		}

		public Dimension getPreferredScrollableViewportSize()
		{
			return getPreferredSize();
		}

		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
		{
			return 0;
		}

		public boolean getScrollableTracksViewportHeight()
		{
			return false;
		}

		public boolean getScrollableTracksViewportWidth()
		{
			return true;
		}

		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
		{
			return 0;
		}
	}
	
}
