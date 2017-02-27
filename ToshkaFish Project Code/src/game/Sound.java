package game;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

	public static Sound menuSound = new Sound("/UTMC.wav");
	public static Sound gameSound = new Sound("/Trololo_Chiptune.wav");
	public static Sound great = new Sound("/great.wav");
	public static Sound yummy = new Sound("/yummy.wav");
	public static Sound sus = new Sound("/sus.wav");

	private AudioClip clip;

	public Sound(String filename) {

		try {
			clip = Applet.newAudioClip(Sound.class.getResource(filename));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void playOnce() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void playLoop() {
		try {
			new Thread() {
				public void run() {
					clip.loop();
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void stop() {
		clip.stop();
	}

}
