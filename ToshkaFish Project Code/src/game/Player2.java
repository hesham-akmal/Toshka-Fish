package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

public class Player2 {

	private int x;
	private int y;
	public int Size;

	private int velXR;
	private int velXL;
	private int velYU;
	private int velYD;

	private int ShiftSPD;
	public int score;
	public boolean RightDir = false;
	private int xad = 10;
	public static String name2;
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
	private BufferedImage player2;
	private BufferedImageLoader loader = new BufferedImageLoader();
	private BufferedImage yummy;
	private int VelocityY;
	private int VelocityX;
	private int weight;

	public Player2() throws IOException {
		INITP();
	}

	public void INITP() throws IOException {

		x = 800;
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

		player2 = loader.loadImage("/fish2l.png");
		player2 = Textures.resize(player2, Size, Size);
		yummy = loader.loadImage("/yummy.png");

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
		Size += 2;
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
			p1.gameEnd(1, p1, p2);

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

			xad++;

			if (xad % 10 == 0) {
				score += 5;

				if (RightDir) {
					player2 = Textures.resize(player2, Size, Size);
					player2 = loader.loadImage("/fish2.png");
				} else {
					player2 = Textures.resize(player2, Size, Size);
					player2 = loader.loadImage("/fish2l.png");
				}

				player2 = Textures.resize(player2, Size, Size);
			}
		}

		////////////////////////////////////////

		if (xad % 180 == 0)
			eatCount = 0;

		if (eatCount == (2 + (Size / 40)) && Size < 150) {
			growSlow = true;
			greatb = true;
			score += 2000;
			Sound.yummy.playOnce();
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
		g.drawString(name2 + " score: " + score, 990, 680);

		g.drawImage(player2, x, y, null);

		if (greatb) {
			g.drawImage(yummy, 500, -50, null);
			if (xad % 30 == 0)
				greatb = false;
		}
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
