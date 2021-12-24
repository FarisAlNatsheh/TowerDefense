package towerdef;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Tower extends Entity{
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
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		for(Projectile k : projectiles) {
			double x = 0, y = 0;
			double angle = 0;
			angle = k.angle;
			x = k.x;
			y = k.y;
			g2.rotate(angle, k.x,k.y);
			g2.drawImage(Main.projectileImg,(int)k.x, (int)k.y, k.width, k.height,null);
			g2.rotate(angle * -1, x,y);
		}
		if(initialYdif > 0) {

			if(angle < 0) {
				g2.rotate(angle, x,y);
				g.drawImage(Main.towerImg,this.x-width/2,this.y-height/2,width,height,null);
				g2.rotate(angle*-1, x,y);
			}
			else {
				g2.rotate(angle+ Math.PI, x,y);
				g.drawImage(Main.towerImg,this.x-width/2,this.y-height/2,width,height,null);
				g2.rotate((angle+ Math.PI)*-1, x,y);
			}
		}
		else if(initialYdif < 0) {
			if(angle > 0) {
				g2.rotate(angle, x,y);
				g.drawImage(Main.towerImg,this.x-width/2,this.y-height/2,width,height,null);
				g2.rotate(angle*-1,x,y);
			}
			else {
				g2.rotate(angle+ Math.PI, x,y);
				g.drawImage(Main.towerImg,this.x-width/2,this.y-height/2,width,height,null);
				g2.rotate((angle+ Math.PI)*-1, x,y);
			}
		}
		else if(initialYdif == 0) {
			if(initialXdif < 0)
				g.drawImage(Main.towerImg,this.x-width/2,this.y-height/2,width,height,null);
			else {
				g2.rotate(Math.PI, x,y);
				g.drawImage(Main.towerImg,this.x-width/2,this.y-height/2,width,height,null);
				g2.rotate(Math.PI*-1, x,y);
			}
		}
	}
	
}
