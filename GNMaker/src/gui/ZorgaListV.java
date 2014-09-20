/**
 * 
 */
package gui;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.miginfocom.swing.MigLayout;

import data.ListOf;
import data.Zorga
;

/**
 * Affiche tous les Zorgas comme une liste de [DEL+JTextField] pour les éditer.
 * 
 * <ul>
 * <li> JButton : NewZorgaAction</li>
 * <li> JButton : DumpAllAction</li>
 * <li> JButton : ClearAllAction</li>
 * <li> MyPanel : liste verticale de ZorgaPanel</li>
 * </ul>
 * 
 * Traite les messages suivants :
 * <ul>
 * <li> id_arg : ajoute le ZorgaPanel(Zorga(id)) à _listPanel</li>
 * <li> id_del : delete le ZorgaPanel de Zorga(id)</li>
 * </ul>
 * 
 * bouton ADD, CLEAR, DUMP en haut ?
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class ZorgaListV extends JPanel implements Observer {
	/** Zorgas comme Model */
	ListOf<Zorga> _zorgaList;
	
	/** Panel pour les Zorgas */
	JPanel _listPanel;
	
	/* In order to Log */
	private static Logger logger = LogManager.getLogger(ZorgaListV.class.getName());
	
	/**
	 * @param zorgas
	 */
	public ZorgaListV(ListOf<Zorga> zorgas) {
		super();
		this._zorgaList = zorgas;
		_zorgaList.addObserver(this);
		buildGUI();
	}

	void buildGUI() {
		this.setLayout(new BorderLayout());
		
		// Button Panel
		JPanel btnPanel = new JPanel();
		this.add(btnPanel, BorderLayout.NORTH);
		JButton addBtn = new JButton( new NewZorgaAction());
		btnPanel.add( addBtn );
		JButton dumpBtn = new JButton( new DumpAllAction());
		btnPanel.add( dumpBtn );
		JButton clearBtn = new JButton( new ClearAction());
		btnPanel.add( clearBtn );
		
		MigLayout zorgasLayout = new MigLayout(
				"hidemode 3,flowy", // Layout Constraints
				"[grow,fill]", // Column constraints
				""); // Row constraints);
		_listPanel = new MigPanel(zorgasLayout);
		this.add(_listPanel, BorderLayout.CENTER);
		
		// Add [ZorgaPanel], only for id >=0
		for (Entry<Integer, Zorga> entry : _zorgaList.entrySet()) {
			if (entry.getKey() >= 0) {
				ZorgaPanel zorgaPanel = new ZorgaPanel(entry.getValue());
				_listPanel.add( zorgaPanel);
			}
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// Log
		logger.debug("o is a "+o.getClass().getName()+ " arg="+arg);
		// Observe Zorgas
		if (arg != null) {
			if (arg instanceof String) {
				StringTokenizer sTok = new StringTokenizer((String)arg, "_");
				int id = Integer.parseInt(sTok.nextToken());
				String command = sTok.nextToken();
				// "add" -> une nouvelle ligne dans _zorgaPanel.
				if (command.equals("add")) {
					ZorgaPanel zorgaPanel = new ZorgaPanel(_zorgaList.get(id));
					_listPanel.add( zorgaPanel);
					this.revalidate();
					this.repaint();
				}
				// "del" efface le composant incriminé
				else if (command.equals("del")){
					for (int i = 0; i < _listPanel.getComponentCount(); i++) {
						ZorgaPanel zorgaPanel = (ZorgaPanel) _listPanel.getComponent(i);
						if (zorgaPanel._zorga.equals(_zorgaList.get(id))) {
							_listPanel.remove(i);
							this.revalidate();
							this.repaint();
							return;
						}
						
					}
				}
			}
		}
	}

	/**
	 * Un ZorgaPanel oberve un Zorga.<br>
	 * 
	 * <ul>
	 * <li>JButton : DelAction</li>
	 * <li>JTextField : change après ENTER</li>
	 * </ul>
	 * 
	 * Message traités :
	 * <ul>
	 * <li>'set' : met à jour _nameField</li>
	 * </ul> 
	 *
	 */
	class ZorgaPanel extends JPanel implements Observer {
		/** Zorga comme Model */
		Zorga _zorga;
		
		/** GUI element à mettre à jour */
		JTextField _nameField;
		
		public ZorgaPanel(Zorga zorga) {
			super();
			_zorga = zorga;
			buildGUI();
			_zorga.addObserver(this);
		}
		void buildGUI() {
			// [Buttons][Name]
			MigLayout zorgaLayout = new MigLayout(
					"", // Layout Constraints
					"[][grow,fill]", // Column constraints
					""); // Row constraints);
			this.setLayout(zorgaLayout);
			
			JButton delBtn = new JButton( new DelAction());
			this.add( delBtn );
			_nameField = new JTextField(_zorga.getName());
			_nameField.addActionListener(new SetNameActionListener(_nameField));
			this.add(_nameField);
		}
		
		@Override
		public void update(Observable o, Object arg) {
			// Observe seulement des Zorga
			// o should be a Zorga
			// arg is a String
			String command = (String) arg;
			if (command.equals("set")) {
				_nameField.setText(_zorga.getName());
			}
			
		}
		
		/**
		 * Détruit un Zorga Particulier.
		 */
		class DelAction extends AbstractAction {
			
			public DelAction() {
				super("DEL", null);
				putValue(SHORT_DESCRIPTION, "Détruit ce Zorga.");
				putValue(MNEMONIC_KEY, null);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				_zorgaList.remove(_zorga.getId());
			}
		}
		/**
		 * Modifie un Name de Zorga (après appui sur ENTER).
		 */
		class SetNameActionListener implements ActionListener {
			JTextField _textField;
			
			public SetNameActionListener(JTextField textField) {
				this._textField = textField;
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				_zorga.setName(_textField.getText());
			}
		}
	}

	
	
	/**
	 * Crée un nouveau Zorga qu'on ajoute aux Zorgas.
	 */
	public class NewZorgaAction extends AbstractAction {

		public NewZorgaAction() {
			super("Nouveau Zorga", null);
			putValue(SHORT_DESCRIPTION, "Ajoute un Zorga.");
			putValue(MNEMONIC_KEY, null);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			_zorgaList.add(new Zorga("NEW"));
		}
	}
	
	/**
	 * Dump all Zorgas.
	 */
	public class DumpAllAction extends AbstractAction {

		public DumpAllAction() {
			super("Dump All", null);
			putValue(SHORT_DESCRIPTION, "Dump all Zorgas.");
			putValue(MNEMONIC_KEY, null);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("***** DumpALLAction *************");
			System.out.println(_zorgaList.sDump());
		}
	}
	/**
	 * Enlève tous les Zorgas.
	 */
	public class ClearAction extends AbstractAction {
		
		public ClearAction() {
			super("Vide", null);
			putValue(SHORT_DESCRIPTION, "Enlève tous les Zorgas.");
			putValue(MNEMONIC_KEY, null);
		}	
		
		@Override
		public void actionPerformed(ActionEvent e) {
			_zorgaList.clear();
		}
	}
}
