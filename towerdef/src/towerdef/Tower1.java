package towerdef;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tower1 extends Tower {
	static double defaultRange = 2;
	public Tower1(int x, int y) {
		super(x, y, 10, 50, defaultRange, 10,0, initializeTexture());
	}
	public static BufferedImage initializeTexture() {
		BufferedImage texture = null;
		try {
			texture = ImageIO.read(new File("cannon2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texture;
	}
}
