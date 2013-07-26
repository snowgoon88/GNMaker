/**
 * 
 */
package test;

import data.Event;
import data.Perso;

/**
 * 
 * @author snowgoon88@gmail.com
 */
public class TestBasic {

	public TestBasic() {
		System.out.println("***** TestBasic *****");

	}
		
	public void run(String[] args) {	
		boolean res;
		int nbTest = 0;
		int nbPassed = 0;

		// -------
		nbTest++;
		res = testCreationPerso(args);
		if (res) {
			System.out.println("testCreationPerso >> " + res);
			nbPassed++;
		} else {
			System.err.println("testCreationPerso >> " + res);
		}
		// -------
		nbTest++;
		res = testCreationEvent(args);
		if (res) {
			System.out.println("testtestCreationEvent >> " + res);
			nbPassed++;
		} else {
			System.err.println("testtestCreationEvent >> " + res);
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
	
	boolean testCreationPerso(String[] args) {
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", "Alain");
		System.out.println(perso1.SDump());
		return true;
	}
	boolean testCreationEvent(String[] args) {
		System.out.println("** Event sans Perso **");
		Event evt1 = new Event("Catastrop Nedelin", "V. Botlinko fait exploser une fusée intentionnellement : 120 morts");
		System.out.println(evt1.SDump());
		
		System.out.println("** Event avec ValeriB **");
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", "Alain");
		evt1._perso.add(perso1);
		System.out.println(evt1.SDump());
		
		return true;
	}
	
	// TODO Afficher Event in GUI
	// Titre
	// Corps
	// Liste <Perso>
	
	// TODO Afficher Perso : Bouton Nom 
	// TODO                  (+Popup)
	// TODO                  + Toggle state.
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestBasic app = new TestBasic();
		app.run(args);

	}
}
