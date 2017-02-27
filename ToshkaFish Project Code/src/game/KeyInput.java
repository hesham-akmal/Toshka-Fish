package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class KeyInput extends KeyAdapter {

	Game game;

	public KeyInput(Game game) {

		this.game = game;

	}

	public void keyPressed(KeyEvent e) {
		game.keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		try {
			game.keyReleased(e);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
