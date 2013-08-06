/**
 * 
 */
package test;

import gui.JEvent;
import gui.JPersoEvent;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import data.Event;
import data.Perso;

/**
 * @author dutech
 *
 */
public class TestGUI {
	
	static boolean _run_test;
	JDialog _testDialog;
	
	public void run(String[] args) {	
		boolean res;
		int nbTest = 0;
		int nbPassed = 0;
		
		// -------
		nbTest++;
		res = testJEventPerso(args);
		if (res) {
			System.out.println("testJEventPerso >> " + res);
			nbPassed++;
		} else {
			System.err.println("testJEventPerso >> " + res);
		}
		// -------
		nbTest++;
		res = testJPersoList(args);
		if (res) {
			System.out.println("testJPersoList >> " + res);
			nbPassed++;
		} else {
			System.err.println("testJPersoList >> " + res);
		}
		// -------
		nbTest++;
		res = testJEvent(args);
		if (res) {
			System.out.println("testJEvent >> " + res);
			nbPassed++;
		} else {
			System.err.println("testJEvent >> " + res);
		}
		
		// ---------------------
		if (nbTest > nbPassed) {
			System.err.println("FAILURE : only "+nbPassed+" success out of "+nbTest);
			System.exit(1);
		}
		else {
			System.out.println("SUCCESS : "+nbPassed+" success out of "+nbTest);
			System.exit(0);
		}
	}
	
	// Afficher Perso : Bouton Nom
	//               click gauche => switch status
	//               click droit => popup avec delete (+ confirm) ou change status
	//               et info.
	boolean testJEventPerso(String[] args) {
		Event evt1 = new Event("Catastrop Nedelin", "V. Botlinko fait exploser une fusée intentionnellement : 120 morts");
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", "Alain");
		JPersoEvent comp = new JPersoEvent(perso1, evt1);

		boolean res =  testComponent("Basic JEventPerso", comp._btn);
		System.out.println("End of testJEventPerso");
		return res;
	}
	boolean testJPersoList(String[] args) {
		Event evt1 = new Event("Catastrop Nedelin", "V. Botlinko fait exploser une fusée intentionnellement : 120 morts");
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", "Alain");
		evt1.addPerso(perso1);
		
		JPanel _persoList = new JPanel();
		JPersoEvent pe;
		for (Map.Entry<Perso, Event.PersoEvent> e : evt1._perso.entrySet()) {
			pe = new JPersoEvent(e.getKey(), evt1);
			_persoList.add(pe._btn);
		}
		_persoList.setVisible(true);
		
		boolean res =  testComponent("TestJPersoList", _persoList);
		System.out.println("End of testJPersoList");
		return res;
	}
	// Afficher Event in GUI
	// Titre
	// Corps
	// Liste <Perso>*
	boolean testJEvent(String[] args) {
		Event evt1 = new Event("Catastrop Nedelin", "V. Botlinko fait exploser une fusée intentionnellement : 120 morts");
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", "Alain");
		evt1.addPerso(perso1);
		JEvent comp = new JEvent( evt1);
		
		boolean res =  testComponent("JEvent", comp);
		System.out.println("End of testJEvent");
		return res;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestGUI app = new TestGUI();
		app.run(args);

	}

}