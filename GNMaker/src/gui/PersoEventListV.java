/**
 * 
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import utils.GraphicHelper;

import net.miginfocom.swing.MigLayout;

import data.Event;
import data.Event.PersoEvent;
import data.Perso;

/**
 * Un JPanel pour lister les différents PersoEvent. Viewer de (MVC).<br>
 * 
 * Est composé de :
 * <ul>
 * <li> JBouton pour expand/reduce</li>
 * <li> _persoPanel (HBox) de PersoEventV où _leftClick fait expand/reduce de la desc associée</li>
 * <li> _descPanel (VBox) de desc (TextArea) </li>
 * </ul>
 * 
 * Traite les messages suivants :
 * <ul>
 * <li> ListOf<PersoEvent>.id_add : ajoute dans _persoPanel et _descPanel </li>
 * <li> ListOf<PersoEvent>.id_del : enlève de _persoPanel et _descPanel </li>
 * </ul>
 * 
 * Display
 * Add a new PersoEvent
 * Remove a given PersoEvent
 * Expand a PersoEvent (LeftClick on JPersoEvent)
 * Expand All
 * 
 * @todo Les textes des TextArea ne sont pas sauvé dans le modèle !!
 * 
 * @author snowgoon88@gmail.com
 */
public class PersoEventListV implements Observer {
	/** Un ListOf<PersoEvent> d'un Event comme Model */
	Event _evt;
	
	/** JPanel comme Component */
	public JPanel _component;
	
	/** Panel for JPersoEvent */
	JPanel _persoPanel;
	/** Panel for PersoEvent description */
	JPanel _descPanel;
	
	/** ExpandAll Button */
	JButton _expandAllBtn;
	
	/** Class for helping in designing GUI */
	ImageIcon _iconClosed = GraphicHelper.createImageIcon(this,"book-closed_32x32.png", "");
	ImageIcon _iconOpen = GraphicHelper.createImageIcon(this,"book-open_32x32.png", "");
	
	/** Map de description */
	ArrayList<JTextArea> _descArea;
	/** Map de name */
	ArrayList<JLabel> _nameLabel;
	
	/** Status Flag pour AllDescExpand */
	boolean _allDescVisible = true;
	
	/* In order to Log */
	private static Logger logger = LogManager.getLogger(PersoEventListV.class.getName());

	/**
	 * Création avec un Event comme modèle (MVC).
	 * @param _evt
	 */
	public PersoEventListV(Event _evt) {
		this._evt = _evt;
		buildGUI();
		
		_evt._listPE.addObserver(this);
	}
	
