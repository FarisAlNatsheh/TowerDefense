package towerdef;

import java.awt.Graphics;

public class Projectile extends Entity{
	public double x,y,mapX, mapY;
	Enemy target;
	double speedX = Main.tileWidth/2, speedY= Main.tileHeight/2;
	double initialXdif;
	double initialYdif;
	double angle;
	int pierce;
	int type;
	int width = Main.tileHeight/2;
	int height = Main.tileWidth/6;
	int damage = 10;
	public Projectile(double x, double y, Enemy target, int pierce, int type) {
		this.x = x;
		this.y = y;
		
		this.target = target;
		this.type = type;
		initialXdif = (target.X)*Main.tileWidth+target.animX+Main.tileWidth/2 - x;
		initialYdif = (target.Y)*Main.tileHeight+target.animY+Main.tileHeight/2 - y;
		angle = Math.atan((double)initialYdif/initialXdif);
		//if(angle < 0) 
			//angle += Math.PI/4;

		this.pierce = pierce;
	}
	public void move() {
		//initialXdif = target.X*Main.tileWidth+target.animX - x;
		//initialYdif = target.Y*Main.tileHeight+target.animY - y;
		//angle = Math.atan((double)initialYdif/initialXdif); 
		//use for auto-aim bullets
		//System.out.println(angle);
		double sin = Math.sin(angle) * speedX;
		double cosine = Math.cos(angle) * speedY;
		
		if(initialYdif < 0) {
			y -= Math.abs(sin);
		}
		else {
			y += Math.abs(sin);
		}
		if(initialXdif < 0) {
			x -= Math.abs(cosine);
		}
		else {
			x += Math.abs(cosine);
		}
	}
	public void draw(Graphics g) {
		
	}
}
