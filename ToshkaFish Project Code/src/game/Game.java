package game;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int SCALE = 2;
	public final String TITLE = "Game";

	public static ArrayList<String> HighList;

	private boolean running = false;
	private Thread thread;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	public static BufferedImage spriteSheet = null;
	private BufferedImage background = null;
	BufferedImageLoader loader = new BufferedImageLoader();

	private static Player p1;
	private static Player2 p2;
	private Controller f;

	@SuppressWarnings("unused")
	private Textures tex;
	private Menu menu;

	public static Game game = new Game();

	public enum GSTATE {
		MENU, GAME
	};

	public static GSTATE state = GSTATE.MENU;

	public void init() throws IOException {

		requestFocus();

		Sound.menuSound.playLoop();

		try {
			background = loader.loadImage("/back2.jpg");
			spriteSheet = loader.loadImage("/ss.png");

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.addKeyListener(new KeyInput(this));
		this.addMouseListener(new MouseInput());

		tex = new Textures();

		createPlayers();

		f = new Controller();
		menu = new Menu();
	}

	public static void createPlayers() throws IOException {
		p1 = new Player();
		p2 = new Player2();
	}

	private synchronized void start() {
		if (running)
			return; // if already running

		running = true;
		thread = new Thread(this);
		thread.start();
	}

	private synchronized void stop() {
		if (!running)
			return;

		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// in case fails, probably won't
			e.printStackTrace();
		}
		System.exit(0);

	}

	@Override
	public void run() {
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long lastTime = System.nanoTime();
		final double Ticks = 60.0;
		double ns = 1000000000 / Ticks;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				try {
					tick();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				updates++;
				delta--;
			}
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " " + frames);
				updates = 0;
				frames = 0;
			}

		}
		stop();

	}

	private void tick() throws IOException {

		if (state == GSTATE.GAME) {
			p1.tick(p1, p2);
			p2.tick(p1, p2);
			f.tick();
		}
	}

	private void render() {

		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null) {
			createBufferStrategy(3); // TRY TWO
			return;
		}

		Graphics g = bs.getDrawGraphics();

		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

		g.drawImage(background, 0, 0, null);

		if (state == GSTATE.GAME) {
			p1.render(g);
			p2.render(g);
			f.render(g);

		} else if (state == GSTATE.MENU)
			menu.render(g);

		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setBounds(250, 100, Game.WIDTH, Game.HEIGHT);
		frame.add(game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Game");
		frame.setResizable(false);
		frame.setVisible(true);
		game.start();
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (state == GSTATE.GAME) {

			if (key == KeyEvent.VK_ESCAPE) {

				try {
					Player.backMenu();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

			if (key == KeyEvent.VK_RIGHT) {
				p2.setVelXR(1);
				p2.RightDir = true;

			} else if (key == KeyEvent.VK_LEFT) {
				p2.setVelXL(-1);
				p2.RightDir = false;

			} else if (key == KeyEvent.VK_UP)
				p2.setVelYU(-1);

			else if (key == KeyEvent.VK_DOWN)
				p2.setVelYD(1);

			else if (key == KeyEvent.VK_A) {
				p1.setVelXL(-1);
				p1.RightDir = false;
			} else if (key == KeyEvent.VK_D) {
				p1.setVelXR(1);
				p1.RightDir = true;
			} else if (key == KeyEvent.VK_W)
				p1.setVelYU(-1);
			else if (key == KeyEvent.VK_S)
				p1.setVelYD(1);
			else if (key == KeyEvent.VK_CONTROL)
				p2.setSPD(13);
			else if (key == KeyEvent.VK_SHIFT)
				p1.setSPD(13);
		}
	}

	public void keyReleased(KeyEvent e) throws IOException {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_RIGHT) {
			p2.setVelXR(0);
		} else if (key == KeyEvent.VK_LEFT) {
			p2.setVelXL(0);
		} else if (key == KeyEvent.VK_UP) {
			p2.setVelYU(0);
		} else if (key == KeyEvent.VK_DOWN) {
			p2.setVelYD(0);
		} else if (key == KeyEvent.VK_A) {
			p1.setVelXL(0);
		} else if (key == KeyEvent.VK_D) {
			p1.setVelXR(0);
		} else if (key == KeyEvent.VK_W) {
			p1.setVelYU(0);
		} else if (key == KeyEvent.VK_S) {
			p1.setVelYD(0);
		} else if (key == KeyEvent.VK_CONTROL) {
			p2.setSPD(11);
		} else if (key == KeyEvent.VK_SHIFT) {
			p1.setSPD(11);
		}

	}

}
