/**
 * 
 */
package test;

import data.Event;
import data.Perso;
import data.Story;

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
		// -------
		nbTest++;
		res = testStoryCreation(args);
		if (res) {
			System.out.println("testStoryCreation >> " + res);
			nbPassed++;
		} else {
			System.err.println("testStoryCreation >> " + res);
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
		// First time 
		evt1.addPerso(perso1);
		System.out.println(evt1.SDump());
		
		System.out.println("** Event avec 2xValeriB **");
		evt1.addPerso(perso1);
		System.out.println(evt1.SDump());
		
		System.out.println("** Valeri To OK **");
		evt1.setStatusPerso(perso1, true);
		if (evt1.getStatusPerso(perso1) != true) {
			System.err.println("testCreationEvent : ValeriB status SHOULD be true");
			return false;
		}
		System.out.println(evt1.SDump());
		
		return true;
	}
	boolean testStoryCreation(String[] args) {
		Story hist = new Story();
		System.out.println("***** Basic Story *****");
		System.out.println(hist.toXML());

		System.out.println("***** One Event *****");
		Event evt1 = new Event("Catastrop Nedelin", "V. Botlinko fait exploser une fusée intentionnellement : 120 morts");
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", "Alain");
		evt1.addPerso(perso1);
		evt1._perso.get(perso1)._desc = "Dans le but de destabiliser Korolev, Botlinko sabote le système de guidage d'un fusée. Mais le nouvel ergol est trop instable et la fusée explose.\nLe bilan est de 120 morts.";
		Perso perso2 = new Perso("Barbera ERINSKA", "Fanny M", "Alain");
		evt1.addPerso(perso2);
		hist.add(evt1);
		System.out.println(hist.toXML());

		
		
		
		return true;
	}
	
	// TODO Ds Event, un Perso peut être ok/todo (suivant). 
	// Par défaut, à la création, c'est todo.
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestBasic app = new TestBasic();
		app.run(args);

	}
}
