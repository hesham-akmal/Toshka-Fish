package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import game.Game.GSTATE;

class PlayerInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	int score;
	String diff;
	String name;

	public PlayerInfo(String name, int score, String diff) {
		this.score = score;
		this.diff = diff;
		this.name = name;
	}

	public static void sortScores() {

		int tmp = 0;
		String di = "";
		String na = "";

		for (int i = 0; i < Player.PINFOS.size(); i++) {
			for (int y = 0; y < Player.PINFOS.size() - 1; y++) {

				if (i == y)
					continue;

				if (Player.PINFOS.get(y).score < Player.PINFOS.get(i).score) {

					tmp = Player.PINFOS.get(i).score;
					Player.PINFOS.get(i).score = Player.PINFOS.get(y).score;
					Player.PINFOS.get(y).score = tmp;

					na = Player.PINFOS.get(i).name;
					Player.PINFOS.get(i).name = Player.PINFOS.get(y).name;
					Player.PINFOS.get(y).name = na;

					di = Player.PINFOS.get(i).diff;
					Player.PINFOS.get(i).diff = Player.PINFOS.get(y).diff;
					Player.PINFOS.get(y).diff = di;
				}
			}
		}
	}
}

public class Player {

	private int x;
	private int y;
	private int Size;

	private int velXR;
	private int velXL;
	private int velYU;
	private int velYD;

	private int ShiftSPD;
	public int score;
	public boolean RightDir = true;
	private int xad = 10;
	public static String name;
	private int eatCount = 0;
	private boolean greatb = false;
	private boolean growSlow = false;
	private int finalSize = 0;
	///////////
	private int x1;
	private int y1;
	private int width;
	private int height;
	///////////
	private Font fnt = new Font("arial", Font.BOLD, 25);
	private BufferedImage player1;
	private BufferedImageLoader loader = new BufferedImageLoader();
	private BufferedImage great;
	private int VelocityX;
	private int VelocityY;
	public int weight = 0;

	public static ArrayList<PlayerInfo> PINFOS = new ArrayList<>();

	public Player() throws IOException {
		INITP();
	}

