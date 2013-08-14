/**
 * 
 */
package Inspiration;


import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Scrollable;

import net.miginfocom.swing.MigLayout;

/**
 * Un essai pour avoir les bases d'un élément dépliable.
 * 
 * @author snowgoon88@gmail.com
 */
public class Expandable {
	public JScrollPane _main;
	public JPanel _component;
	public JPanel _body;
	
	MigLayout _mainLayout;
	/**
	 * 
	 */
	public Expandable() {
		_mainLayout = new MigLayout(
				"flowy, hidemode 3", // Layout Constraints
				"[grow,fill]", // Column constraints
				""); // Row constraints
	}

	public void buildGUI() {
		// Main Panel avec un BoxLayout
		_component = new JPanel();
		BoxLayout compLayout = new BoxLayout(_component, BoxLayout.PAGE_AXIS);
		_component.setLayout(compLayout);
		
		_main = new JScrollPane(_component);
		
		// Check + Expand + JTextField in a line
		Checkbox checkBox1 = new Checkbox();
		JButton expander1 = new JButton("m");
		expander1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		JTextField title1 = new JTextField("Catastrophe de Médelin");
		JPanel eventPanel1 = new JPanel();
		BoxLayout eventLayout1 = new BoxLayout(eventPanel1, BoxLayout.LINE_AXIS);
		eventPanel1.setLayout(eventLayout1);
		eventPanel1.add(checkBox1);
		eventPanel1.add(expander1);
		eventPanel1.add(title1);
		_component.add(eventPanel1);
		
		
		_component.add( Box.createVerticalGlue());
		
		// Check + Expand + JTextField in a line
		Checkbox checkBox2 = new Checkbox();
		JButton expander2 = new JButton("m");
		expander1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		JTextField title2 = new JTextField("C'est la poisse");
		JPanel eventPanel2 = new JPanel();
		BoxLayout eventLayout2 = new BoxLayout(eventPanel2, BoxLayout.LINE_AXIS);
		eventPanel2.setLayout(eventLayout2);
		eventPanel2.add(checkBox2);
		eventPanel2.add(expander2);
		eventPanel2.add(title2);
		_component.add(eventPanel2);
		
		
		
		_component.add( Box.createVerticalGlue());
		_component.setVisible(true);
		
	}
	
	public void buildMIG() {
		// Main Panel avec un BoxLayout
		_component = new JPanel();
		MigLayout compLayout = new MigLayout(
				"", // Layout Constraints
				"[][][grow]", // Column constraints
				""); // Row constraints);
		//_component.setLayout(compLayout);
		_component.setLayout(_mainLayout);
		_main = new JScrollPane(_component);

		// Dockable component
		JPanel tools = new JPanel();
		JButton addBtn = new JButton("ADD");
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addEventAction(1);
			}
		});
		tools.add( addBtn );
		JButton visBtn = new JButton("VIS");
		visBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchVisibilityAction();
			}
		});
		tools.add( visBtn );
		_component.add( tools, "dock north");
		
		// Check + Expand + JTextField in a line
		Checkbox checkBox1 = new Checkbox();
		JButton expander1 = new JButton("m");
//		expander1.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// 
//			}
//		});
		JTextField title1 = new JTextField("Catastrophe de Médelin");
		// Check + Expand + JTextField in a line
		Checkbox checkBox2 = new Checkbox();
		JButton expander2 = new JButton("m");
//		expander1.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// 
//			}
//		});
		JTextField title2 = new JTextField("C'est la poisse");
		
		JPanel eventPanel = new JPanel();
		MigLayout eventLayout = new MigLayout(
				"", // Layout Constraints
				"[][][grow,fill]", // Column constraints
				""); // Row constraints
		eventPanel.setLayout(eventLayout);
		System.out.println("addEventAction");
		eventPanel.add( checkBox1 );
		eventPanel.add( expander1 );
		eventPanel.add( title1 );
		_component.add( eventPanel );
		_body = new JPanel();
		MigLayout bodyLayout = new MigLayout(
				"fill", // Layout Constraints
				"3*para[grow, fill]", // Column constraints
				"[grow, fill]"); // Row constraints
		_body.setLayout(bodyLayout);
		_body.add( new JTextArea("Salut, c'est un texte qui devrait prendre plusieurs lignes.\n Mais je n'en suis pas sur..."));
		_body.setVisible(true);
		_component.add(_body);
		
		eventPanel = new JPanel();
		eventLayout = new MigLayout(
				"", // Layout Constraints
				"[][][grow,fill]", // Column constraints
				""); // Row constraints
		eventPanel.setLayout(eventLayout);
		System.out.println("addEventAction");
		eventPanel.add( checkBox2 );
		eventPanel.add( expander2 );
		eventPanel.add( title2 );
		_component.add( eventPanel );
//		
//		_component.add( checkBox1 );
//		_component.add( expander1 );
//		_component.add( title1, "wrap, growx");
//		_component.add( checkBox2 );
//		_component.add( expander2 );
//		_component.add( title2, "wrap, growx");
		
	}
	
//	public void addEventAction() {
//		System.out.println("addEventAction");
//		// Check + Expand + JTextField in a line
//		Checkbox checkBox = new Checkbox();
//		JButton expander = new JButton("m");
//		expander.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// 
//			}
//		});
//		JTextField title = new JTextField("ajout pour voir");
//		_component.add( checkBox );
//		_component.add( expander );
//		_component.add( title, "wrap, growx");
//		_component.revalidate();
//	}
	public void addEventAction( int pos ) {
		JPanel eventPanel = new JPanel();
		MigLayout eventLayout = new MigLayout(
				"", // Layout Constraints
				"[][][grow,fill]", // Column constraints
				""); // Row constraints
		eventPanel.setLayout(eventLayout);
		System.out.println("addEventAction");
		// Check + Expand + JTextField in a line
		Checkbox checkBox = new Checkbox();
		JButton expander = new JButton("m");
		JTextField title = new JTextField("ajout pour voir");
		eventPanel.add( checkBox );
		eventPanel.add( expander );
		eventPanel.add( title );
		_component.add( eventPanel );
		_component.revalidate();
	}
	public void switchVisibilityAction() {
		_body.setVisible(!_body.isVisible());
		_body.revalidate();
	}
	
	// Pour embedder les JTextArea dans des scrollPane :o)
		// http://stackoverflow.com/questions/2475787/miglayout-jtextarea-is-not-shrinking-when-used-with-linewrap-true
		static class MyPanel extends JPanel implements Scrollable
		{
		  MyPanel(LayoutManager layout)
		  {
		     super(layout);
		  }

		  public Dimension getPreferredScrollableViewportSize()
		  {
		     return getPreferredSize();
		  }

		  public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
		  {
		     return 0;
		  }

		  public boolean getScrollableTracksViewportHeight()
		  {
		     return false;
		  }

		  public boolean getScrollableTracksViewportWidth()
		  {
		     return true;
		  }

		  public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
		  {
		     return 0;
		  }
		}
}
