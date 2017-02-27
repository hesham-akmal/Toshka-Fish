package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Menu {

	public Rectangle playBtn = new Rectangle(Game.WIDTH / 3 + 120, 250, 250, 80);

	public Rectangle Highscore = new Rectangle(Game.WIDTH / 3 + 120, 400, 250, 80);

	public Rectangle Exit = new Rectangle(Game.WIDTH / 3 + 185, 550, 100, 80);

	public static boolean Menu1 = true;

	public void render(Graphics g) {
		// Graphics2D g2d = (Graphics2D) g;

		Font fnt = new Font("arial", Font.BOLD, 100);
		g.setFont(fnt);
		g.setColor(Color.WHITE);
		g.drawString("Toshka Fish", 380, 100);
		//////////////////////////////
		Font fnt2 = new Font("arial", Font.BOLD, 50);
		g.setFont(fnt2);

		if (Menu1) {
			// g2d.draw(playBtn);
			g.drawString("Play now", playBtn.x + 19, playBtn.y + 50);

		} else {

			g.drawString("Easy", playBtn.x - 140, playBtn.y + 50);
			g.drawString("Medium", playBtn.x + 30, playBtn.y + 50);
			g.drawString("Hard", playBtn.x + 280, playBtn.y + 50);

		}

		// g2d.draw(Highscore);
		g.drawString("Highscore", Highscore.x + 1, Highscore.y + 50);

		// g2d.draw(Exit);
		g.drawString("Quit", Exit.x, Exit.y + 50);
	}

}
