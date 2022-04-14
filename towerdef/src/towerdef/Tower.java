package towerdef;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Tower extends Entity{
	private int x;
	private int y;
	private int mapX;
	private int mapY;
	private int width = Main.tileWidth;
	private int height = Main.tileHeight;
	private int speed; //per how many ticks
	private double initialXdif;
	private double initialYdif;
	private double angle;
	private double range;
	private int price;
	private int pierce;
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	private int hitCount;
	private int projType;
	private int damage;
	private BufferedImage texture;
	private int projX, projY, projAngle;
	public Tower(int x, int y, int pierce, int price, double range,int speed, int projType, BufferedImage texture, int damage) {
		this.setX(x*Main.tileWidth+Main.tileWidth/2);
		this.setY(y*Main.tileHeight+Main.tileHeight/2);
		this.setPierce(pierce);
		this.setPrice(price);
		this.setRange(range);
		this.setSpeed(speed);
		this.setProjType(projType);
		this.texture = texture;
		this.setDamage(damage);
		mapX = x;
		mapY = y;
	}
	public Enemy checkTarget() {
		for(Enemy k: Main.enemies) 
			for(int i = getMapY()-(int)getRange(); i <= getMapY()+getRange(); i++) 
				for(int j = getMapX()-(int)getRange(); j <= getMapX()+getRange(); j++) 
					if(k.X == j && k.Y == i) {
						return k;
					}

		return null;

	}
	//Checks for target in a square around the tower

	public Enemy checkTargetOval() {
		for(Enemy k: Main.enemies) {
			if(Math.abs(k.X*Main.tileWidth+Main.tileWidth/2+k.animX-getX())/Main.tileWidth <= getRange() && 
					Math.abs(k.Y*Main.tileHeight+Main.tileHeight/2+k.animY-getY())/Main.tileHeight <= getRange())
				return k;
			//System.out.println(Math.abs( k.Y*Main.tileHeight+Main.tileHeight/2+k.animY-(y+Main.tileHeight/2) )/Main.tileHeight);
		}
		return null;
	}
	//Checks for target in a circle around the tower

	public void drawAim(Graphics g) {
		g.setColor(Color.BLACK);
		for(Enemy k: Main.enemies) {
			if(Math.abs(k.X*Main.tileWidth+Main.tileWidth/2+k.animX-getX()) <= getRange()*Main.tileWidth && 
					Math.abs(k.Y*Main.tileHeight+Main.tileHeight/2+k.animY-getY()) <= getRange()*Main.tileHeight	)
				g.drawLine(getX(), getY(), k.X*Main.tileWidth+Main.tileWidth/2+(int)k.animX, k.Y*Main.tileHeight+Main.tileHeight/2+(int)k.animY);
		}
	}
	//Draws lines between enemies of tower and the tower itself

	public void fire() {
		Enemy target = checkTargetOval();
		//Range option

		if(target != null) {
			setInitialXdif((target.X)*Main.tileWidth+target.animX+Main.tileWidth/2 - getX());
			setInitialYdif((target.Y)*Main.tileHeight+target.animY+Main.tileHeight/2 - getY());
			setAngle(Math.atan((double)getInitialYdif()/getInitialXdif()));
			//Calculate angle

			if(Main.tick % getSpeed() == 0) {
				getProjectiles().add(new Projectile(getX(),getY(), target, getPierce(),getProjType(), getDamage(),true,false));
			}
			//Create new projectile at the towers location
		}
	}
	//Create projectile with conditions

	public void drawRange(Graphics2D g, boolean oval) {
		if(!oval) {
			for(int i = getMapY()-(int)getRange(); i <= getMapY()+getRange(); i++) {
				for(int j = getMapX()-(int)getRange(); j <= getMapX()+getRange(); j++) {
					g.setColor(Color.YELLOW);
					g.drawRect(j*Main.tileWidth, i*Main.tileHeight, Main.tileWidth, Main.tileHeight);
				}
			}
		}
		else {
			g.setColor(new Color(255,255,255,50));
			g.fillOval((int)((getMapX()-getRange())*Main.tileWidth), (int)((getMapY()-getRange())*Main.tileHeight), (int) ((getRange()*2+1)*Main.tileWidth), (int)((getRange()*2+1)*Main.tileHeight));
		}
		g.drawOval((int)((getMapX()-getRange())*Main.tileWidth), (int)((getMapY()-getRange())*Main.tileHeight), (int)((getRange()*2+1)*Main.tileWidth), (int)((getRange()*2+1)*Main.tileHeight));

	}
	//Draw the range around the tower (used when building towers)

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if(Main.dev) {
			drawRange(g2, true);
			drawAim(g);
		}
		////To be implemented

		for(int i = 0; i < getProjectiles().size();i++) {
			Projectile k = getProjectiles().get(i);
			if(k != null)
				k.draw(g);

		}
		//Draw projectiles of this tower

		if(getInitialYdif() > 0) {
			if(getAngle() < 0) {
				g2.rotate(getAngle(), getX(),getY());
				g.drawImage(texture,this.getX()-getWidth()/2,this.getY()-getHeight()/2,getWidth(),getHeight(),null);
				g2.rotate(getAngle()*-1, getX(),getY());
			}
			else {
				g2.rotate(getAngle()+ Math.PI, getX(),getY());
				g.drawImage(texture,this.getX()-getWidth()/2,this.getY()-getHeight()/2,getWidth(),getHeight(),null);
				g2.rotate((getAngle()+ Math.PI)*-1, getX(),getY());
			}
		}
		else if(getInitialYdif() < 0) {
			if(getAngle() > 0) {
				g2.rotate(getAngle(), getX(),getY());
				g.drawImage(texture,this.getX()-getWidth()/2,this.getY()-getHeight()/2,getWidth(),getHeight(),null);
				g2.rotate(getAngle()*-1,getX(),getY());
			}
			else {
				g2.rotate(getAngle()+ Math.PI, getX(),getY());
				g.drawImage(texture,this.getX()-getWidth()/2,this.getY()-getHeight()/2,getWidth(),getHeight(),null);
				g2.rotate((getAngle()+ Math.PI)*-1, getX(),getY());
			}
		}
		else if(getInitialYdif() == 0) {
			if(getInitialXdif() < 0)
				g.drawImage(texture,this.getX()-getWidth()/2,this.getY()-getHeight()/2,getWidth(),getHeight(),null);
			else {
				g2.rotate(Math.PI, getX(),getY());
				g.drawImage(texture,this.getX()-getWidth()/2,this.getY()-getHeight()/2,getWidth(),getHeight(),null);
				g2.rotate(Math.PI*-1, getX(),getY());
			}
		}
		//Rotating the turret while compensating for the difference in negative and positive angles
	}
	public void incCount() {
		setHitCount(getHitCount() + 1);
	}
	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}
	public int getMapY() {
		return mapY;
	}
	public int getMapX() {
		return mapX;
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public double getInitialXdif() {
		return initialXdif;
	}
	public void setInitialXdif(double initialXdif) {
		this.initialXdif = initialXdif;
	}
	public double getInitialYdif() {
		return initialYdif;
	}
	public void setInitialYdif(double initialYdif) {
		this.initialYdif = initialYdif;
	}
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getProjType() {
		return projType;
	}
	public void setProjType(int projType) {
		this.projType = projType;
	}
	public int getPierce() {
		return pierce;
	}
	public void setPierce(int pierce) {
		this.pierce = pierce;
	}
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public double getRange() {
		return range;
	}
	public void setRange(double range) {
		this.range = range;
	}
	public int getHitCount() {
		return hitCount;
	}
	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}

}
