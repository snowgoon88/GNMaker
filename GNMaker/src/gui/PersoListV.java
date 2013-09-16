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

import data.Perso;
import data.PersoList;

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
	/** PersoList comme Modèle */
	PersoList _persoList;
	
	/** GUI element à mettre à jour */
	JPanel _listPanel;
	
	
	/**
	 * Creation avec un PersoList comme Modèle.
	 * @param _persoList
	 */
	public PersoListV(PersoList persos) {
		super();
		this._persoList = persos;
		_persoList.addObserver(this);
		
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
		_listPanel = new MyPanel(persoLayout);
		this.add(_listPanel, BorderLayout.CENTER);
		
		for (Entry<Integer, Perso> entry : _persoList.entrySet()) {
			PersoPanel pPanel = new PersoPanel(entry.getKey(), entry.getValue());
			_listPanel.add(pPanel);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
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
		// "del" ou "clear" => reconstruit tout.
		else if (command.equals("del") || command.equals("clear")) {
			System.out.println("PersoListV.update() : "+command);
			_listPanel.removeAll();
			for (Entry<Integer, Perso> entry : _persoList.entrySet()) {
				PersoPanel pPanel = new PersoPanel(entry.getKey(), entry.getValue());
				_listPanel.add(pPanel);
			}
			this.revalidate();
			this.repaint();
		}
	}
	
	class PersoPanel extends JPanel implements Observer {
		/** Perso comme Model */
		Perso _perso;
		/** Id du Perso pour effacer */
		int _persId;
		
		/** GUI element à mettre à jour */
		JTextField _nameField;
		JTextField _playerField;
		
		public PersoPanel(int persId, Perso pers) {
			super();
			_persId = persId;
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
			
			ZorgaCombo zorgaCombo = new ZorgaCombo(_persoList._zorgas.toArray());
			//zorgaCombo.getSelectedItem();
			this.add(zorgaCombo);
		}
		
		@Override
		public void update(Observable o, Object arg) {
			// Observe seulement des Perso
			// o should be a Perso
			// arg is a String
			String command = (String) arg;
			if (command.equals("set")) {
				_nameField = new JTextField(_perso.getName());
				_playerField = new JTextField(_perso.getPlayer());
			}
			
		}
		/**
		 * Une JComboBox qui observe Zorgas.
		 * TODO Mais qui doit aussi observer Perso (si change de Zorga)
		 */
		class ZorgaCombo extends JComboBox<Object> implements Observer {
			/**
			 * @param _zorgas
			 */
			/* In order to Log */
			private Logger logger = LogManager.getLogger(ZorgaCombo.class.getName());
			
			public ZorgaCombo(Object[] items) {
				super();
				logger.trace("for "+_perso.getName());
				this.addItem("---");
				for (int i = 0; i < items.length; i++) {
					this.addItem(items[i]);
				}
				this.setSelectedItem(_perso.getZorga());
				
				addActionListener( new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						logger.trace("for "+_perso.getName()+" "+e.paramString());
						@SuppressWarnings({ "unchecked" })
						Object zorgaName = ((JComboBox<Object>) e.getSource()).getSelectedItem();
						_perso.setZorga((String) zorgaName);
					}
				});
				_persoList._zorgas.addObserver(this);
			}
			@Override
			public void update(Observable o, Object arg) {
				// Observe un Zorgas
				logger.debug("for "+_perso.getName()+" o is a "+o.getClass().getName()+ " arg="+arg);
				if (arg != null) {
					if (arg instanceof String) {
						StringTokenizer sTok = new StringTokenizer((String)arg, "_");
						int id = Integer.parseInt(sTok.nextToken());
						String command = sTok.nextToken();
						// "add" -> un nouvel item dans ComboBox.
						if (command.equals("add")) {
							this.addItem( _persoList._zorgas.get(id));
						}
						// "del" => modifie sélection si besoin, et détruit l'objet.
						else if (command.equals("del")) {
							if (_perso.getZorgaId() == id) {
								this.setSelectedItem("---");
							}
							this.removeItem(_persoList._zorgas.get(id));
						}
						// "set" - modifier l'item concerné
						// C'est embettant car il faut l'ancienne valeur ET la nouvelle !
						// TODO Faire que Zorga soit une liste de Zorga. => tester JComboBox OKOKOK
						else if (command.equals("set")) {
							
						}
						this.revalidate();
						this.repaint();
					}
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
				_persoList.remove(_persId);
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
			Perso pers = new Perso("---", "---", _persoList._zorgas, -1);
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