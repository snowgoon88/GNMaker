/**
 * 
 */
package test;

import gui.JEvent;
import gui.JPersoEvent;
import gui.JPersoEventList;
import gui.JStory;
import gui.StoryC;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import Inspiration.Expandable;

import data.Event;
import data.Perso;
import data.Story;
import data.converter.PersoConverter;
import data.converter.StoryConverter;

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
//		// -------
//		nbTest++;
//		res = testJStory(args);
//		if (res) {
//			System.out.println("testJStory >> " + res);
//			nbPassed++;
//		} else {
//			System.err.println("testJStory >> " + res);
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
	boolean testJPersoEvent(String[] args) {
		Event evt1 = new Event(null,
				"Catastrop Nedelin", "V. Botlinko fait exploser une fusée intentionnellement : 120 morts");
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", "Alain");
		JPersoEvent comp = new JPersoEvent(perso1, evt1);

		boolean res =  testComponent("Basic JEventPerso", comp);
		System.out.println("End of testJEventPerso");
		return res;
	}
	boolean testJPersoEventList(String[] args) {
		Event evt1 = new Event(null,
				"Catastrop Nedelin", "V. Botlinko fait exploser une fusée intentionnellement : 120 morts");
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", "Alain");
		evt1.addPerso(perso1);
		evt1._perso.get(perso1).setDesc("Dans le but de destabiliser Korolev, Botlinko sabote le système de guidage d'un fusée. Mais le nouvel ergol est trop instable et la fusée explose.\nLe bilan est de 120 morts.");
		Perso perso2 = new Perso("Barbera ERINSKA", "Fanny M", "Alain");
		evt1.addPerso(perso2);
		
		JPersoEventList persoList = new JPersoEventList( evt1 );
		
		boolean res = testComponent( "TestJPersoEventList", persoList._component);
		System.out.println("End of TestJPersoEventList");
		return res;
	}
	// Afficher Event in GUI
	// Titre
	// Corps
	// Liste <Perso>*
	boolean testJEvent(String[] args) {
		Event evt1 = new Event(null,
				"Catastrop Nedelin", "V. Botlinko fait exploser une fusée intentionnellement : 120 morts");
		Perso perso1 = new Perso("Valeri BOTLINKO", "Laurent D", "Alain");
		evt1.addPerso(perso1);
		evt1._perso.get(perso1).setDesc("Dans le but de destabiliser Korolev, Botlinko sabote le système de guidage d'un fusée. Mais le nouvel ergol est trop instable et la fusée explose.\nLe bilan est de 120 morts.");
		Perso perso2 = new Perso("Barbera ERINSKA", "Fanny M", "Alain");
		evt1.addPerso(perso2);
		JEvent comp = new JEvent( evt1);
		
		boolean res =  testComponent("JEvent", comp);
		System.out.println("End of testJEvent");
		return res;
	}
	boolean testExpand(String[] args) {
		Expandable truc = new Expandable();
		truc.buildMIG();
		
		boolean res =  testComponent("JEvent", truc._main);
		System.out.println("End of testExpand");
		return res;
	}
	// Read Story from tmp/story_test.xml
	// Display Story
	boolean testJStory( String[] args) {
		XStream xStream = new XStream(new DomDriver());
		xStream.registerConverter(new StoryConverter());
        xStream.registerConverter(new PersoConverter());
        xStream.alias("story", Story.class);
        
		Story story = (Story) xStream.fromXML(new File("tmp/story_test.xml"));
		System.out.println("** Story from XML **");
        System.out.println(story.SDump());
        
        JStory comp = new JStory(story);
//        JScrollPane main = new JScrollPane(comp);
//        main.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		boolean res =  testComponent("JStory", comp);
		System.out.println("End of testJStory");
		return res;
	}
	boolean testApplication( String[] args ) {
		// Story
		XStream xStream = new XStream(new DomDriver());
		xStream.registerConverter(new StoryConverter());
        xStream.registerConverter(new PersoConverter());
        xStream.alias("story", Story.class);
        
		Story story = (Story) xStream.fromXML(new File("tmp/story_test.xml"));
		System.out.println("** Story from XML **");
        System.out.println(story.SDump());
        
        
		// Main Panel
		JPanel mainP = new JPanel( new BorderLayout());
		JStory comp = new JStory(story);
		story.addObserver(comp);
        JScrollPane storyScroll = new JScrollPane(comp);
        mainP.add( storyScroll, BorderLayout.CENTER);
        StoryC storyControler = new StoryC(story);
        mainP.add( storyControler._component, BorderLayout.NORTH);
        
        boolean res =  testComponent("GNMaker", mainP);
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
		} catch (InvocationTargetException | InterruptedException e) {
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
