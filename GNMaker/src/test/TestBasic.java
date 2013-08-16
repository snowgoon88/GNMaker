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
import data.Zorgas;
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
		// -------
		nbTest++;
		res = testZorga(args);
		if (res) {
			System.out.println("testZorga >> " + res);
			nbPassed++;
		} else {
			System.err.println("testZorga >> " + res);
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
		Event evt1 = new Event(null,
				"Catastrop Nedelin", "V. Botlinko fait exploser une fusée intentionnellement : 120 morts");
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
        
        return (perso1.getName().equals(persoRead.getName()) && 
        		perso1.getPlayer().equals(persoRead.getPlayer()) &&
        		perso1.getZorga().equals(persoRead.getZorga()));
	}
	// Save and Load, then compare.
	boolean testStoryXML(String[] args) {
		Story story = new Story();
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", "Alain");
		Perso perso2 = new Perso("Barbera ERINSKA", "Fanny M", "Alain");
		story._perso.add(perso1);
		story._perso.add(perso2);
		Event evt1 = new Event(story,
				"Catastrophe Nedelin", "V. Botlinko fait exploser une fusée intentionnellement : 120 morts");
		evt1.addPerso(perso1);
		evt1._perso.get(perso1).setDesc("Dans le but de destabiliser Korolev, Botlinko sabote le système de guidage d'un fusée. Mais le nouvel ergol est trop instable et la fusée explose.\nLe bilan est de 120 morts.");
		evt1.addPerso(perso2);
		story.add(evt1);
		Event evt2 = new Event(story,
				"Visite impromtue", "B. Erinska revoit sa \"fantômette\" (O. Petrequin).");
		evt2.addPerso(perso2);
		evt2._perso.get(perso2).setDesc("Une silhouette féminine surgit de la nuit, freinée par son propulseur individuel.");
		story.add(evt2);
		
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
		// Perso
		res = res && (story._perso.size() == stRead._perso.size());
		for (int i = 0; i < story._perso.size(); i++) {
			Perso pOri = story._perso.get(i);
			Perso pRead = stRead._perso.get(i);
			res = res && (pOri.getName().equals(pRead.getName()) && 
	        		pOri.getPlayer().equals(pRead.getPlayer()) &&
	        		pOri.getZorga().equals(pRead.getZorga()));
		}
		// Event
		res = res && (story._story.size() == stRead._story.size());
		for (int i = 0; i < story._story.size(); i++) {
			Event eOri = story._story.get(i);
			Event eRead = stRead._story.get(i);
			res = res && (eOri.getTitle().equals(eRead.getTitle()) &&
					eOri.getBody().equals(eRead.getBody()));
			// PersoEvent
			res = res && (eOri._perso.size() == eRead._perso.size());
			for (Perso p : eOri._perso.keySet()) {
				Event.PersoEvent peOri = eOri._perso.get(p);
				// Trouver le pers correspondant dans stRead
				Perso pRead = stRead._perso.get(story._perso.indexOf(p));
				Event.PersoEvent peRead = eRead._perso.get(pRead);
				res = res && (peOri._perso.getName().equals(peRead._perso.getName()) &&
						peOri._status == peRead._status &&
						peOri.getDesc().equals(peRead.getDesc()));
			}
		}
		return res;
	}
	// Teste la liste d'Orga.
	boolean testZorga(String[] args ) {
		boolean res = true;
		// Création
		Zorgas zorg = new Zorgas();
		res = res && (zorg.size() == 0);
		if (res==false) {
			System.err.println("testZorga : size<>0 at creation");
			System.err.println(zorg.SDump());
			return res;
		}
		
		// Ajoute Fab
		res = res && zorg.add("Fab");
		if (res==false) {
			System.err.println("testZorga : Cannot add Fab");
			System.err.println(zorg.SDump());
			return res;
		}
		res = res && (zorg.size() == 1);
		if (res==false) {
			System.err.println("testZorga : size<>1 after one addition");
			System.err.println(zorg.SDump());
			return res;
		}
		// Ré-Ajoute Fab
		res = res && !zorg.add("Fab");
		if (res==false) {
			System.err.println("testZorga : Added Fab twice");
			System.err.println(zorg.SDump());
			return res;
		}
		res = res && (zorg.size() == 1);
		if (res==false) {
			System.err.println("testZorga : size<>1 after one readdition");
			System.err.println(zorg.SDump());
			return res;
		}
		
		// Ajoute alain
		res = res && zorg.add("alain");
		if (res==false) {
			System.err.println("testZorga : Cannot add alain");
			System.err.println(zorg.SDump());
			return res;
		}
		res = res && (zorg.size() == 2);
		if (res==false) {
			System.err.println("testZorga : size<>2 after two addition");
			System.err.println(zorg.SDump());
			return res;
		}
		// Should be first
		res = res && (zorg.get(0).equals("Alain"));
		if (res==false) {
			System.err.println("testZorga : Alain not added first or not as Alain");
			System.err.println(zorg.SDump());
			return res;
		}
		// Ré-Ajoute ALAIN
		res = res && !zorg.add("ALAIN");
		if (res==false) {
			System.err.println("testZorga : Added Alain twice");
			System.err.println(zorg.SDump());
			return res;
		}
		res = res && (zorg.size() == 2);
		if (res==false) {
			System.err.println("testZorga : size<>2 after two readdition");
			System.err.println(zorg.SDump());
			return res;
		}
		
		System.out.println("****** Zorgas ******\n"+zorg.SDump());
		
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
