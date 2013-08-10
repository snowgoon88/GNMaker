/**
 * 
 */
package test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
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
 * 
 * @author snowgoon88@gmail.com
 */
public class TestTextArea {
	JDialog _testDialog;
	
	JPanel _main;
	JTextArea _area1;
	JTextArea _area2;
	
	public TestTextArea() {
		buildGUI();
	}
	public void run() {
		testComponent("TextArea", _main);
	}
	
	void buildGUI() {
		_main = new JPanel();
		BorderLayout bLayout = new BorderLayout();
//		_main.setLayout(bLayout);
		
		// Ne grossit que les colonnes marquées grow.
		// Les contraintes dans le add() ne concerne que les élements d'une même Cellule. !!!!
		MigLayout migLayout = new MigLayout(
				"debug", // Layout Constraints
				"[][grow,fill][]", // Column constraints
				""); // Row constraints);
		_main.setLayout(migLayout);
		
		_area1 = new JTextArea("Pour voir");
		JLabel avantLab1 = new JLabel("Avant");
		JLabel apresLab1 = new JLabel("Apres");
		
		_main.add(avantLab1, "");
		_main.add(_area1,    "");
		_main.add(apresLab1, "wrap");
		
		_area2 = new JTextArea("Pour voir");
		JLabel avantLab2 = new JLabel("Avant");
		JLabel apresLab2 = new JLabel("Apres");
		
		_main.add(avantLab2, "");
		_main.add(_area2,    "");
		_main.add(apresLab2, "");
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

}
