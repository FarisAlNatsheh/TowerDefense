package towerdef;

import java.awt.Graphics;
import java.awt.Graphics2D;

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
	int damage = 1;
	public Projectile(double x, double y, Enemy target, int pierce, int type) {
		this.x = x;
		this.y = y;
		
		this.target = target;
		this.type = type;
		initialXdif = (target.X)*Main.tileWidth+target.animX+Main.tileWidth/2 - x;
		initialYdif = (target.Y)*Main.tileHeight+target.animY+Main.tileHeight/2 - y;
		angle = Math.atan((double)initialYdif/initialXdif);
		
		//Calcualtes the angle between projectile and targeted enemy
		

		this.pierce = pierce;
	}
	public void move() {
		//initialXdif = target.X*Main.tileWidth+target.animX - x;
		//initialYdif = target.Y*Main.tileHeight+target.animY - y;
		//angle = Math.atan((double)initialYdif/initialXdif); 
		
		//use for auto-aim bullets
		
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
		//Move based on angle
	}
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(angle, x,y);
		g2.drawImage(Main.projectileImg,(int)x, (int)y, width, height,null);
		g2.rotate(angle * -1, x,y);
	}
	//Draw projectile based on rotation
}
