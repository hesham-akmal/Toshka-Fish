package game;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class STT {
	public static EnterNameFrame NameWindow = new EnterNameFrame();
	public static HighscoreFrame high = new HighscoreFrame();
}

class EnterNameFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField namey = new JTextField("player1");
	private JTextField namey2 = new JTextField("player2");
	private JButton save;

	public EnterNameFrame() {

		this.setBounds(830, 350, 200, 200);
		this.setTitle("Name");
		this.setResizable(false);
		save = new JButton("Start game");

		Container c = this.getContentPane();
		c.setLayout(null);
		namey.setBounds(25, 30, 60, 35);
		namey.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		namey2.setBounds(95, 30, 60, 35);
		namey2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		save.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		save.setBounds(50, 80, 80, 50);

		c.add(namey);
		c.add(namey2);
		c.add(save);

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Player.name = namey.getText();
				Player2.name2 = namey2.getText();

				dispose();
				try {
					Game.createPlayers();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Sound.menuSound.stop();
				Sound.gameSound.playLoop();
				Game.state = Game.GSTATE.GAME;

			}
		});

	}
}

class HighscoreFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JLabel[] infos = new JLabel[10];
	private JPanel heh;

	public HighscoreFrame() {

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(650, 100, 400, 800);
		this.setTitle("Highscores");
		this.setResizable(false);
		Container c = this.getContentPane();
		heh = new JPanel();
		c.add(heh);
		heh.setLayout(new GridLayout(10, 1));
		for (int i = 0; i < 10; i++) {
			infos[i] = new JLabel();
			heh.add(infos[i]);
			infos[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}
		update();

	}

	public static void update() {
		for (int i = 0; i < Player.PINFOS.size() && i < 10; i++)
			infos[i].setText("                                           " + (i + 1) + ". " + Player.PINFOS.get(i).name
					+ "  " + Player.PINFOS.get(i).score + "  " + Player.PINFOS.get(i).diff);
	}

}