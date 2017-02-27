package game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

class BufferedImageLoader {

	private BufferedImage image;

	public BufferedImage loadImage(String path) throws IOException {
		image = ImageIO.read(getClass().getResourceAsStream(path));
		return image;
	}
}

class SpriteSheet {

	private BufferedImage image;

	public SpriteSheet(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage grabImage(int col, int row, int width, int height) {

		return image.getSubimage((col * 32) - 32, (row * 32) - 32, width, height);
	}

}

class Textures {

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		if (newW < 1 || newH < 1)
			return img;
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return dimg;
	}
}
