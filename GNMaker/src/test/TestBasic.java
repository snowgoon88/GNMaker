/**
 * 
 */
package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import data.Event;
import data.ListOf;
import data.Perso;
import data.Story;
import data.Zorga;
import data.converter.PersoConverter;
import data.converter.StoryConverter;
import data.converter.ZorgaConverter;

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
		res = testZorga(args);
		if (res) {
			System.out.println("testZorga >> " + res);
			nbPassed++;
		} else {
			System.err.println("testZorga >> " + res);
		}
		// -------
		nbTest++;
		res = testPerso(args);
		if (res) {
			System.out.println("testPerso >> " + res);
			nbPassed++;
		} else {
			System.err.println("testPerso >> " + res);
		}
		// -------
		nbTest++;
		res = testCreationEvent(args);
		if (res) {
			System.out.println("testCreationEvent >> " + res);
			nbPassed++;
		} else {
			System.err.println("testCreationEvent >> " + res);
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
		res = testStoryWithDelete(args);
		if (res) {
			System.out.println("testStoryWithDelete >> " + res);
			nbPassed++;
		} else {
			System.err.println("testStoryWithDelete >> " + res);
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
	boolean testZorga(String[] args) {
		boolean res = true;
		ListOf<Zorga> zorgas = new ListOf<Zorga>(Zorga.zorgaNull);
		
		// Ajoute Fab
		Zorga zorgaFab = new Zorga("Fab");
		int idFab = zorgas.add( zorgaFab);
		res = res && ( idFab >= 0);
		if (res==false) {
			System.err.println("testZorga : Cannot add Fab");
			System.err.println(zorgas.sDump());
			return res;
		}
		int index = zorgas.indexOf(zorgaFab);
		res = res && (index == idFab);
		if (res==false) {
			System.err.println("testZorga : id of Fab="+idFab+" diff from indexOf="+index);
			System.err.println(zorgas.sDump());
			return res;
		}
		
		// Ajoute Fab
		res = res && (zorgas.add(new Zorga("Alain")) >= 0);
		if (res==false) {
			System.err.println("testZorga : Cannot add Alain");
			System.err.println(zorgas.sDump());
			return res;
		}
		index = zorgas.indexOf(zorgaFab);
		res = res && (index == idFab);
		if (res==false) {
			System.err.println("testZorga : id of Fab="+idFab+" diff from indexOf="+index);
			System.err.println(zorgas.sDump());
			return res;
		}
		
		// Remove Fab
		res = res && (zorgas.remove(idFab).equals(zorgaFab));
		if (res==false) {
			System.err.println("testZorga : Fab cannot be removed");
			System.err.println(zorgas.sDump());
			return res;
		}
		index = zorgas.indexOf(zorgaFab);
		res = res && (index == -1);
		if (res==false) {
			System.err.println("testZorga : Fab still here, with index="+index+", idFab="+idFab);
			System.err.println(zorgas.sDump());
			return res;
		}
		res = res && (zorgas.remove(idFab) == null);
		if (res==false) {
			System.err.println("testZorga : Fab seems to be removed twice");
			System.err.println(zorgas.sDump());
			return res;
		}
		
		//on peut modifier un Zorga, directement.
		
		return res;
	}
	
	
	boolean testPerso(String[] args) {
		boolean res = true;
		ListOf<Zorga> zorgas = new ListOf<Zorga>(Zorga.zorgaNull);
		Zorga zorgAlain = new Zorga("Alain");
		zorgas.add( new Zorga("Alain"));
		
		ListOf<Perso> persos = new ListOf<Perso>(Perso.persoNull);
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", zorgAlain);
		persos.add(perso1);
		System.out.println(persos.sDump());
		
		int idPerso1 = perso1.getId();
		Perso persoGet = persos.get(idPerso1);
		res = res && (perso1 == persoGet);
		if (res==false) {
			System.err.println("testPerso : id de perso1="+perso1.getId()+" permet de récupérer");
			System.err.println(persoGet.sDump());
			System.err.println(zorgas.sDump());
			System.err.println(persos.sDump());
			return res;
		}
		
		res = res && (zorgAlain == persoGet.getZorga());
		if (res==false) {
			System.err.println("zorga de persoGet="+persoGet.getZorga().sDump());
			System.err.println("est différent de "+zorgAlain.sDump());
			System.err.println(zorgas.sDump());
			System.err.println(persos.sDump());
			return res;
		}
		
		return res;
	}
	boolean testCreationEvent(String[] args) {
		ListOf<Zorga> zorgas = new ListOf<Zorga>(Zorga.zorgaNull);
		Zorga zorgAlain = new Zorga("Alain");
		zorgas.add( new Zorga("Alain"));
		
		System.out.println("** Event sans Perso **");
		Event evt1 = new Event(null,
				"Catastrop Nedelin", "V. Botlinko fait exploser une fusée intentionnellement : 120 morts");
		System.out.println(evt1.sDump());
		
		System.out.println("** Event avec ValeriB **");
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", zorgAlain);
		// First time 
		evt1.addPerso(perso1);
		System.out.println(evt1.sDump());
		
		System.out.println("** Event avec 2xValeriB **");
		evt1.addPerso(perso1);
		System.out.println(evt1.sDump());
		
		System.out.println("** Valeri To OK **");
		evt1.setStatusPerso(perso1, true);
		if (evt1.getStatusPerso(perso1) != true) {
			System.err.println("testCreationEvent : ValeriB status SHOULD be true");
			return false;
		}
		System.out.println(evt1.sDump());
		
		return true;
	}
//	// Read and Write Perso to XML
//	boolean testPersoXML( String[] args) {
//		Zorgas zorgas = new Zorgas();
//		Zorga zorgAlain = new Zorga("Alain");
////		int idAlain = zorgas.add("Alain");
//		
//		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", zorgAlain);
//		
//		XStream xStream = new XStream(new DomDriver());
//        xStream.registerConverter(new PersoConverter());
//        xStream.alias("perso", Perso.class);
//        System.out.println("** Perso to XML **");
//        System.out.println(xStream.toXML(perso1));
//        
//        File outfile = new File("tmp/perso.xml");
//        try {
//			FileOutputStream writer = new FileOutputStream(outfile);
//			xStream.toXML(perso1, writer);
//			writer.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//        
//        
//        Perso persoRead = (Perso) xStream.fromXML(new File("tmp/perso.xml"));
//        persoRead.setZorgaList(zorgas);
//        System.out.println("** Perso from XML **");
//        System.out.println(persoRead.SDump());
//        
//        return (perso1.getName().equals(persoRead.getName()) && 
//        		perso1.getPlayer().equals(persoRead.getPlayer()) &&
//        		perso1.getZorga().equals(persoRead.getZorga()));
//	}
	// Save and Load, then compare.
	boolean testStoryXML(String[] args) {
		Story story = new Story();
		Zorga zorgAlain = new Zorga("Alain");
		story._zorgaList.add(zorgAlain);
		
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", zorgAlain);
		Perso perso2 = new Perso("Barbera ERINSKA", "Fanny M", zorgAlain);
		story._persoList.add(perso1);
		story._persoList.add(perso2);
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
		System.out.println("** Created **");
		System.out.println(story.sDump());
		
		System.out.println("** Story to XML **");
		XStream xStream = new XStream(new DomDriver());
		xStream.registerConverter(new StoryConverter());
        xStream.registerConverter(new PersoConverter());
        xStream.registerConverter(new ZorgaConverter());
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
        System.out.println(stRead.sDump());
		

		boolean res = true;
		res = res && story.getName().equals(stRead.getName());
		if (res==false) {
			System.err.println("Nom de Story différents");
			System.err.println("story = "+story.getName()+" read="+stRead.getName());
			return res;
		}
		// Perso
		//res = res && (story._perso.size() == stRead._perso.size());
		for (Entry<Integer, Perso> entry : story._persoList.entrySet()) {
			Perso pOri = entry.getValue();
			Perso pRead = stRead._persoList.get(entry.getKey());
			res = res && (pOri.getName().equals(pRead.getName()) && 
	        		pOri.getPlayer().equals(pRead.getPlayer()) &&
	        		pOri.getZorga().getName().equals(pRead.getZorga().getName()));
			if (res==false) {
				System.err.println("Pb de Perso");
				System.err.println("story id= "+pOri.getId()+" read="+pRead.getId());
				System.err.println("Story "+pOri.sDump());
				System.err.println("Read "+pRead.sDump());
				return res;
			}
		}
		// Event
		res = res && (story._story.size() == stRead._story.size());
		for (int i = 0; i < story._story.size(); i++) {
			Event eOri = story._story.get(i);
			Event eRead = stRead._story.get(i);
			res = res && (eOri.getTitle().equals(eRead.getTitle()) &&
					eOri.getBody().equals(eRead.getBody()));
			if (res==false) {
				System.err.println("Pb avec Titre ou Body Event="+i);
				System.err.println("Story evt="+eOri.sDump());
				System.err.println("Story evt="+eRead.sDump());
				return res;
			}
			// PersoEvent
			res = res && (eOri._perso.size() == eRead._perso.size());
			if (res==false) {
				System.err.println("Pb avec NbPerso Event="+i);
				System.err.println("Story evt="+eOri._perso.size());
				System.err.println("Story evt="+eRead._perso.size());
				return res;
			}
			for (Perso p : eOri._perso.keySet()) {
				Event.PersoEvent peOri = eOri._perso.get(p);
				// Trouver le pers correspondant dans stRead
				Perso pRead = stRead._persoList.get(p.getId());
				Event.PersoEvent peRead = eRead._perso.get(pRead);
				res = res && (peOri._perso.getName().equals(peRead._perso.getName()) &&
						peOri._status == peRead._status &&
						peOri.getDesc().equals(peRead.getDesc()));
				if (res==false) {
					System.err.println("Pb avec Perso dans Event="+i);
					System.err.println("Story evt="+peOri._perso.sDump());
					System.err.println("Story evt="+pRead.sDump());
					return res;
				}
			}
		}
		return res;
	}
	boolean testStoryWithDelete(String[] args ) {
		Story story = new Story();
		Zorga zorgAlain = new Zorga("Alain");
		Zorga zorgFab = new Zorga("Fab");
		story._zorgaList.add(zorgAlain);
		story._zorgaList.add(zorgFab);
		
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", zorgAlain);
		Perso perso2 = new Perso("Barbera ERINSKA", "Fanny M", zorgFab);
		story._persoList.add(perso1);
		story._persoList.add(perso2);
		
		// if I delete zorgFab, alors doit affecter perso2 mais pas perso1
		story._zorgaList.remove(zorgFab.getId());
		
		boolean res = true;
		// ne doit plus référencer zorgaFab
		res = res && (perso2.getZorga().getId() != zorgFab.getId());
		if (res == false) {
			System.err.println("Pb with zorga deleted in Perso2, still zorgaFab");
			System.err.println("perso2 ="+perso2.sDump());
			System.err.println("zorgaFab="+zorgFab.sDump()+" id="+zorgFab.getId());
			System.err.println("ListOfZorga = "+story._zorgaList.sDump());
			return res;
		}
		// doit référencer zorgaNull
		res = res && (perso2.getZorga().equals(Zorga.zorgaNull));
		if (res == false) {
			System.err.println("Pb with zorga deleted in Perso2, not zorgaNull");
			System.err.println("perso2 ="+perso2.sDump());
			System.err.println("ListOfZorga = "+story._zorgaList.sDump());
			return res;
		}
		return res;
	}
//	
//	boolean testZorga2Perso(String[] args) {
//		System.out.println("****** Zorga2Perso ******");
//		Zorgas zorgas = new Zorgas();
//		int idAlain = zorgas.add("Alain");
//		
//		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", zorgas, idAlain);
//		
//		zorgas.set( idAlain, "AlainChef");
//		boolean res = perso1.getZorga().equals("AlainChef");
//		if (res==false) {
//			System.err.println("testZorga2Perso : Alain not modified");
//			System.err.println(perso1.SDump());
//			System.err.println(zorgas.SDump());
//			return res;
//		}
//		return true;
//	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestBasic app = new TestBasic();
		app.run(args);

	}
}
