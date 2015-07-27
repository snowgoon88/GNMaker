/**
 * 
 */
package editor;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import editor.example.JDocEditor;
import editor.example.TextSamplerDemo;

/**
 * Test un JEditorPane ou JTextPane.
 * 
 * @author snowgoon88@gmail.com
 */
public class TestEditor {

	JDialog _testDialog;
	private String newline = "\n";
	protected static final String buttonString = "JButton";
	
	boolean testJTextPane(String[] args) {
		//Create a text pane.
        JTextPane textPane = createTextPane();
        boolean res =  testComponent("JTextPane", textPane);
        
        System.out.println("End of testJTextPane");
		return res;
	}
	
	boolean testJDocEditor(String[] args) {
		// Create the JTextEditor
		JDocEditor docEditor = new JDocEditor();
		boolean res =  testComponent("JDocEditor", docEditor);
		
		System.out.println("End of testJDocEditor");
		return res;
	}
	
	private JTextPane createTextPane() {
        String[] initString =
                { "This is an editable JTextPane, ",            //regular
                  "another ",                                   //italic
                  "styled ",                                    //bold
                  "text ",                                      //small
                  "component, ",                                //large
                  "which supports embedded components..." + newline,//regular
                  "...and embedded icons..." + newline,         //regular
                  " ",                                          //icon
                  newline + "JTextPane is a subclass of JEditorPane that " +
                    "uses a StyledEditorKit and StyledDocument, and provides " +
                    "cover methods for interacting with those objects."
                 };

        String[] initStyles =
                { "regular", "italic", "bold", "small", "large",
                  "regular", "regular", "icon",
                  "regular"
                };

        JTextPane textPane = new JTextPane();
        StyledDocument doc = textPane.getStyledDocument();
        addStylesToDocument(doc);

        try {
            for (int i=0; i < initString.length; i++) {
                doc.insertString(doc.getLength(), initString[i],
                                 doc.getStyle(initStyles[i]));
            }
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text into text pane.");
        }

        return textPane;
    }
	protected void addStylesToDocument(StyledDocument doc) {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");

        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

        s = doc.addStyle("small", regular);
        StyleConstants.setFontSize(s, 10);

        s = doc.addStyle("large", regular);
        StyleConstants.setFontSize(s, 16);

        s = doc.addStyle("icon", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon pigIcon = createImageIcon("images/Pig.gif",
                                            "a cute pig");
        if (pigIcon != null) {
            StyleConstants.setIcon(s, pigIcon);
        }

    }
	/** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path,
                                               String description) {
        java.net.URL imgURL = TextSamplerDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
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
		TestEditor app = new TestEditor();
		//app.testJTextPane(args);
		app.testJDocEditor(args);
	}

}
