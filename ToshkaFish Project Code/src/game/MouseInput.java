package game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

	@Override
	public void mousePressed(MouseEvent e) {

		int mx = e.getX();
		int my = e.getY();
		int x = Game.WIDTH / 3 + 120;

		if (Game.state == Game.GSTATE.MENU) {

			// easymwdhard
			if (my >= 250 && my <= 330 && !Menu.Menu1) {
				if (mx >= x - 140 && mx <= x) {
					Controller.RESETFoodPills();
					Controller.cFoodPills(1);
					STT.NameWindow.setVisible(true);
				} else if (mx >= x && mx <= x + 250) {
					Controller.RESETFoodPills();
					Controller.cFoodPills(2);
					STT.NameWindow.setVisible(true);
				} else if (mx >= x + 280 && mx <= x + 390) {
					Controller.RESETFoodPills();
					Controller.cFoodPills(3);
					STT.NameWindow.setVisible(true);
				}

			}

			// play button
			if (mx >= x && mx <= x + 250 && my >= 250 && my <= 330 && Menu.Menu1)
				Menu.Menu1 = false;

			// highscore button
			if (mx >= (Game.WIDTH / 3) + 120 && mx <= (Game.WIDTH / 3 + 120 + 250) && my >= 400 && my <= 400 + 80)
				STT.high.setVisible(true);

			// quit button
			if (mx >= (Game.WIDTH / 3) + 185 && mx <= (Game.WIDTH / 3 + 185 + 100) && my >= 550 && my <= 550 + 80)
				System.exit(0);
		}

	}
}
