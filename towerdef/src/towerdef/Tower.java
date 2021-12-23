package towerdef;

import java.util.ArrayList;

public class Tower {
	public int x;
	public int y;
	public int mapX;
	public int mapY;
	public int width = Main.tileWidth;
	public int height = Main.tileHeight;
	int speed = 10;
	double initialXdif;
	double initialYdif;
	double angle;
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	public Tower(int x, int y) {
		this.x = x*Main.tileWidth+Main.tileWidth/2;
		this.y = y*Main.tileHeight+Main.tileHeight/2;
		mapX = x;
		mapY = y;
	}
	
	public void fire(Enemy target) {
		initialXdif = (target.X)*Main.tileWidth+target.animX+Main.tileWidth/2 - x;
		initialYdif = (target.Y)*Main.tileHeight+target.animY+Main.tileHeight/2 - y;
		angle = Math.atan((double)initialYdif/initialXdif);
		if(Main.tick % speed == 0)
			projectiles.add(new Projectile(x,y, target, 3,0));
	}
	
}
