/**
 * 
 */
package editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;

import org.pushingpixels.flamingo.api.common.CommandButtonDisplayState;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonPopupOrientationKind;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;


/**
 * @author dutech
 *
 */
public class TestButton {

	private static final int DEFAULT_POPUP_ICON_LENGTH = 5;
	JCommandButton btn1;
	
	public void testJButton() {
		JFrame frame = new JFrame("test");
		JPanel main = new JPanel();
		JButton btn2 = new JButton( "bouton", createDefaultPopupIcon());
		Border brd1 = btn2.getBorder();
		System.out.println( "brd1="+brd1.toString());
		Rectangle rect1 = btn2.getBounds();
		System.out.println( "rect1="+rect1.toString());
		
		//btn1.setBorder(BorderFactory.createLineBorder(Color.red));
		main.add( btn2 );
		//main.setBorder(BorderFactory.createLineBorder(Color.green));
		frame.setContentPane(main);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		rect1 = btn2.getBounds();
		System.out.println( "rect1="+rect1.toString());
	}
	public void testFlamingo() {
		JFrame frame = new JFrame("test Flamingo");
		JPanel main = new JPanel();
		btn1 = new JCommandButton( "bouton" );
		// N'apparaît que dans DisplayState(TILE)
		btn1.setExtraText("un truc long");
		btn1.setCommandButtonKind(CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		btn1.setPopupOrientationKind(CommandButtonPopupOrientationKind.DOWNWARD);
		btn1.setFlat(false);
		btn1.setDisplayState(CommandButtonDisplayState.MEDIUM);
		btn1.setPopupCallback(new TestPopupCallback());
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("btn1 ACTION = "+btn1.getText());
			}
		});
		main.add( btn1 );
		JCommandButton btn2 = new JCommandButton( "bouton 2");
		btn2.setDisplayState(CommandButtonDisplayState.MEDIUM);
		btn2.setFlat(false);
		main.add( btn2 );
		//main.setBorder(BorderFactory.createLineBorder(Color.green));
		frame.setContentPane(main);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	class TestPopupCallback implements PopupPanelCallback {
		@Override
		public JPopupPanel getPopupPanel(JCommandButton commandButton) {

			JCommandPopupMenu simpleMenu = new JCommandPopupMenu();

			ActionListener itemAction = new ActionListener() {	
				@Override
				public void actionPerformed(ActionEvent e) {
					JCommandMenuButton item = (JCommandMenuButton) e.getSource();
					btn1.setText( item.getText() );
				}
			};
			
			JCommandMenuButton item = new JCommandMenuButton("-rien-", null);
			item.addActionListener( itemAction );
			simpleMenu.addMenuButton( item );
			item = new JCommandMenuButton("cyan", null);
			item.addActionListener( itemAction );
			simpleMenu.addMenuButton( item );
			item = new JCommandMenuButton("jaune", null);
			item.addActionListener( itemAction );
			simpleMenu.addMenuButton( item );
			
			return simpleMenu;
		}
	};
	
	private Icon createDefaultPopupIcon() {
		Image image = new BufferedImage(DEFAULT_POPUP_ICON_LENGTH*2,
				DEFAULT_POPUP_ICON_LENGTH*2, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		g.setColor(new Color(255, 255, 255, 0));
		g.fillRect(0, 0, DEFAULT_POPUP_ICON_LENGTH*2,
				DEFAULT_POPUP_ICON_LENGTH*2);
		
		g.setColor(Color.BLACK);
		Polygon traingle = new Polygon(
				new int [] { 0, DEFAULT_POPUP_ICON_LENGTH*2,
						DEFAULT_POPUP_ICON_LENGTH*2 / 2, 0 },
				new int[] { 0, 0, DEFAULT_POPUP_ICON_LENGTH*2, 0 }, 4);
		g.fillPolygon(traingle);
		
		g.dispose();
		return new ImageIcon(image);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
		for (LookAndFeelInfo lookAndFeelInfo : lafs) {
			System.out.println(lookAndFeelInfo.getName());
		}
		try {
			UIManager.setLookAndFeel(lafs[1].getClassName());
		} catch (Exception e) { }
		TestButton app = new TestButton();
		//app.testJButton();
		app.testFlamingo();
	}

}
