package game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;

public class Controller {

	public static int difficulty = 1; // 1 easy, 2 medium, 3 hard
	public static LinkedList<Hotdog> f = new LinkedList<Hotdog>();
	public static LinkedList<DietPill> d = new LinkedList<DietPill>();
	Hotdog tempHotdog;
	DietPill tempDietPill;

	private static Random r = new Random();

	public Controller() {

	}

	public static void cFoodPills(int diff) {
		difficulty = diff;
		createHotdogs(10 - (2 * difficulty));
		createDietPills(3 * difficulty);
	}

	public static void RESETFoodPills() {
		f.removeAll(f);
		d.removeAll(d);
	}

	public static void createHotdogs(int food_count) {
		for (int i = 0; i < food_count; i++) {
			addHotdog(new Hotdog(r.nextInt(1300), -100));
		}
	}

	public static void createDietPills(int pills_count) {
		for (int i = 0; i < pills_count; i++) {
			addDietpill(new DietPill(r.nextInt(1300), -100));
		}
	}

	public void tick() {
		for (int i = 0; i < f.size(); i++) {
			tempHotdog = f.get(i);
			tempHotdog.tick();
		}
		for (int i = 0; i < d.size(); i++) {
			tempDietPill = d.get(i);
			tempDietPill.tick();
		}
	}

	public void render(Graphics g) {
		for (int i = 0; i < f.size(); i++) {
			tempHotdog = f.get(i);
			tempHotdog.render(g);
		}
		for (int i = 0; i < d.size(); i++) {
			tempDietPill = d.get(i);
			tempDietPill.render(g);
		}

	}

	public static void addHotdog(Hotdog block) {
		f.add(block);
	}

	public void removeHotdog(Hotdog block) {
		f.remove(block);
	}

	public static void addDietpill(DietPill block) {
		d.add(block);
	}

	public void removeDietpill(DietPill block) {
		d.remove(block);
	}

}

class DietPill {

	private int x;
	private int y;

	Random r = new Random();
	private int fallingVelocity = (r.nextInt((3 * Controller.difficulty)) + 5);

	public int sz = 64;
	private SpriteSheet ss;
	private BufferedImage dietpill;
	public boolean shrink = false;
	public boolean disappeared = false;

	public DietPill(int x, int y) {
		this.x = x;
		this.y = y;

		ss = new SpriteSheet(Game.spriteSheet);
		dietpill = ss.grabImage(7, 1, 64, 64);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 64, 64);
	}

	public void tick() {

		y += fallingVelocity;
		if (y > 800) {
			fallingVelocity = (r.nextInt((3 * Controller.difficulty)) + 5);
			y = -20;
			x = r.nextInt(1300);
		}

		if (shrink)
			dietpill = Textures.resize(dietpill, sz -= 9, sz -= 9);

		if (sz < 10) {
			disappeared = true;
			System.out.println(disappeared);
		}

	}

	public void render(Graphics g) {
		g.drawImage(dietpill, x, y, null);

	}
}

class Hotdog {

	private int x;
	private int y;
	public int sz = 64;
	Random r = new Random();
	private int fallingVelocity = (r.nextInt(7) + 3);

	private SpriteSheet ss;
	private BufferedImage hotdog;
	public boolean shrink = false;
	public boolean disappeared = false;

	public Hotdog(int x, int y) {
		this.x = x;
		this.y = y;
		ss = new SpriteSheet(Game.spriteSheet);
		hotdog = ss.grabImage(3, 1, 64, 64);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 64, 64);
	}

	public void tick() {
		y += fallingVelocity;
		if (y > 800) {
			fallingVelocity = (r.nextInt(7) + 3);
			y = -20;
			x = r.nextInt(1300);
		}

		if (shrink)
			hotdog = Textures.resize(hotdog, sz -= 10, sz -= 10);

		if (sz < 10)
			disappeared = true;
	}

	public void render(Graphics g) {
		g.drawImage(hotdog, x, y, null);
	}

}