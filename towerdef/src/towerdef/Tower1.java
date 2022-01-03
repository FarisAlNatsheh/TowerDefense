package towerdef;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tower1 extends Tower {
	static double defaultRange = 3;
	
	public Tower1(int x, int y) {
		super(x, y, 10, 100, defaultRange, 30,0, texture,4);
	}
	static BufferedImage texture;
	public static void initializeTexture() {
		try {
			texture = ImageIO.read(new File("Textures/cannon2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
