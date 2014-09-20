/**
 * 
 */
package demo;

import gui.EventListV;
import gui.PersoListV;
import gui.StoryC;
import gui.ZorgaListV;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import data.Story;
import data.converter.PersoConverter;
import data.converter.StoryConverter;
import data.converter.ZorgaConverter;

/**
 * Toute première version démo de GNMaker
 * 
 * @versin 0.01
 * @author snowgoon88
 */
public class GNMaker {

	/** Fenêtre Principale */
	JFrame _mainFrame;
	
	/** Story */
	Story _story;
	
	/** Fichier de Story */
	File _storyFile;
	
	public GNMaker() {
		// Load default Story
		// Story
		XStream xStream = new XStream(new DomDriver());
		xStream.registerConverter(new StoryConverter());
		xStream.registerConverter(new PersoConverter());
		xStream.registerConverter(new ZorgaConverter());
		xStream.alias("story", Story.class);

		_storyFile = new File("src/demo/story_test.xml");
		_story = (Story) xStream.fromXML( _storyFile );
		System.out.println("** Story from XML **");
		System.out.println(_story.sDump());
		
		buildGUI();
	}
	
	void buildGUI() {
		_mainFrame = new JFrame("GNMaker - v0.01");
		
		// Different Tabbed Panes
		// Tabbed Panel
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        
		// Main Panel
		JPanel mainP = new JPanel( new BorderLayout());
		EventListV comp = new EventListV(_story._story);
		//story.addObserver(comp);
        JScrollPane storyScroll = new JScrollPane(comp);
        mainP.add( storyScroll, BorderLayout.CENTER);
        
        StoryC storyControler = new StoryC(_story, _storyFile);
        mainP.add( storyControler._component, BorderLayout.NORTH);
        tabbedPane.addTab("Intrigue", mainP);
        
        PersoListV persoP = new PersoListV(_story._persoList, _story._zorgaList);
        JScrollPane persoScroll = new JScrollPane(persoP);
        tabbedPane.addTab("Perso", persoScroll);
        
        ZorgaListV zorgaP = new ZorgaListV(_story._zorgaList);
        JScrollPane zorgaScroll = new JScrollPane(zorgaP);
        tabbedPane.addTab("Zorga", zorgaScroll);
        
        _mainFrame.add( tabbedPane );
        _mainFrame.pack();
	}
	
	public void run() {
		_mainFrame.setVisible(true);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// v2   -Dlog4j.configurationFile=log/log4j2.xml

		GNMaker app = new GNMaker();
		app.run();

	}

}
