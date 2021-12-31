package towerdef;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tower2 extends Tower {
	static double defaultRange = 4;
	public Tower2(int x, int y) {
		super(x, y, 15, 350, defaultRange, 80,1, initializeTexture(),2);
	}
	public static BufferedImage initializeTexture() {
		BufferedImage texture = null;
		try {
			texture = ImageIO.read(new File("Titan.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texture;
	}
}
