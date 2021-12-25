package towerdef;

import java.awt.Graphics;

public class BloodSpot extends Entity{
	public int x,y, rotation; 
	public double animX, animY;
	public BloodSpot(int x, int y, double animX, double animY) {
		this.x = x;
		this.y = y;
		this.animX = animX;
		this.animY = animY;
		//Save last location of the enemy entity and draw it
	}
	public void draw(Graphics g) {
		g.drawImage(Main.blood, Main.tileWidth*x+(int)animX, Main.tileHeight*y+(int)animY, Main.tileWidth, Main.tileHeight, null);
	}
}
