/**
 * 
 */
package gui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Scrollable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.miginfocom.swing.MigLayout;
import data.ListOf;
import data.Perso;
import data.Zorga;

/**
 * Liste de DEL+Name+Player+Zorga (as JComboBox)
 * Chaque ligne observe un Perso
 * JComboBox observe Zorgas
 * ADD, CLEAR, DUMP
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class PersoListV extends JPanel implements Observer {
	/** ListOf<Perso> comme Modèle */
	ListOf<Perso> _persoList;
	/** ListOf<Zorga> comme Modèle */
	ListOf<Zorga> _zorgaList;
	
	/** GUI element à mettre à jour */
	JPanel _listPanel;
	
	/* In order to Log */
	private static Logger logger = LogManager.getLogger(PersoListV.class.getName());
	
	
	/**
	 * Creation avec un ListOf<Perso> comme Modèle.
	 * @param persoList une liste de Perso
	 * @param zorgaList une liste de Zorga
	 */
	public PersoListV(ListOf<Perso> persoList, ListOf<Zorga> zorgaList) {
		super();
		this._persoList = persoList;
		_persoList.addObserver(this);
		
		this._zorgaList = zorgaList;
		// not observed here.
		
		buildGUI();
	}

	void buildGUI() {
		this.setLayout( new BorderLayout() );
		
		// Button Panel
		JPanel btnPanel = new JPanel();
		this.add(btnPanel, BorderLayout.NORTH);
		JButton addBtn = new JButton( new NewPersoAction());
		btnPanel.add( addBtn );
		JButton dumpBtn = new JButton( new DumpAllAction());
		btnPanel.add( dumpBtn );
		JButton clearBtn = new JButton( new ClearAction());
		btnPanel.add( clearBtn );
		
		MigLayout persoLayout = new MigLayout(
				"debug, hidemode 3,flowy", // Layout Constraints
				"[grow,fill]", // Column constraints
				""); // Row constraints);
		_listPanel = new MigPanel(persoLayout);
		this.add(_listPanel, BorderLayout.CENTER);
		
		for (Entry<Integer, Perso> entry : _persoList.entrySet()) {
			if (entry.getKey() >= 0) {
				PersoPanel pPanel = new PersoPanel(entry.getKey(), entry.getValue());
				_listPanel.add(pPanel);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		// Log
		logger.debug("o is a "+o.getClass().getName()+ " arg="+arg);
		// Observe seulement un PersoList
		// o est un PersoList
		// arg est un string
		StringTokenizer sTok = new StringTokenizer((String)arg, "_");
		int id = Integer.parseInt(sTok.nextToken());
		String command = sTok.nextToken();
		
		// "add" -> une nouvelle ligne dans _listPanel.
		if (command.equals("add")) {
			PersoPanel pPanel = new PersoPanel(id, _persoList.get(id));
			_listPanel.add(pPanel);
			this.revalidate();
			this.repaint();
		}
		// "del" efface le composant incriminé
		else if (command.equals("del")) {
			for (int i = 0; i < _listPanel.getComponentCount(); i++) {
				PersoPanel persoPanel = (PersoPanel) _listPanel.getComponent(i);
				if (persoPanel._perso.equals(_persoList.get(id))) {
					_listPanel.remove(i);
					this.revalidate();
					this.repaint();
					return;
				}
				
			}
		}
	}
	
	/**
	 * Un PersoPanel observe un Perso.<br>
	 * 
	 * <ul>
	 * <li> JButton : DelAction</li>
	 * <li> JTextField _nameField : _perso.getName(), change après ENTER</li>
	 * <li> JTextField _playerField : _perso.getPlayer(), change après ENTER</li>
	 * <li> ZorgaCombo : comboBox pour Zorga </li>
	 * </ul>
	 * 
	 * Message traités :
	 * <ul>
	 * <li> 'set' : màj _nameField et _playerField.</li>
	 * </ul>
	 * 
	 */
	class PersoPanel extends MigPanel implements Observer {
		/** Perso comme Model */
		Perso _perso;
		
		/** GUI element à mettre à jour */
		JTextField _nameField;
		JTextField _playerField;
		ZorgaCombo _zorgaCombo;
		
		public PersoPanel(int persId, Perso pers) {
			super();

			_perso = pers;
			buildGUI();
			_perso.addObserver(this);
		}
		void buildGUI() {
			MigLayout persLayout = new MigLayout(
					"", // Layout Constraints
					"[][grow,fill][grow,fill][]", // Column constraints
					""); // Row constraints);
			this.setLayout(persLayout);
			
			JButton delBtn = new JButton( new DelAction());
			this.add( delBtn );
			_nameField = new JTextField(_perso.getName());
			_nameField.addActionListener(new SetNameActionListener(_nameField));
			this.add(_nameField);
			_playerField = new JTextField(_perso.getPlayer());
			_playerField.addActionListener(new SetPlayerActionListener(_playerField));
			this.add(_playerField);
			
			_zorgaCombo = new ZorgaCombo();
			this.add(_zorgaCombo);
		}
		
		@Override
		public void update(Observable o, Object arg) {
			// MUST if Zorga is set !
			// Observe seulement des Perso
			// o should be a Perso
			// arg is a String
			String command = (String) arg;
			if (command.equals("set")) {
				_nameField = new JTextField(_perso.getName());
				_playerField = new JTextField(_perso.getPlayer());
				_zorgaCombo.setSelectedItem(_perso.getZorga());
			}
			
		}
		/**
		 * Une JComboBox qui observe Zorgas.
		 */
		class ZorgaCombo extends JComboBox<Object> implements Observer {
			/** In order to Log */
			private Logger logger = LogManager.getLogger(ZorgaCombo.class.getName());
			/** Listen for selection */
			SelectionActionListener _selectActionListener = null;
			
			public ZorgaCombo() {
				super();
				logger.trace("for "+_perso.getName());
				
				this.buildSortedList();
				
				_selectActionListener = new SelectionActionListener();
				this.addActionListener(_selectActionListener);
				
				_zorgaList.addObserver(this);
			}
			/** Build List of selectable items of ComboBox, listen to all Zorga */
			protected void buildSortedList() {
				// Get and sort Entries
				this.removeAllItems();
				Zorga[] zorgaArray = _zorgaList.toArray(new Zorga[0]);
				Arrays.sort(zorgaArray);
				for (Zorga zorga : zorgaArray) {
					this.addItem( zorga );
					zorga.addObserver(this);
				}
				this.setSelectedItem(_perso.getZorga());
			}
			/** Rebuild List of selectable items of ComboBox */
			protected void rebuildSortedList() {
				// Get and sort Entries
				this.removeAllItems();
				Zorga[] zorgaArray = _zorgaList.toArray(new Zorga[0]);
				Arrays.sort(zorgaArray);
				for (Zorga zorga : zorgaArray) {
					this.addItem( zorga );
				}
				this.setSelectedItem(_perso.getZorga());
			}
			@Override
			public void update(Observable o, Object arg) {
				// Observe une ListOf<Zorga> ou un Zorga
				logger.debug("for "+_perso.getName()+" o is a "+o.getClass().getName()+ " arg="+arg);
				if (o instanceof Zorga) {
					if (arg != null) {
						if (arg instanceof String ) {
							String command = (String)arg;
							if (command == "set") {
								// Disable ActionListener
								_selectActionListener._isEnabled = false;
								// Rebuild list of selection
								this.buildSortedList();
								// Enable ActionListener
								_selectActionListener._isEnabled = true;
							}
							this.revalidate();
							this.repaint();
						}
					}
				}
				else if (o instanceof ListOf<?>) {
					if (arg != null) {
						if (arg instanceof String) {
							StringTokenizer sTok = new StringTokenizer((String)arg, "_");
							int id = Integer.parseInt(sTok.nextToken());
							String command = sTok.nextToken();
							// "add" -> un nouvel item dans ComboBox.
							if (command.equals("add")) {
								// Disable ActionListener
								_selectActionListener._isEnabled = false;
								// Rebuild list of selection
								this.buildSortedList();
								// Listen to this new Zorga
								_zorgaList.get(id).addObserver(this);
								// Enable ActionListener
								_selectActionListener._isEnabled = true;
							}
							// "del" => détruit l'objet et modifie la sélection.
							else if (command.equals("del")) {
								// Disable ActionListener
								_selectActionListener._isEnabled = false;
								// Remove Item
								this.removeItem(_zorgaList.get(id));
								this.setSelectedItem( _perso.getZorga());
								// Enable ActionListener
								_selectActionListener._isEnabled = true;
							}
							this.revalidate();
							this.repaint();
						}
					}
				}
			}
		}
		/**
		 * ActionListener qui change le Zorga d'un Perso.
		 * Il faut pouvoir le "déconnecter" le temps de ré-organiser le JComboBox.
		 * @author dutech
		 *
		 */
		class SelectionActionListener implements ActionListener {
			/** is Enabled ? */
			public boolean _isEnabled = true;
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_isEnabled) {
					logger.trace("for "+_perso.getName()+" Param:"+e.paramString()+" Src:"+e.getSource());
					@SuppressWarnings({ "unchecked" })
					Zorga zorga = (Zorga) ((JComboBox<Object>) e.getSource()).getSelectedItem();
					_perso.setZorga(zorga);
				}
			}
		}
		
		/**
		 * Détruit un Perso Particulier.
		 */
		class DelAction extends AbstractAction {
			
			public DelAction() {
				super("DEL", null);
				putValue(SHORT_DESCRIPTION, "Détruit ce Perso.");
				putValue(MNEMONIC_KEY, null);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				_persoList.remove(_perso.getId());
			}
		}
		/**
		 * Modifie un Name de Perso (après appuis ENTER).
		 */
		class SetNameActionListener implements ActionListener {
			JTextField _textField;
			
			public SetNameActionListener(JTextField textField) {
				this._textField = textField;
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				_perso.setName(_textField.getText());
			}
		}
		/**
		 * Modifie un Player de Perso (après appuis ENTER).
		 */
		class SetPlayerActionListener implements ActionListener {
			JTextField _textField;
			
			public SetPlayerActionListener(JTextField textField) {
				this._textField = textField;
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				_perso.setPlayer(_textField.getText());
			}
		}
	}
	
	/**
	 * Crée un nouveau Zorga qu'on ajoute aux Zorgas.
	 */
	class NewPersoAction extends AbstractAction {

		public NewPersoAction() {
			super("Nouveau Perso", null);
			putValue(SHORT_DESCRIPTION, "Ajoute un Perso.");
			putValue(MNEMONIC_KEY, null);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Perso pers = new Perso("---", "---", Zorga.zorgaNull);
			_persoList.add(pers);
		}
	}
	
	/**
	 * Dump all Perso.
	 */
	public class DumpAllAction extends AbstractAction {

		public DumpAllAction() {
			super("Dump All", null);
			putValue(SHORT_DESCRIPTION, "Dump all Perso.");
			putValue(MNEMONIC_KEY, null);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("***** DumpALLAction *************");
			System.out.println(_persoList.sDump());
		}
	}
	/**
	 * Enlève tous les Perso.
	 */
	public class ClearAction extends AbstractAction {
		
		public ClearAction() {
			super("Vide", null);
			putValue(SHORT_DESCRIPTION, "Enlève tous les Persos.");
			putValue(MNEMONIC_KEY, null);
		}	
		
		@Override
		public void actionPerformed(ActionEvent e) {
			_persoList.clear();
		}
	}
	
	// http://stackoverflow.com/questions/2475787/miglayout-jtextarea-is-not-shrinking-when-used-with-linewrap-true
	/**
	 * L'idée est que le Panel ne soit pas Scrollable Horizontalement.
	 */
	static class MyPanel extends JPanel implements Scrollable
	{
		MyPanel(LayoutManager layout) {
			super(layout);
		}
		public Dimension getPreferredScrollableViewportSize() {
			return getPreferredSize();
		}
		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
			return 0;
		}
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}
		public boolean getScrollableTracksViewportWidth() {
			return true;
		}
		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
			return 0;
		}
	}

}
