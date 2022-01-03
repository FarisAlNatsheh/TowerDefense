package towerdef;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tower3 extends Tower {
	static double defaultRange = 2;
	static BufferedImage texture;
	public Tower3(int x, int y) {
		super(x, y, 1, 50, defaultRange, 5,2, texture,2);
	}
	public static void initializeTexture() {
		try {
			texture = ImageIO.read(new File("Textures/Tide Fighter.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void fire() {
		Enemy target = checkTargetOval();
		//Range option
		
		if(target != null) {
			initialXdif = (target.X)*Main.tileWidth+target.animX+Main.tileWidth/2 - x;
			initialYdif = (target.Y)*Main.tileHeight+target.animY+Main.tileHeight/2 - y;
			angle = Math.atan((double)initialYdif/initialXdif);
			//Calculate angle
			
			if(Main.tick % speed == 0) {
				projectiles.add(new Projectile(x-Main.tileWidth/4,y+Main.tileHeight/4, target, pierce,projType, damage));
				projectiles.add(new Projectile(x+Main.tileWidth/4,y+Main.tileHeight/4, target, pierce,projType, damage)); 
			}
			//Create new projectile at the towers location
		}
	}
}