	void buildGUI() {
//		POURQUOI les BOUTONS n'ont pas de nom !!!!!
		// Internal components list
		_nameLabel = new ArrayList<JLabel>();
		_descArea = new ArrayList<JTextArea>();
		
		// Main Panel avec un MigLayout
		MigLayout compLayout = new MigLayout(
				"hidemode 3,flowy", // Layout Constraints
				"2*indent[grow,fill]", // Column constraints
				""); // Row constraints);
		_component = new MigPanel(compLayout);
		
		// Liste des JPerso
		MigLayout persoLayout = new MigLayout(
				"", // Layout Constraints
				"2*indent", // Column constraints
				""); // Row constraints);
		_persoPanel = new MigPanel(persoLayout);
		_expandAllBtn = new JButton();
		// Action interne : expand/reduce toutes les descriptions.
		_expandAllBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setAllDescVisible(!_allDescVisible);
			}
		});
		_persoPanel.add(_expandAllBtn);
		
		// Description/Body pour chaque perso.
		MigLayout descLayout = new MigLayout(
				"hidemode 3,flowy", // Layout Constraints
				"2*indent[grow,fill]", // Column constraints
				""); // Row constraints);
		_descPanel = new MigPanel(descLayout);
		
		for (Entry<Integer, Event.PersoEvent> entry : _evt._listPE.entrySet()) {
			if (entry.getKey() >= 0) {
				PersoEventV persoBtn = new PersoEventV(_evt, entry.getValue());
				_persoPanel.add( persoBtn );

				PersoEventDescPanel pePanel = new PersoEventDescPanel(entry.getValue());
				_descPanel.add( pePanel );

				// Attache action 'ExpandDesc'
				// WARNING : le fait de modifier directement _leftClickAction peut
				//           faire que des Observables ne soient pas écouté, par exemple.
				persoBtn._leftClickAction = new ExpandDescAction("Détaille", null,
						//"Détaille "+entry.getValue()._perso.getName(),
						"Détaille ce personnage pour l'événement",
						null, pePanel);
			}
		}
		
		setAllDescVisible(true);
		
		_component.add(_persoPanel);
		_component.add(_descPanel);
	}
	
	@Override
	// Implement Observer
	public void update(Observable o, Object arg) {
		// Log
		logger.debug(_evt.getTitle()+" o is a "+o.getClass().getName()+ " arg="+arg);
		
		// Observe un ListOf<PersoEvent>
		if (arg != null) {
			if (arg instanceof String) {
				StringTokenizer sTok = new StringTokenizer((String)arg, "_");
				int id = Integer.parseInt(sTok.nextToken());
				String command = sTok.nextToken();
				// "add" -> un nouveau JPersoEvent et PersoEventPanel.
				if (command.equals("add")) {
					PersoEvent pe = _evt._listPE.get(id);
					PersoEventV persoBtn = new PersoEventV(_evt, pe);
					_persoPanel.add( persoBtn );
					
					PersoEventDescPanel pePanel = new PersoEventDescPanel(pe);
					_descPanel.add( pePanel );
					
					// Attache la bonne action 
					persoBtn._leftClickAction = new ExpandDescAction("Détaille", null,
							//"Détaille "+pe._perso.getName(),
							"Détaille ce personnage pour l'événement",
							null, pePanel);
					_component.revalidate();
					_component.repaint();
				}
				// "del" efface les composants incriminé
				else if (command.equals("del")){
					// Efface JPersoEvent
					for (int i = 0; i < _persoPanel.getComponentCount(); i++) {
						if( _persoPanel.getComponent(i) instanceof PersoEventV) {
							PersoEventV persoBtn = (PersoEventV) _persoPanel.getComponent(i);
							if (persoBtn._pe.getId() == id) {
								_persoPanel.remove(i);
							}
						}
					}
					// Efface PersoEventPanel
					for (int i = 0; i < _descPanel.getComponentCount(); i++) {
						PersoEventDescPanel pePanel = (PersoEventDescPanel) _descPanel.getComponent(i);
						if (pePanel._pe.getId() == id) {
							_descPanel.remove(i);
						}
					}
					_component.revalidate();
					_component.repaint();
				}
			}
		}
	}
	
	@SuppressWarnings("serial")
	/**
	 * Change l'état visible du descriptif d'un PersoEvent (label+desc).
	 */
	class ExpandDescAction extends AbstractAction {
		JPanel _panel;
		public ExpandDescAction(String text, ImageIcon icon, String help, Integer mnemonic,
				JPanel panel) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, help);
			putValue(MNEMONIC_KEY, mnemonic);
			_panel = panel;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("ExpandDescAction pour "+_label.getText());
			_panel.setVisible( !_panel.isVisible());
			_panel.revalidate();
		}
	}
	
	/**
	 * Rend tous les Descriptifs de PersoEvent (label+Desc) visibles ou non.
	 * @param aFlag true pour allVisible.
	 */
	void setAllDescVisible( boolean aFlag) {
		_allDescVisible = aFlag;
		for (JLabel l : _nameLabel) {
			l.setVisible(_allDescVisible);
		}
		for (JTextArea a : _descArea) {
			a.setVisible(_allDescVisible);
		}
		// Change tous visible ou invisibles
		for (int i = 0; i < _descPanel.getComponentCount(); i++) {
			PersoEventDescPanel pePanel = (PersoEventDescPanel) _descPanel.getComponent(i);
			pePanel.setVisible(_allDescVisible);
		}
		
		if (_allDescVisible) {
			_expandAllBtn.setIcon(_iconOpen);
		}
		else {
			_expandAllBtn.setIcon(_iconClosed);
		}
		_component.revalidate();
	}
	
	@SuppressWarnings("serial")
	/**
	 * Viewer interne pour la description d'un PersoEvent.<br>
	 * 
	 * Se compose de :
	 * <ul>
	 * <li> _persoName (JLabel) pour le nom du perso.</li>
	 * <li> _peDescArea (JTextArea) pour la description du PersoEvent </li>
	 * </ul>
	 *
	 * Traite les messages suivants :
	 * <ul>
	 * <li> Perso.set : modif _persoName</li>
	 * <li> PersoEvent.set_desc : update _peDescArea </li>
	 * </ul>
	 */
	static class PersoEventDescPanel extends MigPanel implements Observer {
		/** Model */
		PersoEvent _pe;
		
		/** Un JLabel pour le nom du Perso */
		JLabel _persoName;
		/** Un JTextArea pour la description */
		JTextArea _peDescArea;
		
		/* In order to Log */
		private static Logger loggerPE = LogManager.getLogger(PersoEventDescPanel.class.getName());
		
		public PersoEventDescPanel(PersoEvent persoEvent) {
			super();
			_pe = persoEvent;
			buildGUI();
			
			// Observe Perso (set) et PersoEvent (set)
			_pe.addObserver(this);
			_pe._perso.addObserver(this);
		}
		void buildGUI() {
			
			MigLayout persLayout = new MigLayout(
					"hidemode 3,flowy", // Layout Constraints
					"[grow,fill]", // Column constraints
					""); // Row constraints);
			this.setLayout(persLayout);
			
			_persoName = new JLabel(_pe._perso.getName());
			this.add(_persoName);
			
			_peDescArea = new JTextArea(_pe.getDesc());
			_peDescArea.setLineWrap(true);
			_peDescArea.setWrapStyleWord(true);
			_peDescArea.getDocument().addDocumentListener(new MyTextAreaListener(_peDescArea, _pe));
			
			// "wmin 100" est important, sinon ne rétrécit pas.
			this.add(_peDescArea, "wmin 100");
		}
		@Override
		public void update(Observable o, Object arg) {
			// Log
			loggerPE.debug(_pe._perso.getName()+" o is a "+o.getClass().getName()+ " arg="+arg);
			
			// Observe un Perso
			if (o instanceof Perso) {
				// only "set" message
				if (arg.equals("set")) {
					_persoName.setText(_pe._perso.getName());
					this.revalidate();
					this.repaint();
				}
			}
			// Observe a PersoEvent
			else if (o instanceof Event.PersoEvent) {
				// only "desc" message
				if (arg.equals("set_desc")) {
					_peDescArea.setText(_pe.getDesc());
					this.revalidate();
					this.repaint();
				}
			}
		}
		
		
	}
	
	/**
	 * Listen for changes dans un PersoEventDescPanel._peDescArea
	 * et appelle PersoEvent.setDesc().
	 */
	static class MyTextAreaListener implements DocumentListener {
		JTextArea _area;
		PersoEvent _pe;
		
		public MyTextAreaListener(JTextArea area, PersoEvent pe) {
			_area = area;
			_pe = pe;
		}
		
		@Override
		public void removeUpdate(DocumentEvent e) {
//			System.out.println("DOCUMENT REMOVE _title : "+e.toString());
			//_pe.setDesc(_area.getText());
		}
		@Override
		public void insertUpdate(DocumentEvent e) {
//			System.out.println("DOCUMENT INSERT _title : "+e.toString());
			//_pe.setDesc(_area.getText());
		}
		@Override
		public void changedUpdate(DocumentEvent e) {
//			System.out.println("DOCUMENT CHANGED _title : "+e.toString());
			// When properties change
		}
	}
}
