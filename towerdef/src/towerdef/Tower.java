package towerdef;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Tower extends Entity{
	public int x;
	public int y;
	public int mapX;
	public int mapY;
	public int width = Main.tileWidth;
	public int height = Main.tileHeight;
	int speed; //per how many ticks
	double initialXdif;
	double initialYdif;
	double angle;
	double range;
	int price;
	int pierce;
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	int hitCount;
	int projType;
	int damage;
	BufferedImage texture;
	public Tower(int x, int y, int pierce, int price, double range,int speed, int projType, BufferedImage texture, int damage) {
		this.x = x*Main.tileWidth+Main.tileWidth/2;
		this.y = y*Main.tileHeight+Main.tileHeight/2;
		this.pierce = pierce;
		this.price = price;
		this.range = range;
		this.speed = speed;
		this.projType = projType;
		this.texture = texture;
		this.damage = damage;
		mapX = x;
		mapY = y;
	}
	public Enemy checkTarget() {
		for(Enemy k: Main.enemies) 
			for(int i = mapY-(int)range; i <= mapY+range; i++) 
				for(int j = mapX-(int)range; j <= mapX+range; j++) 
					if(k.X == j && k.Y == i) {
						return k;
					}

		return null;
		
	}
	//Checks for target in a square around the tower
	
	public Enemy checkTargetOval() {
		for(Enemy k: Main.enemies) {
			if(Math.abs(k.X*Main.tileWidth+Main.tileWidth/2+k.animX-x)/Main.tileWidth <= range && 
			   Math.abs(k.Y*Main.tileHeight+Main.tileHeight/2+k.animY-y)/Main.tileHeight <= range)
				return k;
			//System.out.println(Math.abs( k.Y*Main.tileHeight+Main.tileHeight/2+k.animY-(y+Main.tileHeight/2) )/Main.tileHeight);
		}
		return null;
	}
	//Checks for target in a circle around the tower
	
	public void drawAim(Graphics g) {
		g.setColor(Color.BLACK);
		for(Enemy k: Main.enemies) {
			if(Math.abs(k.X*Main.tileWidth+Main.tileWidth/2+k.animX-x) <= range*Main.tileWidth && 
			   Math.abs(k.Y*Main.tileHeight+Main.tileHeight/2+k.animY-y) <= range*Main.tileHeight	)
				g.drawLine(x, y, k.X*Main.tileWidth+Main.tileWidth/2+(int)k.animX, k.Y*Main.tileHeight+Main.tileHeight/2+(int)k.animY);
		}
	}
	//Draws lines between enemies of tower and the tower itself
	
	public void fire() {
		Enemy target = checkTargetOval();
		//Range option
		
		if(target != null) {
			initialXdif = (target.X)*Main.tileWidth+target.animX+Main.tileWidth/2 - x;
			initialYdif = (target.Y)*Main.tileHeight+target.animY+Main.tileHeight/2 - y;
			angle = Math.atan((double)initialYdif/initialXdif);
			//Calculate angle
			
			if(Main.tick % speed == 0) {
				projectiles.add(new Projectile(x,y, target, pierce,projType, damage));
			}
			//Create new projectile at the towers location
		}
	}
	//Create projectile with conditions
	
	public void drawRange(Graphics2D g, boolean oval) {
		if(!oval) {
			for(int i = mapY-(int)range; i <= mapY+range; i++) {
				for(int j = mapX-(int)range; j <= mapX+range; j++) {
					g.setColor(Color.YELLOW);
					g.drawRect(j*Main.tileWidth, i*Main.tileHeight, Main.tileWidth, Main.tileHeight);
				}
			}
		}
		else {
			g.setColor(new Color(255,255,255,50));
			g.fillOval((int)((mapX-range)*Main.tileWidth), (int)((mapY-range)*Main.tileHeight), (int) ((range*2+1)*Main.tileWidth), (int)((range*2+1)*Main.tileHeight));
		}
		g.drawOval((int)((mapX-range)*Main.tileWidth), (int)((mapY-range)*Main.tileHeight), (int)((range*2+1)*Main.tileWidth), (int)((range*2+1)*Main.tileHeight));

	}
	//Draw the range around the tower (used when building towers)
	
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		//drawRange(g2, true);
		//drawAim(g);
		////To be implemented
		for(Projectile k : projectiles) {
			k.draw(g);
		}
		//Draw projectiles of this tower
		
		if(initialYdif > 0) {
			if(angle < 0) {
				g2.rotate(angle, x,y);
				g.drawImage(texture,this.x-width/2,this.y-height/2,width,height,null);
				g2.rotate(angle*-1, x,y);
			}
			else {
				g2.rotate(angle+ Math.PI, x,y);
				g.drawImage(texture,this.x-width/2,this.y-height/2,width,height,null);
				g2.rotate((angle+ Math.PI)*-1, x,y);
			}
		}
		else if(initialYdif < 0) {
			if(angle > 0) {
				g2.rotate(angle, x,y);
				g.drawImage(texture,this.x-width/2,this.y-height/2,width,height,null);
				g2.rotate(angle*-1,x,y);
			}
			else {
				g2.rotate(angle+ Math.PI, x,y);
				g.drawImage(texture,this.x-width/2,this.y-height/2,width,height,null);
				g2.rotate((angle+ Math.PI)*-1, x,y);
			}
		}
		else if(initialYdif == 0) {
			if(initialXdif < 0)
				g.drawImage(texture,this.x-width/2,this.y-height/2,width,height,null);
			else {
				g2.rotate(Math.PI, x,y);
				g.drawImage(texture,this.x-width/2,this.y-height/2,width,height,null);
				g2.rotate(Math.PI*-1, x,y);
			}
		}
		//Rotating the turret while compensating for the difference in negative and positive angles
	}
	public void incCount() {
		hitCount++;
	}

}