	@SuppressWarnings("unchecked")
	public void INITP() throws IOException {

		x = 200;
		y = 200;
		Size = 64;
		score = 0;
		velXR = 0;
		velXL = 0;
		velYU = 0;
		velYD = 0;
		VelocityX = 0;
		VelocityY = 0;
		weight = 0;

		ShiftSPD = 11;

		player1 = loader.loadImage("/fish1.png");
		player1 = Textures.resize(player1, Size, Size);
		great = loader.loadImage("/great.png");

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.hsc"));
		try {
			PINFOS = (ArrayList<PlayerInfo>) ois.readObject();
			ois.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void gameEnd(int i, Player p1, Player2 p2) throws IOException {

		Sound.sus.playOnce();

		////////////// HIGH SCORE ///////////////////////

		String d = "Hard";
		if (Controller.difficulty == 1)
			d = "Easy";
		if (Controller.difficulty == 2)
			d = "Medium";

		if (p1.score > 0)
			PINFOS.add(new PlayerInfo(Player.name, p1.score, d));

		if (p2.score > 0)
			PINFOS.add(new PlayerInfo(Player2.name2, p2.score, d));

		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.hsc"));
			PlayerInfo.sortScores();
			oos.writeObject(PINFOS);
			oos.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		HighscoreFrame.update();

		////////////////////////////////////////

		if (i == 1)
			JOptionPane.showMessageDialog(null, Player.name + " Won!", "", JOptionPane.PLAIN_MESSAGE);
		if (i == 2)
			JOptionPane.showMessageDialog(null, Player2.name2 + " Won!", "", JOptionPane.PLAIN_MESSAGE);

		backMenu();
	}

	public static void backMenu() throws IOException {
		Sound.gameSound.stop();
		Sound.menuSound.playLoop();
		Game.state = GSTATE.MENU;
		Controller.RESETFoodPills();
	}

	public Rectangle getBounds() {

		if (RightDir)
			x1 = x + (Size / 2);
		else
			x1 = x + (Size / 20);

		y1 = y + (Size / 4);
		width = Size - (Size / 2);
		height = Size - (Size / 2);

		return new Rectangle(x1, y1, width, height);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void getBigger() {
		Size++;
	}

	public void getSmaller() {

		Size -= 3;

		score -= 100 * Controller.difficulty;
		if (score < 0)
			score = 0;
	}

	public void CollisionPlayerWithFood(LinkedList<Hotdog> f) {

		for (int i = 0; i < f.size(); i++) {
			if (getBounds().intersects(f.get(i).getBounds()) && xad % 10 == 0)
				if (Size < 445) {
					f.get(i).shrink = true;
					getBigger();
					score += 60;
					eatCount++;
				}
			if (f.get(i).disappeared) {
				f.remove(i);
				Controller.createHotdogs(1);
			}
		}
	}

	public void CollisionPlayerWithPill(LinkedList<DietPill> d, Player p1, Player2 p2) throws IOException {

		if (Size < 10)
			return;

		for (int i = 0; i < d.size(); i++) {

			if (getBounds().intersects(d.get(i).getBounds()) && xad % 10 == 0) {
				d.get(i).shrink = true;
				getSmaller();
				eatCount = 0;
			}

			if (d.get(i).disappeared) {
				d.remove(i);
				Controller.createDietPills(1);
			}

		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void CollisionPlynPly2(Player p1, Player2 p2) throws IOException {

		if (p1.Size < 10)
			gameEnd(2, p1, p2);

		else if (p2.Size < 10)
			gameEnd(1, p1, p2);

		if (getBounds().intersects(p2.getBounds())) {

			if (p1.Size < p2.Size) {

				p1.Size--;

				if (p2.Size < 200)
					p2.Size++;

			} else if (p1.Size > p2.Size) {

				p2.Size--;

				if (p1.Size < 200)
					p1.Size++;

			}
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void tick(Player p1, Player2 p2) throws IOException {

		if (Game.state == Game.GSTATE.GAME) {

			x += VelocityX;

			y += VelocityY;

			if (xad % 2 == 0) {

				if (Size > 400)
					weight = 8;
				else if (Size > 300)
					weight = 7;
				else if (Size > 250)
					weight = 6;
				else if (Size > 200)
					weight = 5;
				else if (Size > 150)
					weight = 4;
				else if (Size > 100)
					weight = 3;
				else if (Size > 50)
					weight = 1;

				if (velXR != 0) {
					if (VelocityX < (ShiftSPD - weight))
						VelocityX++;
				}
				if (velXR == 0) {
					if (VelocityX > 0)
						VelocityX--;
				}
				if (velXL == 0) {
					if (VelocityX < 0)
						VelocityX++;
				}
				if (velXL != 0) {
					if (VelocityX > -(ShiftSPD - weight))
						VelocityX--;
				}
				if (velYD != 0) {
					if (VelocityY < (ShiftSPD - weight))
						VelocityY++;
				}
				if (velYD == 0) {
					if (VelocityY > 0)
						VelocityY--;
				}
				if (velYU != 0) {
					if (VelocityY > -(ShiftSPD - weight))
						VelocityY--;
				}
				if (velYU == 0) {
					if (VelocityY < 0)
						VelocityY++;
				}
			}

			if (x <= 0)
				x = 0;

			if (x >= 1300 - (Size))
				x = 1300 - (Size);

			if (y <= 0)
				y = 0;

			if (y >= 800 - Size)
				y = 800 - Size;

			CollisionPlayerWithFood(Controller.f);
			CollisionPlayerWithPill(Controller.d, p1, p2);
			CollisionPlynPly2(p1, p2);

			// THIS WILL CAUSE SOME FRAMEDROPS
			xad++;

			if (xad % 10 == 0) {
				score += 5;

				if (RightDir) {
					player1 = Textures.resize(player1, Size, Size);
					player1 = loader.loadImage("/fish1.png");
				} else {
					player1 = Textures.resize(player1, Size, Size);
					player1 = loader.loadImage("/fish1l.png");

				}

				player1 = Textures.resize(player1, Size, Size);
			}
		}

		////////////////////////////////////////

		if (xad % 180 == 0)
			eatCount = 0;

		if (eatCount == (2 + (Size / 40)) && Size < 150) {
			growSlow = true;
			greatb = true;
			score += 2000;
			Sound.great.playOnce();
			eatCount = 0;
			finalSize = Size + 5;
		}

		if (growSlow)
			if (xad % 5 == 0) {
				if (Size < finalSize) {
					Size++;
				} else {
					finalSize = 0;
					growSlow = false;
				}
			}
	}

	public void render(Graphics g) {

		g.setFont(fnt);
		g.setColor(Color.BLACK);
		g.drawString(name + " score: " + score, 10, 680);

		g.drawImage(player1, x, y, null);

		if (greatb) {
			g.drawImage(great, 500, -50, null);
			if (xad % 30 == 0)
				greatb = false;
		}

		// Graphics2D g2d = (Graphics2D) g;
		// UNCOMMENT TO SEE EATING RECT
		// g2d.drawRect(x1, y1, width, height);
	}

	public void setVelXL(int velXL) {
		this.velXL = velXL;
	}

	public void setVelXR(int velXR) {
		this.velXR = velXR;
	}

	public void setVelYU(int velYU) {
		this.velYU = velYU;
	}

	public void setVelYD(int VelYD) {
		this.velYD = VelYD;
	}

	public void setSPD(int x) {
		this.ShiftSPD = x;
	}

}
