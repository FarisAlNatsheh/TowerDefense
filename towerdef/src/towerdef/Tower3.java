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
			setInitialXdif((target.X)*Main.tileWidth+target.animX+Main.tileWidth/2 - getX());
			setInitialYdif((target.Y)*Main.tileHeight+target.animY+Main.tileHeight/2 - getY());
			setAngle(Math.atan((double)getInitialYdif()/getInitialXdif()));
			//Calculate angle
			
			if(Main.tick % getSpeed() == 0) {
				getProjectiles().add(new Projectile(getX(),getY()+Main.tileHeight/8, target, getPierce(),getProjType(), getDamage(), true,true));
				getProjectiles().add(new Projectile(getX(),getY()+Main.tileHeight/8, target, getPierce(),getProjType(), getDamage(), false, true)); 
			}
			//Create new projectile at the towers location
		}
	}
}
