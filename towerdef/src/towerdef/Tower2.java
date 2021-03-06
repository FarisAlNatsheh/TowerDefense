package towerdef;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tower2 extends Tower {
	static double defaultRange = 4;
	static BufferedImage texture;
	public Tower2(int x, int y) {
		super(x, y, 150, 350, defaultRange, 80,1, texture,1);
	}
	public static void initializeTexture() {
		try {
			texture = ImageIO.read(new File("Textures/Titan.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
