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
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Scrollable;

import net.miginfocom.swing.MigLayout;

import data.Zorgas;

/**
 * Affiche tous les Zorgas comme une liste de DEL+JTextField pour les éditer.
 * TODO : bouton ADD, CLEAR, DUMP en haut ?
 * 
 * @author snowgoon88@gmail.com
 */
@SuppressWarnings("serial")
public class ZorgasV extends JPanel implements Observer {
	/** Zorgas comme Model */
	Zorgas _zorgas;
	
	/** Panel pour les Zorgas */
	MyPanel _zorgaPanel;
	
	/**
	 * @param _zorgas
	 */
	public ZorgasV(Zorgas zorgas) {
		super();
		this._zorgas = zorgas;
		_zorgas.addObserver(this);
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
		
		MigLayout zorgasLayout = new MigLayout(
				"debug, hidemode 3", // Layout Constraints
				"[][grow,fill]", // Column constraints
				""); // Row constraints);
		_zorgaPanel = new MyPanel(zorgasLayout);
		this.add(_zorgaPanel, BorderLayout.CENTER);
		
		JButton delBtn;
		JTextField zorgaText;
		// Add [DEL][JTextField]
		for (Entry<Integer, String> entry : _zorgas.entrySet()) {
			delBtn = new JButton( new DelAction(entry.getKey()));
			_zorgaPanel.add( delBtn, "");
			
			zorgaText = new JTextField( entry.getValue() );
			// Action Listener : when ENTER pressed.
			zorgaText.addActionListener( new SetActionListener(entry.getKey(), zorgaText));
			_zorgaPanel.add( zorgaText, "wrap");
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg != null) {
			if (arg instanceof String) {
				StringTokenizer sTok = new StringTokenizer((String)arg, "_");
				int id = Integer.parseInt(sTok.nextToken());
				String command = sTok.nextToken();
				// "add" -> une nouvelle ligne dans _zorgaPanel.
				if (command.equals("add")) {
					JButton delBtn = new JButton( new DelAction(id));
					_zorgaPanel.add( delBtn, "");
					
					JTextField zorgaText = new JTextField( _zorgas.get(id) );
					// Action Listener : when ENTER pressed.
					zorgaText.addActionListener( new SetActionListener(id, zorgaText));
					_zorgaPanel.add( zorgaText, "wrap");
				}
				// "del" ou "clean" => reconstruit tout.
				else if (command.equals("del") || command.equals("clear")) {
					_zorgaPanel.removeAll();
					JButton delBtn;
					JTextField zorgaText;
					// Add [DEL][JTextField]
					for (Entry<Integer, String> entry : _zorgas.entrySet()) {
						delBtn = new JButton( new DelAction(entry.getKey()));
						_zorgaPanel.add( delBtn, "");
						
						zorgaText = new JTextField( entry.getValue() );
						// Action Listener : when ENTER pressed.
						zorgaText.addActionListener( new SetActionListener(entry.getKey(), zorgaText));
						_zorgaPanel.add( zorgaText, "wrap");
					}
				}
			}
		}
	}

	/**
	 * Détruit un Zorga Particulier.
	 */
	class DelAction extends AbstractAction {
		int _zorgaId;
		
		public DelAction(int zoraId) {
			super("DEL", null);
			putValue(SHORT_DESCRIPTION, "Détruit ce Zorga.");
			putValue(MNEMONIC_KEY, null);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			_zorgas.remove(_zorgaId);
		}
	}
	/**
	 * Modifie un Zorga (après appuis ENTER).
	 */
	class SetActionListener implements ActionListener {
		int _zorgaId;
		JTextField _textField;
		
		public SetActionListener(int zorgaId, JTextField textField) {
			this._zorgaId = zorgaId;
			this._textField = textField;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			_zorgas.set( _zorgaId, _textField.getText());
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
			_zorgas.add("Nouveau Zorga");
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
			System.out.println(_zorgas.SDump());
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
