/**
 * 
 */
package gui;


import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import data.Event;
import data.Story;
import data.converter.PersoConverter;
import data.converter.StoryConverter;

/**
 * @author snowgoon88@gmail.com
 */
public class StoryC {
	/** Story to control */
	Story _story;
	File _storyFile;
	XStream _xStream;
	
	/** FileChooser */
	JFileChooser _jfc;
	
	/** Component where ActionButtons are added */
	public JPanel _component;
	
	/**
	 * Creation d'un Controler avec une Story et un Fichier pour sauver/lire.
	 * @param story 
	 * @param storyFile File to load/save, can be null.
	 */
	public StoryC(Story story, File storyFile) {
		this._story = story;
		this._storyFile = storyFile;
		
		_xStream = new XStream(new DomDriver());
		_xStream.registerConverter(new StoryConverter());
        _xStream.registerConverter(new PersoConverter());
        _xStream.alias("story", Story.class);
		
		_jfc = new JFileChooser(_storyFile);
		_jfc.addChoosableFileFilter(new XMLFileFilter());
		
		buildGUI();
	}
	public void buildGUI() {
		_component = new JPanel();
		
		JButton newEvtBtn = new JButton( new NewEventAction() );
		_component.add(newEvtBtn);
		JButton dumpAllBtn = new JButton( new DumpAllAction() );
		_component.add(dumpAllBtn);
		JButton saveBtn = new JButton( new SaveAction() );
		_component.add(saveBtn);
	}
	
	
	/**
	 * Crée un nouvel Event qu'on ajoute à une Story.
	 */
	@SuppressWarnings("serial")
	public class NewEventAction extends AbstractAction {

		public NewEventAction() {
			super("Nouvel Evt", null);
			putValue(SHORT_DESCRIPTION, "Ajoute un Evénement à une Story.");
			putValue(MNEMONIC_KEY, null);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Event evt = new Event(_story, "Nouvel Evénement", "-A définir-");
			_story.add(evt);
		}
	}
	
	/**
	 * Dump the while story to console.
	 */
	@SuppressWarnings("serial")
	public class DumpAllAction extends AbstractAction {

		public DumpAllAction() {
			super("Dump All", null);
			putValue(SHORT_DESCRIPTION, "Dump the whole story.");
			putValue(MNEMONIC_KEY, null);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("***** DumpALLAction *************");
			System.out.println(_story.sDump());
		}
	}
	/**
	 * Save the Story to a file.
	 */
	@SuppressWarnings("serial")
	public class SaveAction extends AbstractAction {

		public SaveAction() {
			super("Save", null);
			putValue(SHORT_DESCRIPTION, "Sauve toute l'Intrigue.");
			putValue(MNEMONIC_KEY, null);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			_jfc.setSelectedFile(_storyFile);
			int returnVal = _jfc.showSaveDialog(null);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	_storyFile = _jfc.getSelectedFile();
	            System.out.println("Save to "+_storyFile.getAbsolutePath());
	            try {
	    			FileOutputStream writer = new FileOutputStream(_storyFile);
	    			_xStream.toXML(_story, writer);
	    			writer.close();
	    		} catch (FileNotFoundException exc) {
	    			exc.printStackTrace();
	    		} catch (IOException exc) {
	    			exc.printStackTrace();
	    		}
	            
	        } else {
	            System.out.println("Save command cancelled by user.");
	        }
		}
	}
	
	/**
	 * Accept Directory or XML Files.
	 */
	public class XMLFileFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}

			String extension = getExtension(f);
			if (extension != null) {
				if (extension.equals("xml")) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}
		@Override
		public String getDescription() {
			return "Fichier GNMaker";
		}
	}
	/*
	 * Get the extension of a file.
	 */  
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 &&  i < s.length() - 1) {
			ext = s.substring(i+1).toLowerCase();
		}
		return ext;
	}
}
