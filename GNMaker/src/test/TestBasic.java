/**
 * 
 */
package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import data.Event;
import data.Perso;
import data.Story;
import data.converter.PersoConverter;
import data.converter.StoryConverter;

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
		// -------
		nbTest++;
		res = testPersoXML(args);
		if (res) {
			System.out.println("testPersoXML >> " + res);
			nbPassed++;
		} else {
			System.err.println("testPersoXML >> " + res);
		}
		// -------
		nbTest++;
		res = testStoryXML(args);
		if (res) {
			System.out.println("testStoryXML >> " + res);
			nbPassed++;
		} else {
			System.err.println("testStoryXML >> " + res);
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
	// Read and Write Perso to XML
	boolean testPersoXML( String[] args) {
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", "Alain");
		
		XStream xStream = new XStream(new DomDriver());
        xStream.registerConverter(new PersoConverter());
        xStream.alias("perso", Perso.class);
        System.out.println("** Perso to XML **");
        System.out.println(xStream.toXML(perso1));
        
        File outfile = new File("tmp/perso.xml");
        try {
			FileOutputStream writer = new FileOutputStream(outfile);
			xStream.toXML(perso1, writer);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        
        Perso persoRead = (Perso) xStream.fromXML(new File("tmp/perso.xml"));
        System.out.println("** Perso from XML **");
        System.out.println(persoRead.SDump());
        
        return (perso1._name.equals(persoRead._name) && 
        		perso1._player.equals(persoRead._player) &&
        		perso1._zorga.equals(persoRead._zorga));
	}
	boolean testStoryXML(String[] args) {
		Story story = new Story();
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", "Alain");
		Perso perso2 = new Perso("Barbera ERINSKA", "Fanny M", "Alain");
		story._perso.add(perso1);
		story._perso.add(perso2);
		
		System.out.println("** Story to XML **");
		XStream xStream = new XStream(new DomDriver());
		xStream.registerConverter(new StoryConverter());
        xStream.registerConverter(new PersoConverter());
        xStream.alias("story", Story.class);
        System.out.println(xStream.toXML(story));
        
        File outfile = new File("tmp/story.xml");
        try {
			FileOutputStream writer = new FileOutputStream(outfile);
			xStream.toXML(story, writer);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		Story stRead = (Story) xStream.fromXML(new File("tmp/story.xml"));
		System.out.println("** Story from XML **");
        System.out.println(stRead.SDump());
		

		boolean res = true;
		res = res && story.getName().equals(stRead.getName());
		for (int i = 0; i < story._perso.size(); i++) {
			Perso pOri = story._perso.get(i);
			Perso pRead = stRead._perso.get(i);
			res = res && (pOri._name.equals(pRead._name) && 
	        		pOri._player.equals(pRead._player) &&
	        		pOri._zorga.equals(pRead._zorga));
		}
        
		return res;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestBasic app = new TestBasic();
		app.run(args);

	}
}
