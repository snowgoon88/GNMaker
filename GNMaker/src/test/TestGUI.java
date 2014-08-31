/**
 * 
 */
package test;

import gui.JStory;
import gui.PersoListV;
import gui.StoryC;
import gui.ZorgaListV;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import data.ListOf;
import data.Perso;
import data.Story;
import data.Zorga;
import data.converter.PersoConverter;
import data.converter.StoryConverter;
import data.converter.ZorgaConverter;

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
		res = testZorgaV(args);
		if (res) {
			System.out.println("testZorgaV >> " + res);
			nbPassed++;
		} else {
			System.err.println("testZorgaV >> " + res);
		}
		// -------
		nbTest++;
		res = testPersoListV(args);
		if (res) {
			System.out.println("testPersoListV >> " + res);
			nbPassed++;
		} else {
			System.err.println("testPersoListV >> " + res);
		}
//		// -------
//		nbTest++;
//		res = testJEventPerso(args);
//		if (res) {
//			System.out.println("testJEventPerso >> " + res);
//			nbPassed++;
//		} else {
//			System.err.println("testJEventPerso >> " + res);
//		}
//		// -------
//		nbTest++;
//		res = testJEvent(args);
//		if (res) {
//			System.out.println("testJEvent >> " + res);
//			nbPassed++;
//		} else {
//			System.err.println("testJEvent >> " + res);
//		}
//		// -------
//		nbTest++;
//		res = testExpand(args);
//		if (res) {
//			System.out.println("testExpand >> " + res);
//			nbPassed++;
//		} else {
//			System.err.println("testExpand >> " + res);
//		}
//		// -------
//		nbTest++;
//		res = testJPersoEventList(args);
//		if (res) {
//			System.out.println("testJPersoEventList >> " + res);
//			nbPassed++;
//		} else {
//			System.err.println("testJPersoEventList >> " + res);
//		}
		// -------
		nbTest++;
		res = testApplication(args);
		if (res) {
			System.out.println("testApplication >> " + res);
			nbPassed++;
		} else {
			System.err.println("testApplication >> " + res);
		}
//		// -------
//		nbTest++;
//		res = testComboBox(args);
//		if (res) {
//			System.out.println("testComboBox >> " + res);
//			nbPassed++;
//		} else {
//			System.err.println("testComboBox >> " + res);
//		}
		
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
	
	@SuppressWarnings("unused")
	
	/** Viewer de ListOf<Zorga>. Avec Alain et Fab.<br>
	 * 
	 * Permet de vérifier que GUI permet de :
	 * <ul>
	 * <li>Ajout d'un Zorga (bien appuyer sur Enter pour changer nom</li>
	 * <li>Del d'un Zorga.</li>
	 * <li>Modifier le nom d'un Zorga</li>
	 * <li>Effacer tous les Zorgas</li>
	 * </ul>
	 */
	boolean testZorgaV(String[] args) {
		ListOf<Zorga> zorgaList = new ListOf<Zorga>(Zorga.zorgaNull);
		
		// Ajoute Fab
		Zorga zorgaFab = new Zorga("Fab");
		int idFab = zorgaList.add( zorgaFab);
		zorgaList.add(new Zorga("Alain"));
		
		ZorgaListV comp = new ZorgaListV(zorgaList);
		
		boolean res =  testComponent("testZorgaV", comp);
		System.out.println("End of testZorgaV");
		return res;
	}
	/** Viewer de ListOf<Perso> et ListOf<Zorga>.<br>
	 * 
	 * Permet de vérifier pour les Perso :
	 * <ul>
	 * <li>Ajout/Del d'un Perso</li>
	 * <li>Ajout/Del d'un Zorga</li>
	 * <li>Modif du Zorga</li>
	 * <li>Effacer tous les Perso</li>
	 * </ul>
	 * 
	 * @todo largeur du champ 'selecteur de Zorga'
	 */
	boolean testPersoListV(String[] args) {
		ListOf<Zorga> zorgaList = new ListOf<Zorga>(Zorga.zorgaNull);
		Zorga zorgAlain = new Zorga("Alain");
		zorgaList.add(zorgAlain);
		
		ListOf<Perso> persoList = new ListOf<Perso>();
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", zorgAlain);
		Perso perso2 = new Perso("Barbera ERINSKA", "Fanny M", zorgAlain);
		persoList.add(perso1);
		persoList.add(perso2);
		
		JPanel mainPanel = new JPanel();
		ZorgaListV zorgaPanel = new ZorgaListV(zorgaList);
		PersoListV persoPanel = new PersoListV(persoList, zorgaList);
		mainPanel.add( zorgaPanel);
		mainPanel.add( persoPanel);
		boolean res =  testComponent("testPersoListV", mainPanel);
		System.out.println("End of testPersoListV");
		return res;
	}
	
	
