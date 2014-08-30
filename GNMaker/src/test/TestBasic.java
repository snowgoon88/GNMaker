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
		// -------
		nbTest++;
		res = testStoryWithDeleteInEvent(args);
		if (res) {
			System.out.println("testtestStoryWithDeleteInEvent >> " + res);
			nbPassed++;
		} else {
			System.err.println("testtestStoryWithDeleteInEvent >> " + res);
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
	/**
	 * Test effectués en vérifiant cohérence id, présence élément nul.
	 * <li> Ajoute Fab</li>
	 * <li> Ajoute Alain</li>
	 * <li> Remove Fab</li>
	 * <li> Ajoute Un, Deux et Trois. Puis clear()</li>
	 */
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
		
		// Ajoute Alain
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
		
		// Add some dummies then clear.
		zorgas.add( new Zorga("Un"));
		zorgas.add( new Zorga("Deux"));
		zorgas.add( new Zorga("Trois"));
		zorgas.clear();
		// il doit rester -1
		res = (zorgas.get(-1).equals(Zorga.zorgaNull));
		if (res==false) {
			System.err.println("testZorga : clear removed elem '-1'");
			System.err.println(zorgas.sDump());
			return res;
		}
		
		//on peut modifier un Zorga, directement.
		
		return res;
	}
	
	/**
	 * Test effectué en vérifiant que le lien Zorga/Perso est cohérent, et que les id sont ok.
	 * <li> ListOf<Zorga> avec Alain</li>
	 * <li> ListOf<Perso> avec Valeri BOTLINKO pour Alain</li>
	 */
	boolean testPerso(String[] args) {
		boolean res = true;
		// Liste de Zorga avec Alain et, par défaut, zorgaNull).
		ListOf<Zorga> zorgas = new ListOf<Zorga>(Zorga.zorgaNull);
		Zorga zorgAlain = new Zorga("Alain");
		zorgas.add( new Zorga("Alain"));
		
		// Liste de Perso avec Valeri Botlinko pour Alain
		ListOf<Perso> persos = new ListOf<Perso>();
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
	
	/**
	 * Test création d'une Story, Save, puis Load, puis Compare.
	 * La story est créée avec 
	 * <li>ListOf<Zorga> : Alain</li>
	 * <li>ListOf<Perso> : V Botlinko (Alain), B Erinska (Alain)</li>
	 * <li>Evenements:<ul>
	 *     <li> Catastrophe Nedelin (Botlinko, Erinska)</li>
	 *     <li> Visite impromtue (Erinska)</li>
	 * </ul></li>
	 * Test
	 * <li>print to dump</li>
	 * <li>Save to File("tmp/story.xml")</li>
	 * <li>Read from File("tmp/story.xml")</li>
	 * <li>Test si même info</li>
	 */
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
//		evt1._persoMap.get(perso1).setDesc("Dans le but de destabiliser Korolev, Botlinko sabote le système de guidage d'un fusée. Mais le nouvel ergol est trop instable et la fusée explose.\nLe bilan est de 120 morts.");
		evt1._listPE.get(perso1.getId()).setDesc("Dans le but de destabiliser Korolev, Botlinko sabote le système de guidage d'un fusée. Mais le nouvel ergol est trop instable et la fusée explose.\nLe bilan est de 120 morts.");
		evt1.addPerso(perso2);
		story.add(evt1);
		Event evt2 = new Event(story,
				"Visite impromtue", "B. Erinska revoit sa \"fantômette\" (O. Petrequin).");
		evt2.addPerso(perso2);
//		evt2._persoMap.get(perso2).setDesc("Une silhouette féminine surgit de la nuit, freinée par son propulseur individuel.");
		evt2._listPE.get(perso2.getId()).setDesc("Une silhouette féminine surgit de la nuit, freinée par son propulseur individuel.");
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
//			res = res && (eOri._persoMap.size() == eRead._persoMap.size());
			res = res && (eOri._listPE.entrySet().size() == eRead._listPE.entrySet().size());
			if (res==false) {
				System.err.println("Pb avec NbPerso Event="+i);
				System.err.println("Story evt="+eOri._listPE.entrySet().size());
				System.err.println("Read evt="+eRead._listPE.entrySet().size());
				return res;
			}
			for (Entry<Integer, Event.PersoEvent> entry : eOri._listPE.entrySet()) {
				Event.PersoEvent peOri = entry.getValue();
				// Trouver le pe correspondantg dans stRead
				Event.PersoEvent peRead = eRead._listPE.get(peOri.getId());
				res = res && (peOri.getStatus() == peRead.getStatus() &&
						peOri.getDesc().equals(peRead.getDesc()));
				if (res==false) {
					System.err.println("Pb avec PersoEvent="+i);
					System.err.println("Story evt="+peOri.sDump());
					System.err.println("Read evt="+peRead.sDump());
					return res;
				}
			}
		}
		return res;
	}
	
	/**
	 * Test Story avec association Perso/Zorga.
	 * La story est créée avec 
	 * <li>ListOf<Zorga> : Alain, Fab</li>
	 * <li>ListOf<Perso> : V Botlinko (Alain), B Erinska (Fab)</li>
	 *
	 * Test
	 * <li> Remove Fab : plus d'orga pour Erinska</li>
	 */
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
	
	/**
	 * Test Story avec association Perso/Zorga.
	 * La story est créée avec 
	 * <li>ListOf<Zorga> : Alain, Fab</li>
	 * <li>ListOf<Perso> : V Botlinko (Alain), B Erinska (Fab)</li>
	 * <li>Evenements:<ul>
	 *     <li> Event1 : Catastrophe Nedelin (Botlinko, Erinska)</li>
	 *     <li> Event2 : Visite impromtue (Erinska)</li>
	 * </ul></li>
	 *
	 * Test
	 * <li> Remove Botlinko : il ne reste que Erinska dans Event1</li>
	 * <li> Event2 pas affecté/li>
	 */
	boolean testStoryWithDeleteInEvent(String[] args) {
		Story story = new Story();
		Zorga zorgAlain = new Zorga("Alain");
		Zorga zorgFab = new Zorga("Fab");
		story._zorgaList.add(zorgAlain);
		story._zorgaList.add(zorgFab);
		
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", zorgAlain);
		Perso perso2 = new Perso("Barbera ERINSKA", "Fanny M", zorgFab);
		story._persoList.add(perso1);
		story._persoList.add(perso2);
		
		Event evt1 = new Event(story,
				"Catastrophe Nedelin", "V. Botlinko fait exploser une fusée intentionnellement : 120 morts");
		evt1.addPerso(perso1);
//		evt1._persoMap.get(perso1).setDesc("Dans le but de destabiliser Korolev, Botlinko sabote le système de guidage d'un fusée. Mais le nouvel ergol est trop instable et la fusée explose.\nLe bilan est de 120 morts.");
		evt1._listPE.get(perso1.getId()).setDesc("Dans le but de destabiliser Korolev, Botlinko sabote le système de guidage d'un fusée. Mais le nouvel ergol est trop instable et la fusée explose.\nLe bilan est de 120 morts.");
		evt1.addPerso(perso2);
		story.add(evt1);
		Event evt2 = new Event(story,
				"Visite impromtue", "B. Erinska revoit sa \"fantômette\" (O. Petrequin).");
		evt2.addPerso(perso2);
//		evt2._persoMap.get(perso2).setDesc("Une silhouette féminine surgit de la nuit, freinée par son propulseur individuel.");
		evt2._listPE.get(perso2.getId()).setDesc("Une silhouette féminine surgit de la nuit, freinée par son propulseur individuel.");
		story.add(evt2);
		
		// Delete perso1, should affect Events
		story._persoList.remove(perso1.getId());
		// Only 1 perso in evt1
		boolean res = (evt1._listPE.entrySet().size() == 1); // 1 PersoEvent
		if (res == false) {
			System.err.println("Pb with perso1 deleted in Event1 : still 2 perso");
			System.err.println("evt1 ="+evt1.sDump());
			return res;
		}
		// Should not be perso1
		res = (evt1._listPE.get(perso1.getId()) == null);
		if (res == false) {
			System.err.println("Pb with perso1 deleted in Event1 : still has "+perso1.getName());
			System.err.println("evt1 ="+evt1.sDump());
			return res;
		}
		// evt2 not touched
		res = (evt2._listPE.entrySet().size() == 1); // 1 PersoEvent
		if (res == false) {
			System.err.println("Pb with perso1 deleted in Event2 : not 1 perso");
			System.err.println("evt2 ="+evt1.sDump());
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