//	// Afficher Perso : Bouton Nom
//	//               click gauche => switch status
//	//               click droit => popup avec delete (+ confirm) ou change status
//	//               et info.
//	boolean testJPersoEvent(String[] args) {
//		Zorgas zorgas = new Zorgas();
//		int idAlain = zorgas.add("Alain");
//		
//		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", zorgas, idAlain);
//		
//		Event evt1 = new Event(null,
//				"Catastrop Nedelin", "V. Botlinko fait exploser une fusée intentionnellement : 120 morts");
//		
//		JPersoEvent comp = new JPersoEvent(perso1, evt1);
//
//		boolean res =  testComponent("Basic JEventPerso", comp);
//		System.out.println("End of testJEventPerso");
//		return res;
//	}
//	boolean testJPersoEventList(String[] args) {
//		Zorgas zorgas = new Zorgas();
//		int idAlain = zorgas.add("Alain");
//		
//		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", zorgas, idAlain);
//		
//		Event evt1 = new Event(null,
//				"Catastrop Nedelin", "V. Botlinko fait exploser une fusée intentionnellement : 120 morts");
//		evt1.addPerso(perso1);
//		evt1._persoMap.get(perso1).setDesc("Dans le but de destabiliser Korolev, Botlinko sabote le système de guidage d'un fusée. Mais le nouvel ergol est trop instable et la fusée explose.\nLe bilan est de 120 morts.");
//		Perso perso2 = new Perso("Barbera ERINSKA", "Fanny M", zorgas, idAlain);
//		evt1.addPerso(perso2);
//		
//		JPersoEventList persoList = new JPersoEventList( evt1 );
//		
//		boolean res = testComponent( "TestJPersoEventList", persoList._component);
//		System.out.println("End of TestJPersoEventList");
//		return res;
//	}
//	// Afficher Event in GUI
//	// Titre
//	// Corps
//	// Liste <Perso>*
//	boolean testJEvent(String[] args) {
//		Zorgas zorgas = new Zorgas();
//		int idAlain = zorgas.add("Alain");
//		
//		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", zorgas, idAlain);
//		
//		Event evt1 = new Event(null,
//				"Catastrop Nedelin", "V. Botlinko fait exploser une fusée intentionnellement : 120 morts");
//		evt1.addPerso(perso1);
//		evt1._persoMap.get(perso1).setDesc("Dans le but de destabiliser Korolev, Botlinko sabote le système de guidage d'un fusée. Mais le nouvel ergol est trop instable et la fusée explose.\nLe bilan est de 120 morts.");
//		Perso perso2 = new Perso("Barbera ERINSKA", "Fanny M", zorgas, idAlain);
//		evt1.addPerso(perso2);
//		JEvent comp = new JEvent( evt1);
//		
//		boolean res =  testComponent("JEvent", comp);
//		System.out.println("End of testJEvent");
//		return res;
//	}
//	boolean testExpand(String[] args) {
//		Expandable truc = new Expandable();
//		truc.buildMIG();
//		
//		boolean res =  testComponent("JEvent", truc._main);
//		System.out.println("End of testExpand");
//		return res;
//	}
	boolean testApplication( String[] args ) {
		// Story
		XStream xStream = new XStream(new DomDriver());
		xStream.registerConverter(new StoryConverter());
        xStream.registerConverter(new PersoConverter());
        xStream.registerConverter(new ZorgaConverter());
        xStream.alias("story", Story.class);
        
        File storyFile = new File("tmp/story_test.xml");
		Story story = (Story) xStream.fromXML( storyFile );
		System.out.println("** Story from XML **");
        System.out.println(story.sDump());
        
        
        // Tabbed Panel
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        
		// Main Panel
		JPanel mainP = new JPanel( new BorderLayout());
		JStory comp = new JStory(story);
		story.addObserver(comp);
        JScrollPane storyScroll = new JScrollPane(comp);
        mainP.add( storyScroll, BorderLayout.CENTER);
        StoryC storyControler = new StoryC(story, storyFile);
        mainP.add( storyControler._component, BorderLayout.NORTH);
        tabbedPane.addTab("Intrigue", mainP);
        
        PersoListV persoP = new PersoListV(story._persoList, story._zorgaList);
        tabbedPane.addTab("Perso", persoP);
        
        ZorgaListV zorgaP = new ZorgaListV(story._zorgaList);
        tabbedPane.addTab("Zorga", zorgaP);
        
        boolean res =  testComponent("GNMaker", tabbedPane);
		System.out.println("End of testApplication");
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
		} 
		catch (InvocationTargetException e) {
			e.printStackTrace();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Set up a simple configuration that logs on the console.
		// v1.2 -Dlog4j.configuration=log/log4j1.2.xml
		// v2   -Dlog4j.configurationFile=log/log4j2.xml
		
		TestGUI app = new TestGUI();
		app.run(args);

	}

}
